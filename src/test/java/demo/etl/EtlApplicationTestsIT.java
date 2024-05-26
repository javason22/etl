package demo.etl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * For Integration Test only
 */
@SpringBootTest
class EtlApplicationTestsIT {

    // disable it so it will not be tested when mvn package
    @Disabled
    @Test
    void contextLoads() {
    }

}
