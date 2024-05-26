package demo.etl.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Schema(name = "WagerRequest", description = "Request object for wager")
public class WagerRequest {

    @Schema(description = "Account ID")
    @Size(min = 1, message = "Account ID cannot be empty")
    @NotNull(message = "Account ID is mandatory")
    private String accountId;

    @Schema(description = "Wager amount")
    @Positive(message = "Wager Amount needs to be a positive number")
    @Digits(integer = 10, fraction = 2, message = "Incorrect Wager Amount")
    @NotNull(message = "Wager Amount is mandatory")
    private BigDecimal wagerAmount;

    @Schema(description = "Wager timestamp")
    @NotNull(message = "Wager Timestamp is mandatory")
    private Date wagerTimestamp;
}
