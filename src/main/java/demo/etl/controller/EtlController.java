package demo.etl.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/etl")
@Slf4j
@Tag(name = "ETL", description = "APIs for ETL transformation. Trigger ETL transform for daily wagers to wager summaries.")
public class EtlController {

    private final EtlService etlService;

    @Operation(summary = "Trigger ETL transform for daily wagers to wager summaries")
    @Parameters({@Parameter(name = "date", description = "Date of the wagers (yyyy-MM-dd)", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Trigger ETL transform for daily wagers to wager summaries.
                    1. code=204, transform successfully.
                    2. code=400, parameters invalid.
                    3. code=500, internal server error.""")})
    @PostMapping("/transformation/daily")
    public ResponseEntity<Void> triggerEtlTransform(@RequestParam String date) {
        log.info("Triggering ETL transform for date={}", date);
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateObj = LocalDate.parse(date, formatter);
            etlService.transformDailyWagersToWagerSummaries(dateObj);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to transform daily wagers to wager summaries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Trigger ETL transform for all existing wagers to wager summaries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Trigger ETL transform for all existing wagers to wager summaries.
                    1. code=204, transform successfully.
                    2. code=500, internal server error.""")})
    @PostMapping("/transformation/all")
    public ResponseEntity<Void> triggerEtlTransform() {
        log.info("Triggering ETL transform for all existing wagers");
        try{
            etlService.transformAllWagersToWagerSummaries();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to transform daily wagers to wager summaries", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
