package demo.etl.core.processor;

import demo.etl.core.transformer.SummaryDTOTransformer;
import demo.etl.core.loader.WagerSummaryLoader;
import demo.etl.core.extractor.SummaryDTOExtractor;
import demo.etl.dto.SummaryDTO;
import demo.etl.dto.req.EtlRequest;
import demo.etl.entity.output.WagerSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SummaryDTOToWagerSummaryEtlProcessor extends EtlProcessor<SummaryDTO, WagerSummary, EtlRequest> {

    public SummaryDTOToWagerSummaryEtlProcessor(SummaryDTOExtractor summaryDTOExtractor,
                                                SummaryDTOTransformer summaryDTOTransformer,
                                                WagerSummaryLoader wagerSummaryLoader) {
        this.extractor = summaryDTOExtractor;
        this.transformer = summaryDTOTransformer;
        this.loader = wagerSummaryLoader;
    }

    /*@Override
    public void process(Pageable pageable) {
        log.info("Processing wagers for page: {}", pageable);
        List<CompletableFuture<List<WagerSummary>>> futures = new ArrayList<>();
        while (true) {
            List<SummaryDTO> summaryDTOs = extractor.extract(pageable);
            if (summaryDTOs.isEmpty()) {
                break;
            }
            List<WagerSummary> wagerSummaries = transformer.transform(summaryDTOs);
            futures.add(loader.load(wagerSummaries));
            if (summaryDTOs.size() < pageable.getPageSize()) {
                break;
            }
            pageable = pageable.next();
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
    }*/
}
