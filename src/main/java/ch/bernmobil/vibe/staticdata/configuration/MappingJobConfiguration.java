package ch.bernmobil.vibe.staticdata.configuration;

import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.AreaMapperImport;
import ch.bernmobil.vibe.staticdata.importer.mapping.CalendarDateMapperImport;
import ch.bernmobil.vibe.staticdata.importer.mapping.JourneyMapperImport;
import ch.bernmobil.vibe.staticdata.importer.mapping.MapperImport;
import ch.bernmobil.vibe.staticdata.importer.mapping.RouteMapperImport;
import ch.bernmobil.vibe.staticdata.importer.mapping.StopMapperImport;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import javax.sql.DataSource;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class MappingJobConfiguration {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource mapperDataSource;
    @Value("${bernmobil.batch.chunk-size}")
    private int chunkSize;
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
    public Step areaMapperStep() {
        MapperStore<String, AreaMapping> mapperStore = getMapperStore("areaMapperStore");
        AreaMapperImport helper = new AreaMapperImport(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "Area mapper");
    }

    @Bean
    public Step calendarDateMapperStep() {
        MapperStore<Long, CalendarDateMapping> mapperStore = getMapperStore("calendarDateMapperStore");
        CalendarDateMapperImport helper = new CalendarDateMapperImport(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "Calendar date mapper");
    }

    @Bean
    public Step journeyMapperStep() {
        JourneyMapperStore mapperStore = (JourneyMapperStore)applicationContext.getBean("journeyMapperStore");
        JourneyMapperImport helper = new JourneyMapperImport(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "Journey mapper");
    }

    @Bean
    public Step routeMapperStep() {
        MapperStore<String, RouteMapping> mapperStore = getMapperStore("routeMapperStore");
        RouteMapperImport helper = new RouteMapperImport(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "Route mapper");
    }

    @Bean
    public Step stopMapperStep() {
        MapperStore<String, StopMapping> mapperStore = getMapperStore("stopMapperStore");
        StopMapperImport helper = new StopMapperImport(mapperDataSource, mapperStore);
        return buildMappingStep(helper, "Stop mapper");
    }

    @SuppressWarnings("unchecked")
    private <I, O> MapperStore<I, O> getMapperStore(String beanName) {
        return (MapperStore<I, O>)applicationContext.getBean(beanName);
    }

    private <T> Step buildMappingStep(MapperImport<T> mappingHelper, String stepName) {
        return stepBuilderFactory.get(stepName)
                .<T, T>chunk(chunkSize)
                .reader(mappingHelper.reader())
                .writer(mappingHelper.writer())
                .build();
    }
}
