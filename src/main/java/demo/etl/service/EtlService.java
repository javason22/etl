package demo.etl.service;

import demo.etl.core.processor.WagerToWagerSummaryEtlProcessor;
import demo.etl.core.processor.SummaryDTOToWagerSummaryEtlProcessor;
import demo.etl.dto.req.EtlRequest;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class EtlService {

    private static final int BATCH_SIZE = 5;

    private static final String WAGER_SUMMARY_LOCK = "wager-summary-lock";

    private final WagerToWagerSummaryEtlProcessor wagerToWagerSummaryEtlProcessor;

    private final SummaryDTOToWagerSummaryEtlProcessor summaryDTOToWagerSummaryEtlProcessor;

    private final RedissonClient redissonClient;

    private final WagerSummaryRepository wagerSummaryRepository;


    //@Transactional(transactionManager = "outputTransactionManager")
    public void transformWagersToWagerSummaries(EtlRequest request) {
        log.info("Transforming all wagers to wager summaries");

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear all existing wager summaries
            if(request == null) {
                wagerSummaryRepository.deleteAll();
            }else{
                wagerSummaryRepository.deleteByWagerDateBetween(
                        LocalDate.parse(request.getStartDate()),
                        LocalDate.parse(request.getEndDate()));
            }
            wagerToWagerSummaryEtlProcessor.process(request, BATCH_SIZE);
        } finally {
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }

    //@Transactional(transactionManager = "outputTransactionManager")
    public void transformSummaryDTOToWagerSummaries(EtlRequest request){
        log.info("Transforming summary DTOs to wager summaries");

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear all existing wager summaries
            if(request == null) {
                wagerSummaryRepository.deleteAll();
            } else {
                wagerSummaryRepository.deleteByWagerDateBetween(
                        LocalDate.parse(request.getStartDate()),
                        LocalDate.parse(request.getEndDate()));
            }
            summaryDTOToWagerSummaryEtlProcessor.process(request, BATCH_SIZE);
        } finally {
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }

}
