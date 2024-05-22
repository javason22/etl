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
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "demo.etl.repository.input",
        entityManagerFactoryRef = "inputEntityManagerFactory",
        transactionManagerRef = "inputTransactionManager")
public class InputJpaConfiguration {

    @Bean
    public LocalContainerEntityManagerFactoryBean inputEntityManagerFactory(
            @Qualifier("readDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("demo.etl.repository.input")
                .persistenceUnit("input")
                .build();
    }

    @Bean
    public PlatformTransactionManager inputTransactionManager(
            @Qualifier("inputEntityManagerFactory") LocalContainerEntityManagerFactoryBean inputEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(inputEntityManagerFactory.getObject()));
    }
}
