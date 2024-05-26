package demo.etl.controller;

import demo.etl.dto.req.EtlRequest;
import demo.etl.dto.resp.EtlResponse;
import demo.etl.dto.resp.GeneralResponse;
import demo.etl.dto.resp.WagerSummaryResponse;
import demo.etl.entity.output.WagerSummary;
import demo.etl.service.EtlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v2/etl")
@AllArgsConstructor
@Tag(name = "ETL", description = "Version 2 APIs for ETL transformation. Trigger ETL transform for daily wagers to wager summaries.")
public class EtlV2Controller {

    private final EtlService etlService;

    @Operation(summary = "Trigger ETL transform for all wagers to wager summaries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Trigger ETL transform for all wagers to wager summaries.
                    1. code=204, transform successfully.
                    2. code=500, internal server error.""")})
    @PostMapping("/trigger")
    public ResponseEntity<GeneralResponse> triggerEtlTransform(@RequestBody(required = false) EtlRequest request) {
        if(request != null && (request.getStartDate() == null || request.getEndDate() == null)) {
            return ResponseEntity.badRequest().body(new GeneralResponse("failed", "Invalid request parameters"));
        }
        log.info("Triggering V2 ETL transform for all wagers to wager summaries");
        try {
            List<WagerSummary> result = etlService.transformSummaryDTOToWagerSummaries(request);
            log.info("Triggered V2 ETL transform for all existing wagers");
            if(request == null || request.getImmediateReturn()){
                log.info("Immediate return after ETL process has been triggered");
                return ResponseEntity.accepted().body(new GeneralResponse("accepted", "ETL process has been triggered"));
            }
            List<WagerSummaryResponse> responses = result.stream().map(e ->
                            WagerSummaryResponse.builder()
                                    .id(e.getId())
                                    .accountId(e.getAccountId())
                                    .totalWagerAmount(e.getTotalWagerAmount())
                                    .wagerDate(e.getWagerDate()).build())
                    .collect(Collectors.toList());
            log.info("Collect all the wager summaries after ETL process has been triggered");
            return ResponseEntity.accepted().body(new EtlResponse("accepted", "ETL process has been triggered", responses));
        } catch (Exception e) {
            log.error("Failed to transform daily wagers to wager summaries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GeneralResponse("failed", "Failed to trigger ETL process"));
        }
    }
}
