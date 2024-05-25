package demo.etl.controller;

import demo.etl.service.EtlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @PostMapping("/transformation")
    public ResponseEntity<Void> triggerEtlTransform() {
        log.info("Triggering V2 ETL transform for all wagers to wager summaries");
        etlService.transformSummaryDTOToWagerSummaries();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Trigger V2 ETL transform for daily wagers to wager summaries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Trigger ETL transform for daily wagers to wager summaries.
                    1. code=204, transform successfully.
                    2. code=500, internal server error.""")})
    @PostMapping("/transformation/{date}")
    public ResponseEntity<Void> triggerDailyEtlTransform(@PathVariable String date) {
        log.info("Triggering V2 ETL transform for daily wagers to wager summaries");
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateObj = LocalDate.parse(date, formatter);
            etlService.transformDailySummaryDTOToWagerSummaries(dateObj);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to transform daily wagers to wager summaries", e);
            return ResponseEntity.status(500).build();
        }
    }
}
