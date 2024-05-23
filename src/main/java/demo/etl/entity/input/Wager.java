package demo.etl.entity.input;


import demo.etl.entity.InputType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "WAGER", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ACCOUNT_ID", "WAGER_TIMESTAMP"})
})
@Builder
public class Wager implements Serializable, InputType {

    private static final long serialVersionUID = 163905306L;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private String accountId;

    @Column(name = "WAGER_AMOUNT", nullable = false)
    private BigDecimal wagerAmount;

    @Column(name = "WAGER_TIMESTAMP", columnDefinition = "TIMESTAMP", nullable = false)
    private Date wagerTimestamp;

    @Tolerate
    public Wager() {

    }
}
