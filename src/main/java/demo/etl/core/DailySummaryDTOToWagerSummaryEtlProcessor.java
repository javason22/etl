package demo.etl.core;

import demo.etl.dto.SummaryDTO;
import demo.etl.entity.output.WagerSummary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DailySummaryDTOToWagerSummaryEtlProcessor extends EtlProcessor<SummaryDTO, WagerSummary, LocalDate>{
    public DailySummaryDTOToWagerSummaryEtlProcessor(DailySummaryExtractor dailySummaryExtractor,
                                                     SummaryDTOTransformer wagerSummaryTransformer,
                                                     WagerSummaryLoader wagerSummaryLoader) {
        this.extractor = dailySummaryExtractor;
        this.transformer = wagerSummaryTransformer;
        this.loader = wagerSummaryLoader;
    }
}
