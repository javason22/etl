package demo.etl.controller;

import demo.etl.entity.input.Wager;
import demo.etl.resp.WagerResponse;
import demo.etl.service.WagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Wager controller
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wager")
public class WagerController {

    private final WagerService wagerService;

    @Operation(summary = "List all wagers with pagination")
    @Parameters({@Parameter(name = "example", description = "Wager example", required = true),
            @Parameter(name = "page", description = "Page number", required = true),
            @Parameter(name = "size", description = "Page size", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    List of wagers.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @GetMapping("/list")
    public ResponseEntity<Page<WagerResponse>> list(@RequestParam Wager example, @RequestParam int page, @RequestParam int size){
        log.debug("List wagers example={}, page={}, size={}", example, page, size);
        Page<Wager> result = wagerService.page(example, page, size);
        return ResponseEntity.ok(result.map(wager -> WagerResponse.builder()
                .id(wager.getId())
                .wagerAmount(wager.getWagerAmount())
                .wagerTimestamp(wager.getWagerTimestamp())
                .accountId(wager.getAccountId()).build()));
    }

    @Operation(summary = "Add wager")
    @Parameters({
            @Parameter(name = "wager", description = "Wager object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Add wager.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @PutMapping("/add")
    public ResponseEntity<WagerResponse> add(Wager wager){
        log.debug("Add wager={}", wager);
        try{
            wager = wagerService.add(wager);
            return ResponseEntity.ok(
                    WagerResponse.builder()
                            .id(wager.getId())
                            .wagerAmount(wager.getWagerAmount())
                            .wagerTimestamp(wager.getWagerTimestamp())
                            .accountId(wager.getAccountId()).build());
        } catch (Exception e){
            log.error("Error adding wager", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update wager")
    @Parameters({
            @Parameter(name = "wager", description = "Wager object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Update wager.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @PostMapping("/update")
    public ResponseEntity<Boolean> update(Wager wager){
        log.debug("Update wager={}", wager);
        try{
            wagerService.update(wager);
            return ResponseEntity.ok(true);
        } catch (Exception e){
            log.error("Error updating wager", e);
            return ResponseEntity.ok(false);
        }
    }

    @Operation(summary = "Delete wager by id")
    @Parameters({
            @Parameter(name = "id", description = "Wager id", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Delete wager.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        log.debug("Delete wager id={}", id);
        try{
            wagerService.delete(id);
            return ResponseEntity.ok(true);
        } catch (Exception e){
            log.error("Error deleting wager", e);
            return ResponseEntity.ok(false);
        }
    }

}
