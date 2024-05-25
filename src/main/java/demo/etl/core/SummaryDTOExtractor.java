package demo.etl.core;

import demo.etl.dto.SummaryDTO;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SummaryDTOExtractor implements Extractor<SummaryDTO, Pageable> {

    private final WagerRepository wagerRepository;
    @Override
    public List<SummaryDTO> extract(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        return wagerRepository.findAllWagerSummaries(pageable.getPageSize(), offset);
    }
}