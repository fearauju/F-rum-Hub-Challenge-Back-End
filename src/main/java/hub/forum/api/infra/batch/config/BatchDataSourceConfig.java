package hub.forum.api.infra.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BatchDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }
}