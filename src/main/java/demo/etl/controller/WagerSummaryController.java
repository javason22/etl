package demo.etl.controller;

import demo.etl.dto.resp.GeneralResponse;
import demo.etl.entity.output.WagerSummary;
import demo.etl.dto.req.WagerSummaryRequest;
import demo.etl.dto.resp.WagerSummaryResponse;
import demo.etl.service.WagerSummaryService;
import demo.etl.validation.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "WagerSummary", description = "APIs for wager summary operations. CRUD operations for wager summary.")
@RequestMapping("/api/v1/wager-summary")
public class WagerSummaryController {

    private final WagerSummaryService wagerSummaryService;

    @Operation(summary = "List all wager summaries with pagination")
    @Parameters({@Parameter(name = "request", description = "Search criteria request", required = true),
            @Parameter(name = "page", description = "Page number", required = true),
            @Parameter(name = "size", description = "Page size", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get a Page of wager summaries successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @GetMapping("/")
    public ResponseEntity<Page<WagerSummaryResponse>> list(@ModelAttribute WagerSummaryRequest request,
                                                           @RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "10") int size){
        log.info("List wager summaries example={}, page={}, size={}", request, page, size);
        WagerSummary searchCriteria = new WagerSummary();
        if(request != null){
            BeanUtils.copyProperties(request, searchCriteria);
        }
        Page<WagerSummary> result = wagerSummaryService.page(searchCriteria, page, size);
        return ResponseEntity.ok(result.map(wagerSummary -> WagerSummaryResponse.builder()
                .id(wagerSummary.getId())
                .totalWagerAmount(wagerSummary.getTotalWagerAmount())
                .wagerDate(wagerSummary.getWagerDate())
                .accountId(wagerSummary.getAccountId()).build()));
    }

    @Operation(summary = "Add wager summary")
    @Parameters({
            @Parameter(name = "request", description = "Wager summary object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created wager summary successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "422", description = "Duplicate wager summary creation."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @PostMapping("/")
    public ResponseEntity<WagerSummaryResponse> add(@Valid @RequestBody WagerSummaryRequest request){
        log.info("Add wager summary={}", request);
        // convert request object to entity
        WagerSummary summary = WagerSummary.builder()
                .accountId(request.getAccountId())
                .totalWagerAmount(request.getTotalWagerAmount())
                .wagerDate(request.getWagerDate()).build();
        summary = wagerSummaryService.add(summary);
        // convert entity to response object
        return ResponseEntity.status(HttpStatus.CREATED).body(WagerSummaryResponse.builder()
                .id(summary.getId())
                .totalWagerAmount(summary.getTotalWagerAmount())
                .wagerDate(summary.getWagerDate())
                .accountId(summary.getAccountId()).build());
    }

    @Operation(summary = "Update wager summary")
    @Parameters({
            @Parameter(name = "id", description = "Wager summary id", required = true),
            @Parameter(name = "request", description = "Wager summary object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update wager summary successfully."),
            @ApiResponse(responseCode = "404", description = "Wager summary not found."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @PutMapping("/{id}")
    public ResponseEntity<WagerSummaryResponse> update(@ValidUUID @PathVariable String id, @Valid @RequestBody WagerSummaryRequest request){
        log.info("Update wager summary={}", request);
        WagerSummary wagerSummary = WagerSummary.builder()
                .id(id)
                .accountId(request.getAccountId())
                .totalWagerAmount(request.getTotalWagerAmount())
                .wagerDate(request.getWagerDate()).build();
        wagerSummary = wagerSummaryService.update(wagerSummary);
        if(wagerSummary == null){
            //return ResponseEntity.notFound().build();
            throw new DataRetrievalFailureException("Wager summary not found");
        }
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
            @ApiResponse(responseCode = "200", description = "Delete wager summary successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "404", description = "Wager summary not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> delete(@ValidUUID @PathVariable String id){
        log.info("Delete wager summary id={}", id);
        if(!wagerSummaryService.delete(id)) {
            //return ResponseEntity.notFound().build();
            throw new DataRetrievalFailureException("Wager summary not found");
        }
        return ResponseEntity.ok(new GeneralResponse("success", MessageFormat.format("Delete wager summary {0} successfully", id)));
    }

    @Operation(summary = "Get wager summary")
    @Parameters({
            @Parameter(name = "id", description = "Wager summary id", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get wager summary by ID successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "404", description = "Wager summary not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @GetMapping("/{id}")
    public ResponseEntity<WagerSummaryResponse> get(@ValidUUID @PathVariable String id){
        log.info("Get wager summary id={}", id);
        WagerSummary wagerSummary = wagerSummaryService.get(id);
        if(wagerSummary == null){
            //return ResponseEntity.notFound().build();
            throw new DataRetrievalFailureException("Wager summary not found");
        }
        return ResponseEntity.ok(WagerSummaryResponse.builder()
                .id(wagerSummary.getId())
                .totalWagerAmount(wagerSummary.getTotalWagerAmount())
                .wagerDate(wagerSummary.getWagerDate())
                .accountId(wagerSummary.getAccountId()).build());
    }
}
