package demo.etl.controller;

import demo.etl.dto.req.EtlRequest;
import demo.etl.dto.resp.GeneralResponse;
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
            etlService.transformSummaryDTOToWagerSummaries(request);
            return ResponseEntity.accepted().body(new GeneralResponse("accepted", "ETL process has been triggered"));
        } catch (Exception e) {
            log.error("Failed to transform daily wagers to wager summaries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GeneralResponse("failed", "Failed to trigger ETL process"));
        }
    }
}
