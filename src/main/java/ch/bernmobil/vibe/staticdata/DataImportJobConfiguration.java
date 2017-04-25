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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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


    public DataImportJobConfiguration(
        StepBuilderFactory stepBuilderFactory,
        @Qualifier("PostgresDataSource") DataSource postgresDataSource) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.postgresDataSource = postgresDataSource;
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
        return createStepBuilder(
            "areaImportStep",
            new AreaImport(postgresDataSource, destinationFolder),
            new AreaProcessor()
        );
    }

    @Bean
    public Step stopImportStep() {
        return createStepBuilder(
            "stopImportStep",
            new StopImport(postgresDataSource, destinationFolder),
            new StopProcessor()
        );
    }

    @Bean
    public Step routeImportStep() {
        return createStepBuilder(
            "routeImportStep",
            new RouteImport(postgresDataSource, destinationFolder),
            new RouteProcessor()
        );
    }

    @Bean
    public Step calendarDateImportStep() {
        return createStepBuilder(
            "calendarDateImportStep",
            new CalendarDateImport(postgresDataSource, destinationFolder),
            new CalendarDateProcessor()
        );
    }

    @Bean
    public Step journeyImportStep() {
        return createStepBuilder(
            "journeyImportStep",
            new TripImport(postgresDataSource, destinationFolder),
            new JourneyProcessor()
        );
    }

    @Bean
    public Step scheduleImportStep() {
        return createStepBuilder("scheduleImportStep",
            new StopTimeImport(postgresDataSource, destinationFolder),
            new ScheduleProcessor()
        );
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
