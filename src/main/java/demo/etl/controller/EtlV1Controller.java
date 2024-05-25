package demo.etl.controller;

import demo.etl.dto.req.EtlRequest;
import demo.etl.dto.resp.EtlResponse;
import demo.etl.service.EtlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/etl")
@Slf4j
@Tag(name = "ETL", description = "Version 1 APIs for ETL transformation. Trigger ETL transform for daily wagers to wager summaries.")
public class EtlV1Controller {

    private final EtlService etlService;

    @Operation(summary = "Trigger V1 ETL transform for all existing wagers to wager summaries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Trigger ETL transform for all existing wagers to wager summaries.
                    1. code=204, transform successfully.
                    2. code=500, internal server error.""")})
    @PostMapping("/trigger")
    public ResponseEntity<EtlResponse> triggerEtlTransform(@RequestBody(required = false) EtlRequest request) {
        if(request != null && (request.getStartDate() == null || request.getEndDate() == null)) {
            return ResponseEntity.badRequest().body(new EtlResponse("failed", "Invalid request parameters"));
        }
        log.info("Triggering ETL transform for all existing wagers");
        try{
            etlService.transformWagersToWagerSummaries(request);
            return ResponseEntity.accepted().body(new EtlResponse("accepted", "ETL process has been triggered"));
        } catch (Exception e) {
            log.error("Failed to transform daily wagers to wager summaries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new EtlResponse("failed", "Failed to trigger ETL process"));
        }
    }
}
