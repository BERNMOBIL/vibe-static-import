package ch.bernmobil.vibe.staticdata;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing
public class StaticImportConfiguration {
    @Value("${bernmobil.staticsource.url}")
    private String staticFileUrl;

    @Value("${bernmobil.staticsource.folder}")
    public String destinationFolder;

    private final JobBuilderFactory jobBuilderFactory;
    private MappingJobConfiguration mappingJobConfiguration;
    private DataImportJobConfiguration dataImportJobConfiguration;
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

    @Bean
    public Job importStaticJob(){
        return jobBuilderFactory.get("importStaticJob")
                .listener(jobExecutionListener)
                .incrementer(new RunIdIncrementer())
                //.flow(dataImportJobConfiguration.fileDownloadStep())
                .flow(dataImportJobConfiguration.areaImportStep())
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
