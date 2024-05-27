package demo.etl.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "EtlResponse", description = "Response object for ETL trigger")
public class EtlResponse extends GeneralResponse{
    @Schema(description = "List of wager summaries")
    List<WagerSummaryResponse> wagerSummaries;
    public EtlResponse(String status, String message, List<WagerSummaryResponse> wagerSummaries) {
        super(status, message);
        this.wagerSummaries = wagerSummaries;
    }
}
