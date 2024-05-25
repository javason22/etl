package demo.etl.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(name = "WagerSummaryRequest", description = "Request object for wager summary")
public class WagerSummaryRequest {

    @Schema(description = "Wager summary ID")
    private String id;
    @Schema(description = "Account ID")
    private String accountId;
    @Schema(description = "Wager date")
    private LocalDate wagerDate;
    @Schema(description = "Total wager amount")
    private BigDecimal totalWagerAmount;
}
