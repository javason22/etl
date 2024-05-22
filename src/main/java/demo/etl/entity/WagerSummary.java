package demo.etl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "wager_summary")
@NoArgsConstructor
public class WagerSummary implements Serializable {

    private static final long serialVersionUID = 2359006129L;

    @Id
    private Long id;
    private String accountId;
    private Date wagerDate;
    private BigDecimal totalWagerAmount;

    /**
     * Constructor for WagerSummary without the ID
     *
     * @param accountId
     * @param wagerDate
     * @param totalWagerAmount
     */
    public WagerSummary(String accountId, Date wagerDate, BigDecimal totalWagerAmount) {
        this.accountId = accountId;
        this.wagerDate = wagerDate;
        this.totalWagerAmount = totalWagerAmount;
    }
}
