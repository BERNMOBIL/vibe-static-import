package ch.bernmobil.vibe.staticdata;

import ch.bernmobil.vibe.staticdata.entity.sync.AreaMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.CalendarDateMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.JourneyMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.RouteMapper;
import ch.bernmobil.vibe.staticdata.entity.sync.StopMapper;
import ch.bernmobil.vibe.staticdata.importer.AreaImport;
import ch.bernmobil.vibe.staticdata.importer.CalendarDateImport;
import ch.bernmobil.vibe.staticdata.importer.Import;
import ch.bernmobil.vibe.staticdata.importer.RouteImport;
import ch.bernmobil.vibe.staticdata.importer.StopImport;
import ch.bernmobil.vibe.staticdata.importer.StopTimeImport;
import ch.bernmobil.vibe.staticdata.importer.TripImport;
import ch.bernmobil.vibe.staticdata.processor.AreaProcessor;
import ch.bernmobil.vibe.staticdata.processor.CalendarDateProcessor;
import ch.bernmobil.vibe.staticdata.processor.JourneyProcessor;
import ch.bernmobil.vibe.staticdata.processor.RouteProcessor;
import ch.bernmobil.vibe.staticdata.processor.ScheduleProcessor;
import ch.bernmobil.vibe.staticdata.processor.StopProcessor;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class StaticImportConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private static final int chunkSize = 100;

    @Autowired
    public StaticImportConfiguration(JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory, @Qualifier("PostgresDataSource") DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    public Job importStaticJob(){
        return jobBuilderFactory.get("importStaticJob")
                .incrementer(new RunIdIncrementer())
                .flow(areaImportStep())
                .next(stopImportStep())
                .next(routeImportStep())
                .next(journeyImportStep())
                .next(calendarDateImportStep())
                .next(scheduleImportStep())
                .next(areaMapperStep())
                .next(calendarDateMapperStep())
                .next(journeyMapperStep())
                .next(routeMapperStep())
                .next(stopMapperStep())
                .end()
                .build();
    }

    @Bean
    public Step areaMapperStep() {
        String query =  "INSERT INTO area_mapper(gtfs_id, id) VALUES(?, ?)";
        ItemPreparedStatementSetter<AreaMapper> setter = ((item, ps) -> {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        });
        return createSaveMappingStep("areaMapper",
                new AreaMapper.BatchReader(), buildJdbcItemWriter(query, setter));
    }

    @Bean
    public Step calendarDateMapperStep() {
        String query =  "INSERT INTO calendar_date_mapper(gtfs_id, id) VALUES(?, ?)";
        ItemPreparedStatementSetter<CalendarDateMapper> setter = ((item, ps) -> {
            ps.setLong(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        });
        return createSaveMappingStep("calendarMapper", new CalendarDateMapper.BatchReader(), buildJdbcItemWriter(query, setter));
    }

    @Bean
    public Step journeyMapperStep() {
        String query =  "INSERT INTO journey_mapper(gtfs_trip_id, gtfs_service_id, id) VALUES(?, ?, ?)";
        ItemPreparedStatementSetter<JourneyMapper> setter = ((item, ps) -> {
            ps.setString(1, item.getGtfsTripId());
            ps.setString(2, item.getGtfsServiceId());
            ps.setLong(3, item.getId());
        });
        return createSaveMappingStep("journeyMapper", new JourneyMapper.BatchReader(), buildJdbcItemWriter(query, setter));
    }

    @Bean
    public Step routeMapperStep() {
        String query =  "INSERT INTO route_mapper(gtfs_id, id) VALUES(?, ?)";
        ItemPreparedStatementSetter<RouteMapper> setter = ((item, ps) -> {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        });
        return createSaveMappingStep("routeMapper", new RouteMapper.BatchReader(), buildJdbcItemWriter(query, setter));
    }

    @Bean
    public Step stopMapperStep() {
        String query =  "INSERT INTO stop_mapper(gtfs_id, id) VALUES(?, ?)";
        ItemPreparedStatementSetter<StopMapper> setter = ((item, ps) -> {
            ps.setString(1, item.getGtfsId());
            ps.setLong(2, item.getId());
        });
        return createSaveMappingStep("stopMapper",
                new StopMapper.BatchReader(), buildJdbcItemWriter(query, setter));
    }

    @Bean
    public Step areaImportStep() {
        return createStepBuilder("areaImportStep", new AreaImport(dataSource), new AreaProcessor());
    }

    @Bean
    public Step stopImportStep() {
        return createStepBuilder("stopImportStep", new StopImport(dataSource), new StopProcessor());
    }

    @Bean
    public Step routeImportStep() {
        return createStepBuilder("routeImportStep", new RouteImport(dataSource), new RouteProcessor());
    }

    @Bean
    public Step calendarDateImportStep() {
        return createStepBuilder("calendarDateImportStep", new CalendarDateImport(dataSource), new CalendarDateProcessor());
    }

    @Bean
    public Step journeyImportStep() {
        return createStepBuilder("journeyImportStep", new TripImport(dataSource), new JourneyProcessor());
    }

    @Bean
    public Step scheduleImportStep() {
        return createStepBuilder("scheduleImportStep", new StopTimeImport(dataSource), new ScheduleProcessor());
    }

    private <TIn, TOut> Step createStepBuilder(String name, Import<TIn, TOut> importer,
            ItemProcessor<TIn, TOut> processor) {
        return stepBuilderFactory.get(name)
            .<TIn, TOut>chunk(chunkSize)
            .reader(importer.reader())
            .processor(processor)
            .writer(importer.writer())
            .build();
    }

    private <T> Step createSaveMappingStep(String name, ItemReader<T> reader, ItemWriter<T> writer) {
        return stepBuilderFactory.get(name)
            .<T, T>chunk(chunkSize)
            .reader(reader)
            .writer(writer)
            .build();
    }

    private <T> JdbcBatchItemWriter<T> buildJdbcItemWriter(String preparedStatement, ItemPreparedStatementSetter<T> preparedStatementSetter){
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<>();
        writer.setSql(preparedStatement);
        writer.setItemPreparedStatementSetter(preparedStatementSetter);
        writer.setDataSource(dataSource);
        return writer;
    }
}
