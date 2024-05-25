package demo.etl.core;

import demo.etl.dto.SummaryDTO;
import demo.etl.entity.output.WagerSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class SummaryDTOToWagerSummaryEtlProcessor extends EtlProcessor<SummaryDTO, WagerSummary, Pageable>{

    public SummaryDTOToWagerSummaryEtlProcessor(SummaryDTOExtractor summaryDTOExtractor,
                                                SummaryDTOTransformer summaryDTOTransformer,
                                                WagerSummaryLoader wagerSummaryLoader) {
        this.extractor = summaryDTOExtractor;
        this.transformer = summaryDTOTransformer;
        this.loader = wagerSummaryLoader;
    }

    @Override
    public void process(Pageable pageable) {
        log.info("Processing wagers for page: {}", pageable);
        while (true) {
            List<SummaryDTO> summaryDTOs = extractor.extract(pageable);
            if (summaryDTOs.isEmpty()) {
                break;
            }
            List<WagerSummary> wagerSummaries = transformer.transform(summaryDTOs);
            loader.load(wagerSummaries);
            if (summaryDTOs.size() < pageable.getPageSize()) {
                break;
            }
            pageable = pageable.next();
        }
    }
}
