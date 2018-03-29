package ch.bernmobil.vibe.staticdata.configuration;

import ch.bernmobil.vibe.shared.UpdateHistoryRepository;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.UpdateManagerRepository;
import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
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
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Main configuration class for any Spring related objects.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Configuration
@EnableBatchProcessing
public class SpringConfig {
    private final Environment environment;

    @Value("classpath:/org/springframework/batch/core/schema-sqlite.sql")
    private Resource dataRepositorySchema;


    @Autowired
    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * Create a {@link DataSource} for the Spring Batch Job-Repository, where it stores all its jobs and logs.
     *
     * @see <a href=http://docs.spring.io/spring-batch/reference/html/></a>
     *
     * @return {@link DataSource} for a job repository.
     */
    @Primary
    @Bean
    public DataSource sqliteDataSource() {
        String url = String.format("%s:%s",
                environment.getProperty("bernmobil.jobrepository.datasource"),
                environment.getProperty("bernmobil.jobrepository.name"));
        return createDataSource(environment.getProperty("bernmobil.jobrepository.driver-class-name"), url);
    }

    /**
     * Create a {@link DataSource} for the Mapping Database.
     * @return {@link DataSource} for the mappings.
     */
    @Bean(name = "MapperDataSource")
    public DataSource mapperDataSource() {
        return createDataSource(environment.getProperty("spring.datasource.driver-class-name"),
                environment.getProperty("bernmobil.mappingrepository.datasource.url"),
                environment.getProperty("bernmobil.mappingrepository.datasource.username"),
                environment.getProperty("bernmobil.mappingrepository.datasource.password"));
    }

    /**
     * Create a {@link DataSource} for the static schedule database.
     * @return {@link DataSource}
     */
    @Bean(name = "StaticDataSource")
    public DataSource staticDataSource() {
        return createDataSource(environment.getProperty("spring.datasource.driver-class-name"),
                environment.getProperty("spring.datasource.url"),
                environment.getProperty("spring.datasource.username"),
                environment.getProperty("spring.datasource.password"));
    }

    /**
     * Creates a {@link DataSourceInitializer} for the job repository. Because it is designed for SQLite it checks
     * whether the database file exists. If not, it creates a new one and applies the pre-defined database schema to it.
     * @param dataSource to which the {@link DataSourceInitializer} is applied.
     * @return {@link DataSourceInitializer} for the given {@link DataSource}
     */
    @Bean(name = "JobRepositoryInitializer")
    public DataSourceInitializer jobRepositoryInitializer(DataSource dataSource) {
        if (Files.exists(Paths.get(environment.getProperty("bernmobil.jobrepository.name")))) {
            return dataSourceInitializer(dataSource);
        }
        return dataSourceInitializer(dataSource, dataRepositorySchema);
    }

    /**
     * Build a {@link JobLauncher} by using a {@link PlatformTransactionManager} to connect Spring Batch to its
     * {@link DataSource}.
     * @param transactionManager to manage the connection to the job repository
     * @param sqliteDataSource to save job information
     * @return {@link JobLauncher} which is used to start jobs and steps.
     * @throws Exception will be thrown if there is an {@link Exception} while creating.
     */
    @Bean("jobLauncher")
    public JobLauncher jobLauncher(PlatformTransactionManager transactionManager, DataSource sqliteDataSource) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository(transactionManager, sqliteDataSource));
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    /**
     * Build an {@link UpdateManager} with its dependent repositories and an {@link UpdateTimestampManager}.
     * @param mapperRepository which holds a connection to the mapping database.
     * @param staticRepository which holds a connection to the schedule database.
     * @param updateHistoryRepository which manages operations on the update history table.
     * @param updateTimestampManager to share the current timestamp in the application.
     * @return {@link UpdateManager}.
     */
    @Bean
    public UpdateManager updateManager(@Qualifier("MapperRepository") UpdateManagerRepository mapperRepository,
                                       @Qualifier("StaticRepository") UpdateManagerRepository staticRepository,
                                       UpdateHistoryRepository updateHistoryRepository,
                                       UpdateTimestampManager updateTimestampManager) {
        int historySize = environment.getProperty("bernmobil.history.size", Integer.class);
        Duration timeout = Duration.ofMinutes(environment.getProperty("bernmobil.history.timeout-duration", Long.class));
        return new UpdateManager(mapperRepository, staticRepository, updateHistoryRepository, historySize, timeout, updateTimestampManager);
    }

    /**
     * Build an {@link UpdateManagerRepository} which manages versioning on all tables of the mapping database.
     * @param dslContext to access the database.
     * @return {@link UpdateManagerRepository} to manage versioning.
     */
    @Bean(name = "MapperRepository")
    public UpdateManagerRepository mapperRepository(@Qualifier("MapperDslContext") DSLContext dslContext) {
        return new UpdateManagerRepository(dslContext);
    }

    /**
     * Build an {@link UpdateManagerRepository} which manages versioning on all tables of the schedule database.
     * @param dslContext to access the database.
     * @return {@link UpdateManagerRepository} to manage versioning.
     */
    @Bean(name = "StaticRepository")
    public UpdateManagerRepository staticRepository(@Qualifier("StaticDslContext") DSLContext dslContext) {
        return new UpdateManagerRepository(dslContext);
    }

    /**
     * Build an {@link UpdateHistoryRepository} which manages access to the update history.
     * @param dslContext to access the database.
     * @return {@link UpdateHistoryRepository} to manage update history.
     */
    @Bean
    public UpdateHistoryRepository updateHistoryRepository(@Qualifier("StaticDslContext") DSLContext dslContext) {
        return new UpdateHistoryRepository(dslContext);
    }

    /**
     * Build a  {@link DSLContext} to access the schedule database.
     * @param dataSource to access database.
     * @return {@link DSLContext}.
     */
    @Bean(name = "StaticDslContext")
    public DSLContext staticDslContext(@Qualifier("StaticDataSource") DataSource dataSource) {
        return getDslContext(dataSource);
    }


    /**
     * Build a {@link DSLContext} to access mapping database.
     * @param dataSource to access database.
     * @return {@link DSLContext}
     */
    @Bean(name = "MapperDslContext")
    public DSLContext mapperDslContext(@Qualifier("MapperDataSource") DataSource dataSource) {
        return getDslContext(dataSource);
    }

    /**
     * Build an {@link UpdateTimestampManager} to manage the active version timestamp.
     * @return {@link UpdateTimestampManager}.
     */
    @Bean
    public UpdateTimestampManager updateTimestampManager() {
        return new UpdateTimestampManager();
    }

    /**
     * Build a {@link DSLContext} with the SQL Dialect from spring.jooq.sql-dialect.
     * @param dataSource to which the {@link DSLContext} will be connected.
     * @return A configured {@link DSLContext}.
     */
    private DSLContext getDslContext(DataSource dataSource) {
        String dialectString = environment.getProperty("spring.jooq.sql-dialect").toUpperCase();
        SQLDialect dialect = SQLDialect.valueOf(dialectString);
        return DSL.using(dataSource, dialect);
    }

    /**
     * Build a {@link DataSourceInitializer} with no SQL scripts to execute on it.
     * @param dataSource to which the {@link DataSourceInitializer} is linked
     * @return {@link DataSourceInitializer}.
     */
    private DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        return dataSourceInitializer(dataSource, new Resource[]{});
    }

    /**
     * Build a {@link DataSourceInitializer} and add a set of SQL scripts wrapped in {@link Resource} to be executed
     * when the {@link DataSource} is created.
     * @param dataSource to which the {@link Resource}s are applied.
     * @param sqlScripts which will be applied.
     * @return {@link DataSourceInitializer} which will be initialized.
     */
    private DataSourceInitializer dataSourceInitializer(DataSource dataSource, Resource... sqlScripts) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        for (Resource resource : sqlScripts) {
            databasePopulator.addScript(resource);
        }
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }

    /**
     * Build a {@link JobRepository} using a given {@link PlatformTransactionManager} and {@link DataSource}.
     * @param transactionManager which is used to serialize access to the {@link DataSource}
     * @param dataSource which holds a connection to the job database.
     * @return {@link JobRepository} to save job information and logs.
     * @throws Exception if there is any while building the repository.
     */
    private JobRepository getJobRepository(PlatformTransactionManager transactionManager, DataSource dataSource) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * Create a {@link DataSource} using the given parameter for configuring the database connection.
     * @param driver which is used by the database. Must be a full qualified class name so JDBC can resolve it.
     * @param url where the database is reachable.
     * @param username of the database.
     * @param password of the database.
     * @return A configured {@link DataSource}.
     *
     * @see DataSourceBuilder
     */
    private DataSource createDataSource(String driver, String url, String username, String password) {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName(driver);
        builder.url(url);
        builder.username(username);
        builder.password(password);
        return builder.build();
    }

    /**
     * Create a {@link DataSource} using the given parameter for configuring the database connection.
     * @param driver which is used by the database. Must be a full qualified class name so JDBC can resolve it.
     * @param url where the database is reachable.
     * @return A configured {@link DataSource}.
     */
    private DataSource createDataSource(String driver, String url) {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName(driver);
        builder.url(url);
        return builder.build();
    }
}
