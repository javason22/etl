package demo.etl.service;

import demo.etl.core.DailySummaryDTOToWagerSummaryEtlProcessor;
import demo.etl.core.WagerToWagerSummaryEtlProcessor;
import demo.etl.core.DailyWagerToWagerSummaryEtlProcessor;
import demo.etl.core.SummaryDTOToWagerSummaryEtlProcessor;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class EtlService {

    private static final int BATCH_SIZE = 5;

    private static final String WAGER_SUMMARY_LOCK = "wager-summary-lock";

    private final DailyWagerToWagerSummaryEtlProcessor dailyWagerToWagerSummaryEtlProcessor;

    private final WagerToWagerSummaryEtlProcessor allWagerToWagerSummaryEtlProcessor;

    private final SummaryDTOToWagerSummaryEtlProcessor summaryDTOToWagerSummaryEtlProcessor;

    private final DailySummaryDTOToWagerSummaryEtlProcessor dailySummaryDTOToWagerSummaryEtlProcessor;

    private final RedissonClient redissonClient;

    private final WagerSummaryRepository wagerSummaryRepository;

    //@Transactional(transactionManager = "outputTransactionManager")
    public void transformDailyWagersToWagerSummaries(LocalDate currentDate) {
        log.info("Transforming wagers for date {} to wager summaries", currentDate);

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear existing wager summaries for the date
            wagerSummaryRepository.deleteByWagerDate(currentDate);
            dailyWagerToWagerSummaryEtlProcessor.process(currentDate);
        } finally {
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }

    //@Transactional(transactionManager = "outputTransactionManager")
    public void transformAllWagersToWagerSummaries() {
        log.info("Transforming all wagers to wager summaries");

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear all existing wager summaries
            wagerSummaryRepository.deleteAll();
            Sort sort = Sort.by(Sort.Order.asc("accountId"), Sort.Order.asc("wagerTimestamp"));
            allWagerToWagerSummaryEtlProcessor.process(PageRequest.of(0, BATCH_SIZE, sort));
        } finally {
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }

    //@Transactional(transactionManager = "outputTransactionManager")
    public void transformSummaryDTOToWagerSummaries() {
        log.info("Transforming summary DTOs to wager summaries");

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear all existing wager summaries
            wagerSummaryRepository.deleteAll();
            Sort sort = Sort.by(Sort.Order.asc("accountId"), Sort.Order.asc("wagerTimestamp"));
            summaryDTOToWagerSummaryEtlProcessor.process(PageRequest.of(0, BATCH_SIZE, sort));
        } finally {
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }

    public void transformDailySummaryDTOToWagerSummaries(LocalDate currentDate) {
        log.info("Transforming daily summary DTOs for date {} to wager summaries", currentDate);

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear existing wager summaries for the date
            wagerSummaryRepository.deleteByWagerDate(currentDate);
            dailySummaryDTOToWagerSummaryEtlProcessor.process(currentDate);
        } finally {
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }
}
