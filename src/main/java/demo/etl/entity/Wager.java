package demo.etl.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "wager")
public class Wager implements Serializable {

    private static final long serialVersionUID = 163905306L;

    @Id
    private Long id;
    private String accountId;
    private BigDecimal wagerAmount;
    private Date wagerTimestamp;

    @Transient
    private BigDecimal totalWagerAmount; // temporary storage of total wager amount for the account
}
