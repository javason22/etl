package demo.etl.controller;

import demo.etl.entity.output.WagerSummary;
import demo.etl.resp.WagerSummaryResponse;
import demo.etl.service.WagerSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "WagerSummary", description = "APIs for wager summary operations")
@RequestMapping("/api/v1/wager-summary")
public class WagerSummaryController {

    private final WagerSummaryService wagerSummaryService;

    private final RedissonClient redissonClient;

    @Operation(summary = "List all wager summaries with pagination")
    @Parameters({@Parameter(name = "example", description = "Wager summary example", required = true),
            @Parameter(name = "page", description = "Page number", required = true),
            @Parameter(name = "size", description = "Page size", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    List of wager summaries.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @GetMapping("/")
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
    @PostMapping("/")
    public ResponseEntity<WagerSummaryResponse> add(@RequestBody WagerSummary wagerSummary){
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
    @PutMapping("/")
    public ResponseEntity<WagerSummaryResponse> update(@RequestBody WagerSummary wagerSummary){
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id){
        wagerSummaryService.delete(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Get wager summary")
    @Parameters({
            @Parameter(name = "id", description = "Wager summary id", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Get wager summary.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @GetMapping("/{id}")
    public ResponseEntity<WagerSummaryResponse> get(@PathVariable String id){
        WagerSummary wagerSummary = wagerSummaryService.get(id);
        return ResponseEntity.ok(WagerSummaryResponse.builder()
                .id(wagerSummary.getId())
                .totalWagerAmount(wagerSummary.getTotalWagerAmount())
                .wagerDate(wagerSummary.getWagerDate())
                .accountId(wagerSummary.getAccountId()).build());
    }
}
