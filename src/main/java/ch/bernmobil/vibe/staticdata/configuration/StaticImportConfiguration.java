package ch.bernmobil.vibe.staticdata.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class which configures the jobs by assembling all steps and arranges them in a defined order.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Configuration
@EnableBatchProcessing
public class StaticImportConfiguration {
    @Value("${bernmobil.staticsource.url}")
    private String staticFileUrl;

    @Value("${bernmobil.staticsource.folder}")
    public String destinationFolder;

    private final JobBuilderFactory jobBuilderFactory;
    private final MappingJobConfiguration mappingJobConfiguration;
    private final DataImportJobConfiguration dataImportJobConfiguration;
    private final JobExecutionListener jobExecutionListener;


    @Autowired
    public StaticImportConfiguration(JobBuilderFactory jobBuilderFactory,
        MappingJobConfiguration mappingJobConfiguration,
        DataImportJobConfiguration dataImportJobConfiguration,
        JobExecutionListener jobExecutionListener) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.mappingJobConfiguration = mappingJobConfiguration;
        this.dataImportJobConfiguration = dataImportJobConfiguration;
        this.jobExecutionListener = jobExecutionListener;
    }

    /**
     * Configures the import job.
     * @return A {@link Job} which imports static schedule data.
     */
    @Bean
    public Job importStaticJob(){
        return jobBuilderFactory.get("Import static data")
                .listener(jobExecutionListener)
                .incrementer(new RunIdIncrementer())
                .flow(dataImportJobConfiguration.fileDownloadStep())
                .next(dataImportJobConfiguration.areaImportStep())
                .next(dataImportJobConfiguration.stopImportStep())
                .next(dataImportJobConfiguration.routeImportStep())
                .next(dataImportJobConfiguration.journeyImportStep())
                .next(dataImportJobConfiguration.calendarDateImportStep())
                .next(dataImportJobConfiguration.scheduleImportStep())
                .next(mappingJobConfiguration.areaMapperStep())
                .next(mappingJobConfiguration.calendarDateMapperStep())
                .next(mappingJobConfiguration.journeyMapperStep())
                .next(mappingJobConfiguration.routeMapperStep())
                .next(mappingJobConfiguration.stopMapperStep())
                .end()
                .build();
    }

}
