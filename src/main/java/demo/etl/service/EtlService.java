package demo.etl.service;

import demo.etl.core.AllWagerToWagerSummaryEtlProcessor;
import demo.etl.core.DailyWagerToWagerSummaryEtlProcessor;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class EtlService {

    private static final int BATCH_SIZE = 10;

    private static final String WAGER_SUMMARY_LOCK = "wager-summary-lock";

    private final DailyWagerToWagerSummaryEtlProcessor dailyWagerToWagerSummaryEtlProcessor;

    private final AllWagerToWagerSummaryEtlProcessor allWagerToWagerSummaryEtlProcessor;

    private final RedissonClient redissonClient;

    private final WagerSummaryRepository wagerSummaryRepository;

    @Transactional
    public void transformDailyWagersToWagerSummaries(LocalDate currentDate) {
        log.info("Transforming wagers for date {} to wager summaries", currentDate);

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            // critical section
            // clear existing wager summaries for the date
            wagerSummaryRepository.deleteByWagerDate(currentDate);
            dailyWagerToWagerSummaryEtlProcessor.process(currentDate);
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void transformAllWagersToWagerSummaries() {
        log.info("Transforming all wagers to wager summaries");

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            // critical section
            // clear all existing wager summaries
            wagerSummaryRepository.deleteAll();
            allWagerToWagerSummaryEtlProcessor.process(PageRequest.of(0, BATCH_SIZE));
        } finally {
            lock.unlock();
        }
    }
}
