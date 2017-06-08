package ch.bernmobil.vibe.staticdata.configuration;


import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import ch.bernmobil.vibe.staticdata.importer.*;
import ch.bernmobil.vibe.staticdata.processor.*;
import ch.bernmobil.vibe.staticdata.reader.OneTimeItemStreamReader;
import ch.bernmobil.vibe.staticdata.writer.ZipInputStreamWriter;
import org.jooq.DSLContext;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.zip.ZipInputStream;

/**
 * Configure the {@link Step} which are necessary to download and process GTFS entities.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Configuration
@EnableBatchProcessing
public class DataImportJobConfiguration {
    @Value("${bernmobil.batch.chunk-size}")
    private int chunkSize;
    @Value("${bernmobil.staticsource.url}")
    private String staticFileUrl;
    @Value("${bernmobil.staticsource.folder}")
    private String destinationFolder;

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource staticDataSource;
    private final ApplicationContext applicationContext;
    private final DSLContext dslContext;
    private final UpdateTimestampManager updateTimestampManager;

    @Autowired
    public DataImportJobConfiguration(
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("StaticDataSource") DataSource staticDataSource,
            ApplicationContext applicationContext,
            @Qualifier("StaticDslContext") DSLContext dslContext,
            UpdateTimestampManager updateTimestampManager) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.staticDataSource = staticDataSource;
        this.applicationContext = applicationContext;
        this.dslContext = dslContext;
        this.updateTimestampManager = updateTimestampManager;
    }

    /**
     * Creates a {@link Step} which reads zipped, static GTFS files and writes them to the filesystem
     * @return {@link Step} which downloads and saves a ZIP file.
     */
    @Bean
    public Step fileDownloadStep() {
        return stepBuilderFactory.get("Download GTFS Files")
                .<ZipInputStream, ZipInputStream>chunk(1)
                .reader(new OneTimeItemStreamReader<>(new ZipFileDownload(staticFileUrl)))
                .writer(new ZipInputStreamWriter(destinationFolder))
                .build();
    }

    /**
     * Create a {@link Step} to convert a subset of {@link ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStop} to
     * {@link ch.bernmobil.vibe.shared.entitiy.Area}.
     * @return {@link Step} which converts GTFS entities.
     *
     * @see AreaImport
     */
    @Bean
    public Step areaImportStep() {
        return createStepBuilder("Area import",
                new AreaImport(staticDataSource, dslContext, destinationFolder, updateTimestampManager),
                applicationContext.getBean(AreaProcessor.class));
    }

    /**
     * Create a {@link Step} to convert a subset of {@link ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStop} to
     * {@link ch.bernmobil.vibe.shared.entitiy.Area}.
     * @return {@link Step} which converts GTFS entities.
     *
     * @see StopImport
     */
    @Bean
    public Step stopImportStep() {
        return createStepBuilder("Stop import",
                new StopImport(staticDataSource, dslContext, destinationFolder, updateTimestampManager),
                applicationContext.getBean(StopProcessor.class));
    }

    /**
     * Create a {@link Step} to convert {@link ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsRoute} to
     * {@link ch.bernmobil.vibe.shared.entitiy.Route}.
     * @return {@link Step} which converts GTFS entities.
     */
    @Bean
    public Step routeImportStep() {
        return createStepBuilder("Route import",
                new RouteImport(staticDataSource, dslContext, destinationFolder, updateTimestampManager),
                applicationContext.getBean(RouteProcessor.class));
    }

    /**
     * Create a {@link Step} to convert {@link ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsCalendarDate} to
     * {@link ch.bernmobil.vibe.shared.entitiy.CalendarDate}.
     * @return {@link Step} which converts GTFS entities.
     */
    @Bean
    public Step calendarDateImportStep() {
        CalendarDateImport importer = new CalendarDateImport(staticDataSource, dslContext, destinationFolder, updateTimestampManager);
        return createStepBuilder("Calendar-date import",
                importer.reader(),
                importer.listItemWriter(),
                applicationContext.getBean(CalendarDateProcessor.class));
    }

    /**
     * Create a {@link Step} to convert {@link ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsTrip} to
     * {@link ch.bernmobil.vibe.shared.entitiy.Journey}.
     * @return {@link Step} which converts GTFS entities.
     */
    @Bean
    public Step journeyImportStep() {
        return createStepBuilder("Journey import",
                new TripImport(staticDataSource, dslContext, destinationFolder, updateTimestampManager),
                applicationContext.getBean(JourneyProcessor.class));
    }

    /**
     * Create a {@link Step} to convert {@link ch.bernmobil.vibe.staticdata.gtfs.entitiy.GtfsStopTime} to
     * {@link ch.bernmobil.vibe.shared.entitiy.Schedule}.
     * @return {@link Step} which converts GTFS entities.
     */
    @Bean
    public Step scheduleImportStep() {
        return createStepBuilder("Schedule import",
                new StopTimeImport(staticDataSource, dslContext, destinationFolder, updateTimestampManager),
                applicationContext.getBean(ScheduleProcessor.class));
    }

    /**
     * Create a {@link org.springframework.batch.core.step.builder.StepBuilder} which consists of a {@link ItemReader}
     * and {@link ItemWriter}, which are configured in a subclass of {@link Import}, and a {@link ItemProcessor}.
     * @param name of the {@link Step} which will be displayed in the logs and written into the database.
     * @param importer which provides the {@link ItemReader} and {@link ItemWriter}.
     * @param processor which is able to process {@link TIn} to {@link TOut}.
     * @param <TIn> type which is read by the reader.
     * @param <TOut> type which is written by the writer.
     * @return {@link Step} which has been built from the given parameters.
     */
    private <TIn, TOut> Step createStepBuilder(String name,
            Import<TIn, TOut> importer,
            ItemProcessor<TIn, TOut> processor) {
        return createStepBuilder(name, importer.reader(), importer.writer(), processor);
    }

    /**
     * Creata a {@link org.springframework.batch.core.step.builder.StepBuilder} which consists of a {@link ItemReader},
     * and {@link ItemProcessor} and a {@link ItemWriter}.
     * @param name of the {@link Step} which will be displayed in the logs and written into the database.
     * @param reader which is able to read types of {@link TIn}.
     * @param writer which is able to write types of {@link TOut}.
     * @param processor which is able to process {@link TIn} into {@link TOut}.
     * @param <TIn> type which is read by the reader.
     * @param <TOut> type which is written by the writer.
     * @return {@link Step} which has been built from the given parameters.
     */
    private <TIn, TOut> Step createStepBuilder(String name,
            ItemReader<TIn> reader,
            ItemWriter<TOut> writer,
            ItemProcessor<TIn, TOut> processor) {
        return stepBuilderFactory.get(name)
                .<TIn, TOut>chunk(chunkSize)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
