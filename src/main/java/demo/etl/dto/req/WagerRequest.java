package demo.etl.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Schema(name = "WagerRequest", description = "Request object for wager")
public class WagerRequest {

    @Schema(description = "Account ID")
    @Size(min = 1, max = 50, message = "Account ID length should be between 1 and 50 characters")
    @NotNull(message = "Account ID is mandatory")
    private String accountId;

    @Schema(description = "Wager amount")
    @Positive(message = "Wager Amount needs to be a positive number")
    @Digits(integer = 10, fraction = 2, message = "Incorrect Wager Amount")
    @NotNull(message = "Wager Amount is mandatory")
    private BigDecimal wagerAmount;

    @Schema(description = "Wager timestamp")
    @NotNull(message = "Wager Timestamp is mandatory")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date wagerTimestamp;
}
