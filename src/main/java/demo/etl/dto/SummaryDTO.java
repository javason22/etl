package demo.etl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Data Transfer Object for Wager Summary
 */
@Slf4j
@Data
@AllArgsConstructor
public class SummaryDTO {
    private String accountId;
    private LocalDate wagerDate;
    private BigDecimal totalWagerAmount;

    public SummaryDTO(String accountId, Date wagerDate, BigDecimal totalWagerAmount) {
        this.accountId = accountId;
        this.wagerDate = wagerDate.toLocalDate();
        this.totalWagerAmount = totalWagerAmount;
    }
}
