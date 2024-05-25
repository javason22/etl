package demo.etl.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(name = "WagerRequest", description = "Request object for wager")
public class WagerRequest {

    @Schema(description = "Wager ID")
    private String id;
    @Schema(description = "Account ID")
    private String accountId;
    @Schema(description = "Wager amount")
    private BigDecimal wagerAmount;
}
