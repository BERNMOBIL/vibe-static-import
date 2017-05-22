package ch.bernmobil.vibe.staticdata.configuration;


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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class DataImportJobConfiguration {
    @Value("${bernmobil.batch.chunk-size}")
    private int chunkSize;
    @Value("${bernmobil.staticsource.url}")
    private String staticFileUrl;
    @Value("${bernmobil.staticsource.folder}")
    public String destinationFolder;

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource staticDataSource;
    private final ApplicationContext applicationContext;

    @Autowired
    public DataImportJobConfiguration(
            StepBuilderFactory stepBuilderFactory,
            @Qualifier("StaticDataSource") DataSource staticDataSource,
            ApplicationContext applicationContext) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.staticDataSource = staticDataSource;
        this.applicationContext = applicationContext;
    }

    @Bean
    public Step fileDownloadStep() {
        return stepBuilderFactory.get("Download GTFS Files")
                .<ZipInputStream, ZipInputStream>chunk(1)
                .reader(new ZipFileDownload(staticFileUrl))
                .writer(new ZipInputStreamWriter(destinationFolder))
                .build();
    }

    @Bean
    public Step areaImportStep() {
        return createStepBuilder("Area import",
                new AreaImport(staticDataSource, destinationFolder),
                applicationContext.getBean(AreaProcessor.class));
    }

    @Bean
    public Step stopImportStep() {
        return createStepBuilder("Stop import",
                new StopImport(staticDataSource, destinationFolder),
                applicationContext.getBean( StopProcessor.class));
    }

    @Bean
    public Step routeImportStep() {
        return createStepBuilder("Route import",
                new RouteImport(staticDataSource, destinationFolder),
                applicationContext.getBean(RouteProcessor.class));
    }

    @Bean
    public Step calendarDateImportStep() {
        CalendarDateImport importer = new CalendarDateImport(staticDataSource, destinationFolder);
        return createStepBuilder("Calendar-date import",
                importer.reader(),
                importer.listItemWriter(),
                applicationContext.getBean(CalendarDateProcessor.class));
    }

    @Bean
    public Step journeyImportStep() {
        return createStepBuilder("Journey import",
                new TripImport(staticDataSource, destinationFolder),
                applicationContext.getBean(JourneyProcessor.class));
    }

    @Bean
    public Step scheduleImportStep() {
        return createStepBuilder("Schedule import",
                new StopTimeImport(staticDataSource, destinationFolder),
                applicationContext.getBean(ScheduleProcessor.class));
    }

    private <TIn, TOut> Step createStepBuilder(String name,
            Import<TIn, TOut> importer,
            ItemProcessor<TIn, TOut> processor) {
        return createStepBuilder(name, importer.reader(), importer.writer(), processor);
    }

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
