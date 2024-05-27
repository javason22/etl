package demo.etl.service;

import demo.etl.core.processor.WagerToWagerSummaryEtlProcessor;
import demo.etl.core.processor.SummaryDTOToWagerSummaryEtlProcessor;
import demo.etl.dto.req.EtlRequest;
import demo.etl.entity.output.WagerSummary;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class EtlService {

    private static final int BATCH_SIZE = 2000;

    private static final String WAGER_SUMMARY_LOCK = "wager-summary-lock";

    private final WagerToWagerSummaryEtlProcessor wagerToWagerSummaryEtlProcessor;

    private final SummaryDTOToWagerSummaryEtlProcessor summaryDTOToWagerSummaryEtlProcessor;

    private final RedissonClient redissonClient;

    private final WagerSummaryRepository wagerSummaryRepository;


    //@Transactional(transactionManager = "outputTransactionManager", isolation = Isolation.SERIALIZABLE)
    @Caching(evict = {
            @CacheEvict(value = "wagerSummary", allEntries = true),
            @CacheEvict(value = "wagerSummaryList", allEntries = true)
    })
    public List<WagerSummary> transformWagersToWagerSummaries(EtlRequest request) {
        log.info("Transforming all wagers to wager summaries");

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        // if request is null, immediateReturn is true
        boolean immediateReturn = request == null || request.getImmediateReturn();
        List<CompletableFuture<List<WagerSummary>>> futures = null;
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear all existing wager summaries
            if(request == null) {
                wagerSummaryRepository.deleteAll();
            }else{
                List<WagerSummary> wagerSummaries = wagerSummaryRepository.findByWagerDateBetween(
                        LocalDate.parse(request.getStartDate()),
                        LocalDate.parse(request.getEndDate()));
                wagerSummaryRepository.deleteAll(wagerSummaries);
                wagerSummaryRepository.flush();
            }
            futures = wagerToWagerSummaryEtlProcessor.process(request, BATCH_SIZE);
            if(!immediateReturn && futures != null){
                log.info("Waiting for all futures to complete");
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
                List<CompletableFuture<List<WagerSummary>>> finalFutures = futures;
                CompletableFuture<List<WagerSummary>> allResultsFuture = allFutures.thenApply(v -> finalFutures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .toList());
                return allResultsFuture.join();
            }
            // return null if immediateReturn is true
            return null;
        } finally {
            // release the lock only when all futures are completed
            if(immediateReturn && futures != null) {
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }

    //@Transactional(transactionManager = "outputTransactionManager", isolation = Isolation.SERIALIZABLE)
    @Caching(evict = {
            @CacheEvict(value = "wagerSummary", allEntries = true),
            @CacheEvict(value = "wagerSummaryList", allEntries = true)
    })
    public List<WagerSummary> transformSummaryDTOToWagerSummaries(EtlRequest request){
        log.info("Transforming summary DTOs to wager summaries");

        RLock lock = redissonClient.getLock(WAGER_SUMMARY_LOCK);
        // if request is null, immediateReturn is true
        Boolean immediateReturn = request == null || request.getImmediateReturn();
        List<CompletableFuture<List<WagerSummary>>> futures = null;
        try {
            lock.lock();
            log.info("Acquired lock {}", WAGER_SUMMARY_LOCK);
            // critical section
            // clear all existing wager summaries
            if(request == null) {
                wagerSummaryRepository.deleteAll();
                wagerSummaryRepository.flush();
            } else {
                List<WagerSummary> wagerSummaries = wagerSummaryRepository.findByWagerDateBetween(
                        LocalDate.parse(request.getStartDate()),
                        LocalDate.parse(request.getEndDate()));
                wagerSummaryRepository.deleteAll(wagerSummaries);
            }
            futures = summaryDTOToWagerSummaryEtlProcessor.process(request, BATCH_SIZE);
            if(!immediateReturn && futures != null){
                log.info("Waiting for all futures to complete");
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
                List<CompletableFuture<List<WagerSummary>>> finalFutures = futures;
                CompletableFuture<List<WagerSummary>> allResultsFuture = allFutures.thenApply(v -> finalFutures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .toList());
                return allResultsFuture.join();
            }
            // return null if immediateReturn is true
            return null;
        } finally {
            // release the lock only when all futures are completed
            if(immediateReturn && futures != null) {
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
            lock.unlock();
            log.info("Released lock {}", WAGER_SUMMARY_LOCK);
        }
    }

}
