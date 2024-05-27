package demo.etl.config;

import demo.etl.repository.input.WagerRepository;
import demo.etl.repository.output.WagerSummaryRepository;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloomFilterConfig {

    /**
     * Only for testing.
     * To align bloomFilter with database records.
     *
     * @param redissonClient redisson client bean
     * @param wagerRepository wager repository bean
     * @param wagerSummaryRepository wager summary repository bean
     * @return application runner
     */
    @Bean
    public ApplicationRunner bloomFilterInitializer(RedissonClient redissonClient,
                                                    WagerRepository wagerRepository,
                                                    WagerSummaryRepository wagerSummaryRepository){
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                RBloomFilter<String> wagerBloomFilter = redissonClient.getBloomFilter("wager-bloom-filter");
                wagerRepository.findAll().forEach(wager -> wagerBloomFilter.add(wager.getId()));
                RBloomFilter<String> summaryBloomFilter = redissonClient.getBloomFilter("wager-summary-bloom-filter");
                wagerSummaryRepository.findAll().forEach(summary -> summaryBloomFilter.add(summary.getId()));
            }
        };
    }
}
