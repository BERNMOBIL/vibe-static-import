package ch.bernmobil.vibe.staticdata;

import ch.bernmobil.vibe.staticdata.importer.AreaImport;
import ch.bernmobil.vibe.staticdata.importer.CalendarDateImport;
import ch.bernmobil.vibe.staticdata.importer.Import;
import ch.bernmobil.vibe.staticdata.importer.RouteImport;
import ch.bernmobil.vibe.staticdata.importer.StopImport;
import ch.bernmobil.vibe.staticdata.importer.StopTimeImport;
import ch.bernmobil.vibe.staticdata.importer.TripImport;
import ch.bernmobil.vibe.staticdata.importer.ZipFileDownload;
import ch.bernmobil.vibe.staticdata.processor.AreaProcessor;
import ch.bernmobil.vibe.staticdata.processor.CalendarDateProcessor;
import ch.bernmobil.vibe.staticdata.processor.JourneyProcessor;
import ch.bernmobil.vibe.staticdata.processor.RouteProcessor;
import ch.bernmobil.vibe.staticdata.processor.ScheduleProcessor;
import ch.bernmobil.vibe.staticdata.processor.StopProcessor;
import ch.bernmobil.vibe.staticdata.writer.ZipInputStreamWriter;
import java.util.zip.ZipInputStream;
import javax.sql.DataSource;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class DataImportJobConfiguration {
    @Value("${bernmobil.staticsource.url}")
    private String staticFileUrl;

    @Value("${bernmobil.staticsource.folder}")
    public String destinationFolder;

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource postgresDataSource;

    private static final int CHUNK_SIZE = 100;

    private final ApplicationContext applicationContext;

    @Autowired
    public DataImportJobConfiguration(
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("PostgresDataSource")DataSource postgresDataSource,
            ApplicationContext applicationContext) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.postgresDataSource = postgresDataSource;
        this.applicationContext = applicationContext;
    }

    @Bean
    public Step fileDownloadStep() {
        return stepBuilderFactory.get("download GTFS Files")
                .<ZipInputStream, ZipInputStream>chunk(1)
                .reader(new ZipFileDownload(staticFileUrl))
                .writer(new ZipInputStreamWriter(destinationFolder))
                //TODO let whole job fail if these steps fail
                .build();
    }

    @Bean
    public Step areaImportStep() {
        return createStepBuilder("area import", new AreaImport(postgresDataSource, destinationFolder), applicationContext.getBean(AreaProcessor.class));
    }

    @Bean
    public Step stopImportStep() {
        return createStepBuilder("stop import", new StopImport(postgresDataSource, destinationFolder), applicationContext.getBean( StopProcessor.class));
    }

    @Bean
    public Step routeImportStep() {
        return createStepBuilder("route import", new RouteImport(postgresDataSource, destinationFolder), applicationContext.getBean( RouteProcessor.class));
    }

    @Bean
    public Step calendarDateImportStep() {
        return createStepBuilder("calendar-date import", new CalendarDateImport(postgresDataSource, destinationFolder), applicationContext.getBean( CalendarDateProcessor.class));
    }

    @Bean
    public Step journeyImportStep() {
        return createStepBuilder("journey import", new TripImport(postgresDataSource, destinationFolder), applicationContext.getBean( JourneyProcessor.class));
    }

    @Bean
    public Step scheduleImportStep() {
        return createStepBuilder("schedule import", new StopTimeImport(postgresDataSource, destinationFolder), applicationContext.getBean( ScheduleProcessor.class));
    }

    private <TIn, TOut> Step createStepBuilder(String name, Import<TIn, TOut> importer, ItemProcessor<TIn, TOut> processor) {
        return stepBuilderFactory.get(name)
                .<TIn, TOut>chunk(CHUNK_SIZE)
                .reader(importer.reader())
                .processor(processor)
                .writer(importer.writer())
                .build();
    }
}
