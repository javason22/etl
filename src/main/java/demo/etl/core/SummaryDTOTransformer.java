package demo.etl.core;

import demo.etl.dto.SummaryDTO;
import demo.etl.entity.output.WagerSummary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Transform WagerSummaryDTO to WagerSummary
 */
@Component
public class SummaryDTOTransformer implements Transformer<SummaryDTO, WagerSummary>{
    @Override
    public List<WagerSummary> transform(List<SummaryDTO> data) {
        return data.stream().map(dto ->
            WagerSummary.builder()
                    .accountId(dto.getAccountId())
                    .totalWagerAmount(dto.getTotalWagerAmount())
                    .wagerDate(dto.getWagerDate())
                    .build()
        ).toList();
    }
}
