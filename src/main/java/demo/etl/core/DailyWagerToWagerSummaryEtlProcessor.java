package demo.etl.core;

import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class DailyWagerToWagerSummaryEtlProcessor extends EtlProcessor<Wager, WagerSummary, LocalDate>{

    public DailyWagerToWagerSummaryEtlProcessor(DailyWagerExtractor wagerExtractor,
                                                WagerSummaryTransformer dailyWagerSummaryTransformer,
                                                WagerSummaryLoader wagerSummaryLoader) {
        this.extractor = wagerExtractor;
        this.transformer = dailyWagerSummaryTransformer;
        this.loader = wagerSummaryLoader;
    }


}
