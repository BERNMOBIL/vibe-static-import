package ch.bernmobil.vibe.staticdata;

import org.springframework.boot.context.properties.ConfigurationProperties;

public class BernmobilConfigurationProperties {

    @ConfigurationProperties(prefix = "bernmobil.jobrepository")
    public class JobRepository {

        /**
         * Value of the datasource of the repository where logs of Spring Batch will be stored
         */
        private String datasource;

        public String getDatasource() {
            return datasource;
        }

        public void setDatasource(String datasource) {
            this.datasource = datasource;
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
}
