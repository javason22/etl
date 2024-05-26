package demo.etl.entity.input;


import demo.etl.entity.InputType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.apache.commons.lang3.time.DateUtils;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "WAGER", uniqueConstraints = {
        @UniqueConstraint(name = "unique_wager_account_id_timestamp",
                columnNames = {"ACCOUNT_ID", "WAGER_TIMESTAMP"})
})
@Builder
public class Wager implements Serializable, InputType<Wager>{

    private static final long serialVersionUID = 163905306L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private String accountId;

    @Column(name = "WAGER_AMOUNT", nullable = false)
    private BigDecimal wagerAmount;

    @Column(name = "WAGER_TIMESTAMP", columnDefinition = "TIMESTAMP", nullable = false)
    private Date wagerTimestamp;

    @Tolerate
    public Wager() {

    }

    @Override
    public boolean sameGroupToTransform(Wager comparedTo) {
        // same group if same account id and same day of wager timestamp
        return comparedTo.getAccountId().equals(accountId) && DateUtils.isSameDay(wagerTimestamp, comparedTo.getWagerTimestamp());
    }


}
