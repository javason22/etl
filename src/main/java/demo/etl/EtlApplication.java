package demo.etl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication
public class EtlApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtlApplication.class, args);
    }

}
