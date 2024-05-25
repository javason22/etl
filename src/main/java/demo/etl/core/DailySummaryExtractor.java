package demo.etl.core;

import demo.etl.dto.SummaryDTO;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class DailySummaryExtractor implements Extractor<SummaryDTO, LocalDate>{

    private final WagerRepository wagerRepository;
    @Override
    public List<SummaryDTO> extract(LocalDate date) {
        return wagerRepository.findWagerSummaries(date);
    }
}
