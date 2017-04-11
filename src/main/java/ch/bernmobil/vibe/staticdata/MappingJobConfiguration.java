package ch.bernmobil.vibe.staticdata;

import ch.bernmobil.vibe.staticdata.mapper.AreaMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.CalendarDateMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.JourneyMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.Mapper;
import ch.bernmobil.vibe.staticdata.mapper.RouteMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.StopMapperHelper;
import javax.sql.DataSource;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class MappingJobConfiguration {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource mapperDataSource;
    private static final int chunkSize = 100;

    public MappingJobConfiguration(
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("MapperDataSource")DataSource mapperDataSource) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.mapperDataSource = mapperDataSource;
    }

    @Bean
    public Step areaMapperStep() {
        AreaMapperHelper helper = new AreaMapperHelper(mapperDataSource);
        return buildMappingStep(helper, "area mapper");
    }

    @Bean
    public Step calendarDateMapperStep() {
        CalendarDateMapperHelper helper = new CalendarDateMapperHelper(mapperDataSource);
        return buildMappingStep(helper, "calendar date mapper");
    }

    @Bean
    public Step journeyMapperStep() {
        JourneyMapperHelper helper = new JourneyMapperHelper(mapperDataSource);
        return buildMappingStep(helper, "journey mapper");
    }

    @Bean
    public Step routeMapperStep() {
        RouteMapperHelper helper = new RouteMapperHelper(mapperDataSource);
        return buildMappingStep(helper, "route mapper");
    }

    @Bean
    public Step stopMapperStep() {
        StopMapperHelper helper = new StopMapperHelper(mapperDataSource);
        return buildMappingStep(helper, "stop mapper");
    }

    private <T> Step buildMappingStep(Mapper<T> mappingHelper, String stepName) {
        return stepBuilderFactory.get(stepName)
                .<T, T>chunk(chunkSize)
                .reader(mappingHelper.reader())
                .writer(mappingHelper.writer())
                .build();
    }
}
