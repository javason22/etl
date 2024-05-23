package demo.etl.resp;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class WagerResponse {
    private Long id;
    private String accountId;
    private BigDecimal wagerAmount;
    private Date wagerTimestamp;
}
