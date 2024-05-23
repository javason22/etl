package demo.etl.controller;

import demo.etl.entity.output.WagerSummary;
import demo.etl.resp.WagerSummaryResponse;
import demo.etl.service.WagerSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/wager-summary")
public class WagerSummaryController {

    private final WagerSummaryService wagerSummaryService;

    @Operation(summary = "List all wager summaries with pagination")
    @Parameters({@Parameter(name = "example", description = "Wager summary example", required = true),
            @Parameter(name = "page", description = "Page number", required = true),
            @Parameter(name = "size", description = "Page size", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    List of wager summaries.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @GetMapping("/list")
    public ResponseEntity<Page<WagerSummaryResponse>> list(WagerSummary example, int page, int size){
        Page<WagerSummary> result = wagerSummaryService.page(example, page, size);
        return ResponseEntity.ok(result.map(wagerSummary -> WagerSummaryResponse.builder()
                .id(wagerSummary.getId())
                .totalWagerAmount(wagerSummary.getTotalWagerAmount())
                .wagerDate(wagerSummary.getWagerDate())
                .accountId(wagerSummary.getAccountId()).build()));
    }

    @Operation(summary = "Add wager summary")
    @Parameters({
            @Parameter(name = "wagerSummary", description = "Wager summary object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Add wager summary.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @PutMapping("/add")
    public ResponseEntity<WagerSummaryResponse> add(WagerSummary wagerSummary){
        wagerSummary = wagerSummaryService.add(wagerSummary);
        return ResponseEntity.ok(WagerSummaryResponse.builder()
                .id(wagerSummary.getId())
                .totalWagerAmount(wagerSummary.getTotalWagerAmount())
                .wagerDate(wagerSummary.getWagerDate())
                .accountId(wagerSummary.getAccountId()).build());
    }

    @Operation(summary = "Update wager summary")
    @Parameters({
            @Parameter(name = "wagerSummary", description = "Wager summary object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Update wager summary.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @PutMapping("/update")
    public ResponseEntity<WagerSummaryResponse> update(WagerSummary wagerSummary){
        wagerSummary = wagerSummaryService.update(wagerSummary);
        return ResponseEntity.ok(WagerSummaryResponse.builder()
                .id(wagerSummary.getId())
                .totalWagerAmount(wagerSummary.getTotalWagerAmount())
                .wagerDate(wagerSummary.getWagerDate())
                .accountId(wagerSummary.getAccountId()).build());
    }

    @Operation(summary = "Delete wager summary")
    @Parameters({
            @Parameter(name = "id", description = "Wager summary id", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Delete wager summary.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @PutMapping("/delete")
    public ResponseEntity<Boolean> delete(Long id){
        wagerSummaryService.delete(id);
        return ResponseEntity.ok(true);
    }
}
