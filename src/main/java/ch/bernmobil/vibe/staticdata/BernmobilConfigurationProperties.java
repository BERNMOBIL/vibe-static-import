package ch.bernmobil.vibe.staticdata;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * This class helps the Configuration-Processor of Spring resolving the types of configuration properties.
 */
public class BernmobilConfigurationProperties {

    @ConfigurationProperties(prefix = "bernmobil.jobrepository")
    public class JobRepository {

        /**
         * Value of the datasource of the repository where logs of Spring Batch will be stored
         */
        private String datasource;

        /**
         * Classname of the database driver used for the mapping repository
         */
        private String driverClassName;
        /**
         * Filename of the sqlite file
         */
        private String name;

        public String getDatasource() {
            return datasource;
        }

        public void setDatasource(String datasource) {
            this.datasource = datasource;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.staticsource")
    public class StaticSource {

        /**
         * Url of the source for the static data
         */
        private String url;
        /**
         * Folder where the the static data could be saved prior to the import
         */
        private String folder;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFolder() {
            return folder;
        }

        public void setFolder(String folder) {
            this.folder = folder;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.mappingrepository.datasource")
    public class MappingRepository {

        /**
         * Url of the datasource where the mapping data should be stored
         */
        private String url;
        /**
         * Username for the mapping datasource
         */
        private String username;
        /**
         * Password for the mapping datasource
         */
        private String password;



        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    @ConfigurationProperties(prefix = "bernmobil.amqp")
    public class Amqp {

        /**
         * Name of the fanout where update notifications will be sent
         */
        private String fanoutQueue;

        public String getFanoutQueue() {
            return fanoutQueue;
        }

        public void setFanoutQueue(String fanoutQueue) {
            this.fanoutQueue = fanoutQueue;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.history")
    public class History {
        /**
         * Defines how many different import versions are stored in the database
         */
        private int size;
        /**
         * Defines how long the realtime service in minutes
         */
        private long timeoutDuration;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTimeoutDuration() {
            return timeoutDuration;
        }

        public void setTimeoutDuration(long timeoutDuration) {
            this.timeoutDuration = timeoutDuration;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.batch")
    public class Batch {
        /**
         * Defines the size of chunks used in Spring Batch
         * A chunk determines the amount of data which is processed at once
         */
        private int chunkSize;

        /**
         * A cron based expression to schedule the execution of the import job.
         * The expression "0 0 2 1/1 * ?" starts the job every night at 2 AM.
         */
        private String schedule;

        public int getChunkSize() {
            return chunkSize;
        }

        public void setChunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }
    }
}
