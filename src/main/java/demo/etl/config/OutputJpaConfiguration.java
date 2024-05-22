package demo.etl.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "demo.etl.repository.output",
        entityManagerFactoryRef = "outputEntityManagerFactory",
        transactionManagerRef = "outputTransactionManager")
public class OutputJpaConfiguration {

    @Bean
    public LocalContainerEntityManagerFactoryBean outputEntityManagerFactory(
            @Qualifier("writeDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("demo.etl.repository.output")
                .persistenceUnit("output")
                .build();
    }

    @Bean
    public PlatformTransactionManager outputTransactionManager(
            @Qualifier("outputEntityManagerFactory") LocalContainerEntityManagerFactoryBean outputEntityManagerFactory) {
        return new JpaTransactionManager(outputEntityManagerFactory.getObject());
    }
}
