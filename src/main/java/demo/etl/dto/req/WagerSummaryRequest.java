package demo.etl.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(name = "WagerSummaryRequest", description = "Request object for wager summary")
public class WagerSummaryRequest {

    @Schema(description = "Account ID")
    @NotNull(message = "Account ID is mandatory")
    @Size(message = "Account ID length should be between 1 and 50 characters", min = 1, max = 50)
    private String accountId;

    @Schema(description = "Wager date")
    @NotNull(message = "Wager Date is mandatory")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate wagerDate;

    @Schema(description = "Total wager amount")
    @Positive(message = "Total Wager Amount needs to be a positive number")
    @Digits(integer = 10, fraction = 2, message = "Incorrect Total Wager Amount")
    @NotNull(message = "Total Wager Amount is mandatory")
    private BigDecimal totalWagerAmount;
}
