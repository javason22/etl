package demo.etl.service;

import demo.etl.entity.Wager;
import demo.etl.entity.WagerSummary;
import demo.etl.repository.input.WagerRepository;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class EtlService {

    private static final int DEFAULT_PAGE_SIZE = 1000;

    private final WagerRepository wagerRepository;
    private final WagerSummaryRepository wagerSummaryRepository;

    @Transactional(rollbackFor = Exception.class)
    public void transformWagers(Date currentDate) {
        log.info("Transforming wagers for date {} to wager summaries", currentDate);
        int page = 0;
        Page<WagerSummary> wagerSummaries;
        do{
            Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.DEFAULT_DIRECTION);
            // Get all wagers for the current date
            wagerSummaries = wagerRepository.findWagerSummaries(currentDate, pageable);
            // Process the wager summary
            wagerSummaryRepository.saveAll(wagerSummaries.getContent());
            page++;
            log.info("Transformed {} wagers accounts", wagerSummaries.getContent().size());
        } while(wagerSummaries.hasNext());
        log.info("Transformation completed");
    }
}
