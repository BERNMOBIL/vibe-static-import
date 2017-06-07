package ch.bernmobil.vibe.staticdata.configuration;

import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.*;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import org.jooq.DSLContext;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class MappingJobConfiguration {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource mapperDataSource;
    private final ApplicationContext applicationContext;
    private final DSLContext dslContext;
    @Value("${bernmobil.batch.chunk-size}")
    private int chunkSize;

    @Autowired
    public MappingJobConfiguration(
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("MapperDataSource") DataSource mapperDataSource,
            ApplicationContext applicationContext,
            @Qualifier("MapperDslContext") DSLContext dslContext) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.mapperDataSource = mapperDataSource;
        this.applicationContext = applicationContext;
        this.dslContext = dslContext;
    }

    @Bean
    public Step areaMapperStep() {
        MapperStore<String, AreaMapping> mapperStore = getMapperStore("areaMapperStore");
        AreaMapperImport helper = new AreaMapperImport(mapperDataSource, mapperStore, dslContext);
        return buildMappingStep(helper, "Area mapper");
    }

    @Bean
    public Step calendarDateMapperStep() {
        MapperStore<Long, CalendarDateMapping> mapperStore = getMapperStore("calendarDateMapperStore");
        CalendarDateMapperImport helper = new CalendarDateMapperImport(mapperDataSource, mapperStore, dslContext);
        return buildMappingStep(helper, "Calendar date mapper");
    }

    @Bean
    public Step journeyMapperStep() {
        JourneyMapperStore mapperStore = (JourneyMapperStore)applicationContext.getBean("journeyMapperStore");
        JourneyMapperImport helper = new JourneyMapperImport(mapperDataSource, mapperStore, dslContext);
        return buildMappingStep(helper, "Journey mapper");
    }

    @Bean
    public Step routeMapperStep() {
        MapperStore<String, RouteMapping> mapperStore = getMapperStore("routeMapperStore");
        RouteMapperImport helper = new RouteMapperImport(mapperDataSource, mapperStore, dslContext);
        return buildMappingStep(helper, "Route mapper");
    }

    @Bean
    public Step stopMapperStep() {
        MapperStore<String, StopMapping> mapperStore = getMapperStore("stopMapperStore");
        StopMapperImport helper = new StopMapperImport(mapperDataSource, mapperStore, dslContext);
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
