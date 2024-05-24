package demo.etl.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(name = "WagerSummaryResponse", description = "Response object for wager summary")
public class WagerSummaryResponse {
    @Schema(description = "Wager summary ID")
    private String id;
    @Schema(description = "Account ID")
    private String accountId;
    @Schema(description = "Wager date")
    private LocalDate wagerDate;
    @Schema(description = "Total wager amount")
    private BigDecimal totalWagerAmount;
}
