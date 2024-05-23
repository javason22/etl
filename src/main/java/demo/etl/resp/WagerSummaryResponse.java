package demo.etl.resp;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class WagerSummaryResponse {
    private Long id;
    private String accountId;
    private LocalDate wagerDate;
    private BigDecimal totalWagerAmount;
}
