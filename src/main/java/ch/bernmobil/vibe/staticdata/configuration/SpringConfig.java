package ch.bernmobil.vibe.staticdata.configuration;

import ch.bernmobil.vibe.shared.UpdateHistoryRepository;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.UpdateManagerRepository;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class SpringConfig {
    @Value("classpath:/org/springframework/batch/core/schema-drop-sqlite.sql")
    private Resource dropRepositoryTables;

    @Value("classpath:/org/springframework/batch/core/schema-sqlite.sql")
    private Resource dataRepositorySchema;

    @Value("mapperDatabaseSchema.sql")
    private Resource mapperDatabaseSchema;

    @Value("dropMapperDatabase.sql")
    private Resource mapperDropDatabase;

    @Value("truncatePostgres.sql")
    private Resource truncatePostgres;

    private Environment environment;

    @Primary
    @Bean
    public DataSource sqliteDataSource() {
        String url = String.format("%s:%s",
                        environment.getProperty("bernmobil.jobrepository.datasource"),
                        environment.getProperty("bernmobil.jobrepository.name"));
        return createDataSource(environment.getProperty("bernmobil.jobrepository.driver-class-name"), url);
    }

    @Bean("MapperDataSource")
    public DataSource mapperDataSource() {
        return createDataSource(environment.getProperty("spring.datasource.driver-class-name"),
                environment.getProperty("bernmobil.mappingrepository.datasource.url"),
                environment.getProperty("bernmobil.mappingrepository.datasource.username"),
                environment.getProperty("bernmobil.mappingrepository.datasource.password"));
    }

    @Bean("StaticDataSource")
    public DataSource StaticDataSource() {
        return createDataSource(environment.getProperty("spring.datasource.driver-class-name"),
                environment.getProperty("spring.datasource.url"),
                environment.getProperty("spring.datasource.username"),
                environment.getProperty("spring.datasource.password"));
    }

    @Bean("JobRepositoryInitializer")
    public DataSourceInitializer jobRepositoryInitializer(DataSource dataSource) throws MalformedURLException {
        if(Files.exists(Paths.get(environment.getProperty("bernmobil.jobrepository.name")))) {
            return dataSourceInitializer(dataSource);
        }
        return dataSourceInitializer(dataSource, dataRepositorySchema);
    }

    @Bean("jobLauncher")
    public JobLauncher jobLauncher(PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository(transactionManager));
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public UpdateManager updateManager(@Qualifier("MapperRepository") UpdateManagerRepository mapperRepository,
            @Qualifier("StaticRepository") UpdateManagerRepository staticRepository,
            UpdateHistoryRepository updateHistoryRepository) {
        int historySize = environment.getProperty("bernmobil.history.size", Integer.class);
        Duration timeout = Duration.ofMinutes(environment.getProperty("bernmobil.history.timeout-duration", Long.class));
        return new UpdateManager(mapperRepository, staticRepository, updateHistoryRepository, historySize, timeout);
    }

    @Bean(name = "MapperRepository")
    public UpdateManagerRepository mapperRepository(@Qualifier("MapperDataSource") DataSource mapperDataSource) {
        return new UpdateManagerRepository(new JdbcTemplate(mapperDataSource));
    }

    @Bean(name = "StaticRepository")
    public UpdateManagerRepository staticRepository(@Qualifier("StaticDataSource")DataSource staticDataSource) {
        return new UpdateManagerRepository(new JdbcTemplate(staticDataSource));
    }

    @Bean
    public UpdateHistoryRepository updateHistoryRepository(@Qualifier("StaticDataSource") DataSource dataSource) {
        return new UpdateHistoryRepository(dataSource);
    }

    private DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        return dataSourceInitializer(dataSource, new Resource[]{});
    }

    private DataSourceInitializer dataSourceInitializer(DataSource dataSource, Resource... sqlScripts) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        for(Resource resource : sqlScripts){
            databasePopulator.addScript(resource);
        }
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }

    private JobRepository getJobRepository(PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(sqliteDataSource());
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    private DataSource createDataSource(String driver, String url, String username, String password) {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName(driver);
        builder.url(url);
        builder.username(username);
        builder.password(password);
        return builder.build();
    }

    private DataSource createDataSource(String driver, String url) {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName(driver);
        builder.url(url);
        return builder.build();
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
