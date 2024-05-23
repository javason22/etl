package demo.etl.service;

import demo.etl.core.DailyWagerSummaryTransformer;
import demo.etl.core.WagerExtractor;
import demo.etl.core.WagerSummaryLoader;
import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EtlService {


    private final WagerExtractor wagerExtractor;
    private final WagerSummaryLoader wagerSummaryLoader;
    private final DailyWagerSummaryTransformer dailyWagerSummaryTransformer;


    public void transformDailyWagerToWagerSummary(LocalDate currentDate) {
        log.info("Transforming wagers for date {} to wager summaries", currentDate);
        List<Wager> wagers = wagerExtractor.extract(currentDate);
        if(wagers.isEmpty()) {
            log.warn("No wagers found for date {}", currentDate);
            return;
        }
        List<WagerSummary> wagerSummaries = dailyWagerSummaryTransformer.transform(wagers, currentDate);
        if(wagerSummaries.isEmpty()){
            log.warn("No wager summaries transformed for date {}", currentDate);
            return;
        }
        wagerSummaryLoader.load(wagerSummaries, currentDate);
        log.info("Transformed {} wagers accounts", wagerSummaries.size());
    }
}
