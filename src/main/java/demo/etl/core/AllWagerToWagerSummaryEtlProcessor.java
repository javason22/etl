package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AllWagerToWagerSummaryEtlProcessor extends EtlProcessor<Wager, WagerSummary, Pageable>{

    public AllWagerToWagerSummaryEtlProcessor(AllWagerExtractor allWagerExtractor,
                                              WagerSummaryTransformer allWagerSummaryTransformer,
                                              WagerSummaryLoader allWagerSummaryLoader) {
        this.extractor = allWagerExtractor;
        this.transformer = allWagerSummaryTransformer;
        this.loader = allWagerSummaryLoader;
    }

    @Override
    public void process(Pageable pageable) {
        log.info("Processing wagers for page: {}", pageable);
        List<Wager> lastBatchWagers = new ArrayList<>();
        List<CompletableFuture<List<WagerSummary>>> futures = new ArrayList<>();
        while (true) {
            List<Wager> wagers = extractor.extract(pageable);
            if (wagers.isEmpty()) { // if no more wagers to process
                if(!lastBatchWagers.isEmpty()){ // if there are still wagers in the last batch waiting to be processed
                    List<WagerSummary> wagerSummaries = transformer.transform(lastBatchWagers);
                    futures.add(loader.load(wagerSummaries));
                }
                break;
            }
            if (!lastBatchWagers.isEmpty()){
                wagers.addAll(0, lastBatchWagers);
                lastBatchWagers.clear();
            }
            String accountId = wagers.get(wagers.size() - 1).getAccountId();
            Date date = wagers.get(wagers.size() - 1).getWagerTimestamp();
            // for grouping by account id and trimmed wagerTimestamp,
            // we need to group the last wager of the previous batch with the first wager of the current batch
            wagers.stream()
                    .filter(wager -> wager.getAccountId().equals(accountId) && DateUtils.isSameDay(date, wager.getWagerTimestamp()))
                    .forEach(wager ->{
                        lastBatchWagers.add(wager);
                        //wagers.remove(wager);
                    });
            List<Wager> reducedWagers = wagers.stream().filter(wager -> !lastBatchWagers.contains(wager)).collect(Collectors.toList());
            List<WagerSummary> wagerSummaries = transformer.transform(reducedWagers);
            futures.add(loader.load(wagerSummaries));

            pageable = PageRequest.of(pageable.getPageNumber() + 1, pageable.getPageSize(), pageable.getSort());
        }
        //CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
    }
}
