package ch.bernmobil.vibe.staticdata;

import ch.bernmobil.vibe.staticdata.mapper.AreaMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.CalendarDateMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.JourneyMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.Mapper;
import ch.bernmobil.vibe.staticdata.mapper.RouteMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.StopMapperHelper;
import ch.bernmobil.vibe.staticdata.mapper.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.mapper.store.MapperStore;
import ch.bernmobil.vibe.staticdata.mapper.sync.AreaMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.CalendarDateMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.RouteMapping;
import ch.bernmobil.vibe.staticdata.mapper.sync.StopMapping;
import javax.sql.DataSource;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class MappingJobConfiguration {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource mapperDataSource;
    private static final int CHUNK_SIZE = 100;
    private final ApplicationContext applicationContext;

    @Autowired
    public MappingJobConfiguration(
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("MapperDataSource") DataSource mapperDataSource,
            ApplicationContext applicationContext) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.mapperDataSource = mapperDataSource;
        this.applicationContext = applicationContext;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Step areaMapperStep() {
        MapperStore<String, AreaMapping> mapperStore =
                (MapperStore<String, AreaMapping>)applicationContext.getBean(MapperStore.class);

        AreaMapperHelper helper = new AreaMapperHelper(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "area mapper");
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Step calendarDateMapperStep() {
        MapperStore<Long, CalendarDateMapping> mapperStore =
                (MapperStore<Long, CalendarDateMapping>)applicationContext.getBean(MapperStore.class);

        CalendarDateMapperHelper helper = new CalendarDateMapperHelper(mapperDataSource,
                mapperStore);
        return buildMappingStep(helper, "calendar date mapper");
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Step journeyMapperStep() {
        JourneyMapperStore mapperStore = applicationContext.getBean(JourneyMapperStore.class);

        JourneyMapperHelper helper = new JourneyMapperHelper(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "journey mapper");
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Step routeMapperStep() {
        MapperStore<String, RouteMapping> mapperStore =
                (MapperStore<String, RouteMapping>)applicationContext.getBean(MapperStore.class);

        RouteMapperHelper helper = new RouteMapperHelper(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "route mapper");
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Step stopMapperStep() {
        MapperStore<String, StopMapping> mapperStore =
                (MapperStore<String, StopMapping>)applicationContext.getBean(MapperStore.class);

        StopMapperHelper helper = new StopMapperHelper(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "stop mapper");
    }

    private <T> Step buildMappingStep(Mapper<T> mappingHelper, String stepName) {
        return stepBuilderFactory.get(stepName)
                .<T, T>chunk(CHUNK_SIZE)
                .reader(mappingHelper.reader())
                .writer(mappingHelper.writer())
                .build();
    }
}
