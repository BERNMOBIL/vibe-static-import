package ch.bernmobil.vibe.staticdata;

import java.net.MalformedURLException;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:/application.properties")
public class SpringConfig {
    @Value("org/springframework/batch/core/schema-drop-sqlite.sql")
    private Resource dropRepositoryTables;

    @Value("org/springframework/batch/core/schema-sqlite.sql")
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
        return createDataSource("org.sqlite.JDBC",
                environment.getProperty("bernmobil.springconfig.jobrepository.datasource"));
    }

    @Bean("MapperDataSource")
    public DataSource mapperDataSource() {
        return createDataSource("org.postgresql.Driver",
                environment.getProperty("bernmobil.springconfig.mappingrepository.datasource"),
                environment.getProperty("bernmobil.springconfig.mappingrepository.username"),
                environment.getProperty("bernmobil.springconfig.mappingrepository.password"));
    }

    @Bean("PostgresDataSource")
    public DataSource postgresDataSource() {
        return createDataSource("org.postgresql.Driver",
                environment.getProperty("bernmobil.datasource.url"),
                environment.getProperty("bernmobil.datasource.username"),
                environment.getProperty("bernmobil.datasource.password"));
    }

    private DriverManagerDataSource createDataSource(String driver, String url, String username, String password) {
        DriverManagerDataSource dataSource = createDataSource(driver, url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private DriverManagerDataSource createDataSource(String driver, String url) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        return dataSource;
    }

    @Bean
    public BatchConfigurer configurer(DataSource dataSource){
        return new DefaultBatchConfigurer(dataSource);
    }

    @Bean("PostgresInitializer")
    public DataSourceInitializer postgresInitializer(@Qualifier("PostgresDataSource") DataSource dataSource) {
        return dataSourceInitializer(dataSource, truncatePostgres);
    }

    @Bean("MappingDatabaseInitializer")
    public DataSourceInitializer mappingSourceInitializer(@Qualifier("MapperDataSource") DataSource dataSource){
        return dataSourceInitializer(dataSource, mapperDropDatabase, mapperDatabaseSchema);
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

    @Bean("JobRepositoryInitializer")
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) throws MalformedURLException {
        return dataSourceInitializer(dataSource, dropRepositoryTables, dataRepositorySchema);
    }

    private JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(sqliteDataSource());
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    private PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean("jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
