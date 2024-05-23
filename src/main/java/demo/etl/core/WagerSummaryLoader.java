package demo.etl.core;

import demo.etl.entity.output.WagerSummary;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class WagerSummaryLoader implements Loader<WagerSummary, LocalDate>{

    private final WagerSummaryRepository wagerSummaryRepository;

    @Override
    public void load(List<WagerSummary> data) {
        wagerSummaryRepository.deleteAll();
        wagerSummaryRepository.saveAll(data);
    }

    @Override
    public void load(List<WagerSummary> data, LocalDate param) {
        wagerSummaryRepository.deleteByWagerDate(param);
        wagerSummaryRepository.saveAll(data);
    }


}
