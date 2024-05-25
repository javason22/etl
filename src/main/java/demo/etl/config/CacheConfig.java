package demo.etl.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // set cache TTL to 5 minutes
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    // set bloom filter cache for wager query
    @Bean
    public RBloomFilter<String> wagerBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("wager-bloom-filter");
        // initialize bloom filter with 100000 expected insertions and 0.01 expected false positive probability
        bloomFilter.tryInit(100000, 0.01);
        return bloomFilter;
    }

    // set bloom filter cache for wager summary query
    @Bean
    public RBloomFilter<String> wagerSummaryBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("wager-summary-bloom-filter");
        // initialize bloom filter with 10000 expected insertions and 0.01 expected false positive probability
        bloomFilter.tryInit(10000, 0.01);
        return bloomFilter;
    }
}
