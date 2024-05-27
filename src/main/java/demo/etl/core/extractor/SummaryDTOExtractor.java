package demo.etl.core.extractor;

import demo.etl.dto.SummaryDTO;
import demo.etl.dto.req.EtlRequest;
import demo.etl.repository.input.WagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SummaryDTOExtractor implements Extractor<SummaryDTO, EtlRequest> {

    private final WagerRepository wagerRepository;
    @Override
    public List<SummaryDTO> extract(EtlRequest request, int page, int size) {
        Sort sort = Sort.by(Sort.Order.asc("wagerTimestamp"), Sort.Order.asc("accountId"));
        Pageable pageable = PageRequest.of(page, size, sort);
        // if request is null, return all wagers with pagination
        if(request == null){
            return new ArrayList<>(wagerRepository.findAllWagerSummaries(pageable).getContent());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDate.parse(request.getStartDate(), formatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(request.getEndDate(), formatter).atTime(23, 59, 59, 999999999);
        // convert unmodifiable Page to List
        return new ArrayList<>(wagerRepository.findAllWagerSummaries(start, end, pageable).getContent());
    }
}