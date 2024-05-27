package demo.etl.entity.output;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "WAGER_SUMMARY",
        uniqueConstraints = {
        @UniqueConstraint(name = "unique_wager_summary_account_id_wager_date",
                columnNames = {"ACCOUNT_ID", "WAGER_DATE"})
})
@Builder
public class WagerSummary implements Serializable {

    @Serial
    private static final long serialVersionUID = 2359006129L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private String accountId;

    @Column(name = "WAGER_DATE", columnDefinition = "DATE", nullable = false)
    private LocalDate wagerDate;

    @Column(name = "TOTAL_WAGER_AMOUNT", nullable = false)
    private BigDecimal totalWagerAmount;

    @Tolerate
    public WagerSummary() {

    }
}
