package demo.etl.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MultipleDatasourceConfig {

    @Bean
    @ConfigurationProperties("app.datasource.input")
    public DataSourceProperties inputDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.output")
    public DataSourceProperties outputDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource readDataSource() {
        return inputDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource writeDataSource() {
        return outputDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
