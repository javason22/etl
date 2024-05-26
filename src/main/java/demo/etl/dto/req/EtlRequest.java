package demo.etl.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "EtlRequest", description = "Request object for ETL trigger")
public class EtlRequest {

    @Schema(description = "Start date", example = "2021-01-01")
    private String startDate;
    @Schema(description = "End date", example = "2021-01-31")
    private String endDate;
    @Schema(description = "Immediate return after triggering ETL process", example = "true")
    private Boolean immediateReturn;
}
