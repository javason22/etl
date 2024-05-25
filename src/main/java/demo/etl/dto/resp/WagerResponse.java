package demo.etl.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Schema(name = "WagerResponse", description = "Response object for wager")
public class WagerResponse {
    @Schema(description = "Wager ID")
    private String id;
    @Schema(description = "Account ID")
    private String accountId;
    @Schema(description = "Wager amount")
    private BigDecimal wagerAmount;
    @Schema(description = "Wager timestamp")
    private Date wagerTimestamp;
}
