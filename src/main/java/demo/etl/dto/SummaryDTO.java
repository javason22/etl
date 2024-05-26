package demo.etl.dto;

import demo.etl.entity.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
public class SummaryDTO implements InputType<SummaryDTO> {
    private String accountId;
    private LocalDate wagerDate;
    private BigDecimal totalWagerAmount;

    public SummaryDTO(String accountId, Date wagerDate, BigDecimal totalWagerAmount) {
        this.accountId = accountId;
        this.wagerDate = wagerDate.toLocalDate();
        this.totalWagerAmount = totalWagerAmount;
    }

    @Override
    public boolean sameGroupToTransform(SummaryDTO comparedTo) {
        // no grouping for summary DTO
        return false;
    }
}
