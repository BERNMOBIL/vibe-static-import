package ch.bernmobil.vibe.staticdata.configuration;

import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.shared.mapping.AreaMapping;
import ch.bernmobil.vibe.shared.mapping.CalendarDateMapping;
import ch.bernmobil.vibe.shared.mapping.RouteMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.staticdata.importer.mapping.*;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.JourneyMapperStore;
import ch.bernmobil.vibe.staticdata.importer.mapping.store.MapperStore;
import ch.bernmobil.vibe.staticdata.reader.LazyListItemReader;
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

/**
 * Configure the {@link Step} which are necessary to save all mappings.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Configuration
@EnableBatchProcessing
public class MappingJobConfiguration {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource mapperDataSource;
    private final ApplicationContext applicationContext;
    private final DSLContext dslContext;
    private final UpdateTimestampManager updateTimestampManager;
    @Value("${bernmobil.batch.chunk-size}")
    private int chunkSize;

    @Autowired
    public MappingJobConfiguration(
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("MapperDataSource") DataSource mapperDataSource,
            ApplicationContext applicationContext,
            @Qualifier("MapperDslContext") DSLContext dslContext,
            UpdateTimestampManager updateTimestampManager) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.mapperDataSource = mapperDataSource;
        this.applicationContext = applicationContext;
        this.dslContext = dslContext;
        this.updateTimestampManager = updateTimestampManager;
    }

    /**
     * Create a {@link Step} which is able to read {@link AreaMapping} from a {@link LazyListItemReader}
     * and write it into a {@link org.springframework.batch.item.database.JdbcBatchItemWriter}.
     * @return {@link Step} which saves {@link AreaMapping}
     */
    @Bean
    public Step areaMapperStep() {
        MapperStore<String, AreaMapping> mapperStore = getMapperStore("areaMapperStore");
        AreaMapperImport helper = new AreaMapperImport(mapperDataSource, mapperStore, dslContext, updateTimestampManager);
        return buildMappingStep(helper, "Area mapper");
    }

    /**
     * Create a {@link Step} which is able to read {@link CalendarDateMapping} from a {@link LazyListItemReader}
     * and write it into a {@link org.springframework.batch.item.database.JdbcBatchItemWriter}.
     * @return {@link Step} which saves {@link CalendarDateMapping}
     */
    @Bean
    public Step calendarDateMapperStep() {
        MapperStore<Long, CalendarDateMapping> mapperStore = getMapperStore("calendarDateMapperStore");
        CalendarDateMapperImport helper = new CalendarDateMapperImport(mapperDataSource, mapperStore, dslContext, updateTimestampManager);
        return buildMappingStep(helper, "Calendar date mapper");
    }

    /**
     * Create a {@link Step} which is able to read {@link ch.bernmobil.vibe.shared.mapping.JourneyMapping}
     * from a {@link LazyListItemReader} and write
     * it into a {@link org.springframework.batch.item.database.JdbcBatchItemWriter}.
     * @return {@link Step} which saves {@link ch.bernmobil.vibe.shared.mapping.JourneyMapping}
     */
    @Bean
    public Step journeyMapperStep() {
        JourneyMapperStore mapperStore = (JourneyMapperStore)applicationContext.getBean("journeyMapperStore");
        JourneyMapperImport helper = new JourneyMapperImport(mapperDataSource, mapperStore, dslContext, updateTimestampManager);
        return buildMappingStep(helper, "Journey mapper");
    }

    /**
     * Create a {@link Step} which is able to read {@link RouteMapping} from a {@link LazyListItemReader}
     * and write it into a {@link org.springframework.batch.item.database.JdbcBatchItemWriter}.
     * @return {@link Step} which saves {@link RouteMapping}
     */
    @Bean
    public Step routeMapperStep() {
        MapperStore<String, RouteMapping> mapperStore = getMapperStore("routeMapperStore");
        RouteMapperImport helper = new RouteMapperImport(mapperDataSource, mapperStore, dslContext, updateTimestampManager);
        return buildMappingStep(helper, "Route mapper");
    }

    /**
     * Create a {@link Step} which is able to read {@link StopMapping} from a {@link LazyListItemReader}
     * and write it into a {@link org.springframework.batch.item.database.JdbcBatchItemWriter}.
     * @return {@link Step} which saves {@link StopMapping}
     */
    @Bean
    public Step stopMapperStep() {
        MapperStore<String, StopMapping> mapperStore = getMapperStore("stopMapperStore");
        StopMapperImport helper = new StopMapperImport(mapperDataSource, mapperStore, dslContext, updateTimestampManager);
        return buildMappingStep(helper, "Stop mapper");
    }

    /**
     * Get a {@link MapperStore} by bean name from the currently loaded {@link ApplicationContext}.
     * @param beanName of the {@link MapperStore}
     * @param <I> ID type usually {@link String} or int.
     * @param <O> Mapping type.
     * @return A {@link MapperStore} by name.
     */
    @SuppressWarnings("unchecked")
    private <I, O> MapperStore<I, O> getMapperStore(String beanName) {
        return (MapperStore<I, O>)applicationContext.getBean(beanName);
    }

    /**
     * Build a {@link Step} which uses {@link org.springframework.batch.item.ItemReader} and
     * {@link org.springframework.batch.item.ItemWriter} from a {@link MapperImport} configuration instance.
     * @param mappingHelper which provides the corresponding objects.
     * @param stepName which is printed in the command line and the Spring Batch database.
     * @param <T> Type of the mapping
     * @return A fully configured step.
     */
    private <T> Step buildMappingStep(MapperImport<T> mappingHelper, String stepName) {
        return stepBuilderFactory.get(stepName)
                .<T, T>chunk(chunkSize)
                .reader(mappingHelper.reader())
                .writer(mappingHelper.writer())
                .build();
    }
}
