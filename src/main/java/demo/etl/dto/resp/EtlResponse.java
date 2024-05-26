package demo.etl.dto.resp;

import java.util.List;

public class EtlResponse extends GeneralResponse{
    List<WagerSummaryResponse> wagerSummaries;
    public EtlResponse(String status, String message, List<WagerSummaryResponse> wagerSummaries) {
        super(status, message);
        this.wagerSummaries = wagerSummaries;
    }
}
