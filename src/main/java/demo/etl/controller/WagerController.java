package demo.etl.controller;

import demo.etl.entity.input.Wager;
import demo.etl.resp.WagerResponse;
import demo.etl.service.WagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Wager controller
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Wager", description = "APIs for wager operations. CRUD operations for wager.")
@RequestMapping("/api/v1/wager")
public class WagerController {

    private final WagerService wagerService;

    @Operation(summary = "List all wagers with pagination")
    @Parameters({@Parameter(name = "example", description = "Wager example", required = true),
            @Parameter(name = "page", description = "Page number", required = true),
            @Parameter(name = "size", description = "Page size", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Page of wagers.
                    1. code=200, success.
                    2. code=400, parameters invalid.""")})
    @GetMapping("/")
    public ResponseEntity<Page<WagerResponse>> list(@RequestParam(required = false) Wager example, @RequestParam int page, @RequestParam int size){
        log.debug("List wagers example={}, page={}, size={}", example, page, size);
        Page<Wager> result = wagerService.page(example, page, size);
        return ResponseEntity.ok(result.map(wager -> WagerResponse.builder()
                .id(wager.getId())
                .wagerAmount(wager.getWagerAmount())
                .wagerTimestamp(wager.getWagerTimestamp())
                .accountId(wager.getAccountId()).build()));
    }

    @Operation(summary = "Get wager by giving its unique ID in the URL.")
    @Parameters({
            @Parameter(name = "id", description = "Wager id", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Get wager by ID.
                    1. code=200, get wager successfully.
                    2. code=400, parameters invalid.
                    3. code=404, wager not found.""")})
    @GetMapping("/{id}")
    public ResponseEntity<WagerResponse> get(@PathVariable String id){
        log.debug("Get wager id={}", id);
        Wager wager = wagerService.get(id);
        if(wager == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(WagerResponse.builder()
                .id(wager.getId())
                .wagerAmount(wager.getWagerAmount())
                .wagerTimestamp(wager.getWagerTimestamp())
                .accountId(wager.getAccountId()).build());
    }

    @Operation(summary = "Create a new wager record by sending the full object of the wager without the unique ID.")
    @Parameters({
            @Parameter(name = "wager", description = "Wager object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Create a new wager.
                    1. code=201, wager created successfully.
                    2. code=400, parameters invalid.
                    3. code=422, duplicate wager creation.
                    3. code=500, internal server error.""")})
    @PostMapping("/")
    public ResponseEntity<WagerResponse> add(@RequestBody Wager wager){
        log.debug("Add wager={}", wager);
        try{
            Wager createdWager = wagerService.add(wager);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    WagerResponse.builder()
                            .id(createdWager.getId())
                            .wagerAmount(createdWager.getWagerAmount())
                            .wagerTimestamp(createdWager.getWagerTimestamp())
                            .accountId(createdWager.getAccountId()).build());
        } catch(DataIntegrityViolationException e){
            log.error("Duplicate wager creation", e);
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e){
            log.error("Error adding wager", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update wager by sending the full object of the wager with the new values and unique ID.")
    @Parameters({
            @Parameter(name = "wager", description = "Wager object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Update wager.
                    1. code=200, update wager successfully.
                    2. code=400, parameters invalid.
                    3. code=404, wager not found.
                    3. code=500, internal server error.""")})
    @PutMapping("/")
    public ResponseEntity<WagerResponse> update(@RequestBody Wager wager){
        log.debug("Update wager={}", wager);
        try{
            Wager updatedWager = wagerService.update(wager);
            if(updatedWager == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(WagerResponse.builder()
                    .id(updatedWager.getId())
                    .wagerAmount(updatedWager.getWagerAmount())
                    .wagerTimestamp(updatedWager.getWagerTimestamp())
                    .accountId(updatedWager.getAccountId()).build());
        } catch (Exception e){
            log.error("Error updating wager", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Delete wager by giving its unique ID in the URL.")
    @Parameters({
            @Parameter(name = "id", description = "Wager id", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = """
                    Delete wager.
                    1. code=204, Delete wager successfully. No content returned.
                    2. code=400, parameters invalid.
                    3. code=404, wager not found.
                    4. code=500, internal server error.""")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        log.debug("Delete wager id={}", id);
        try{
            if(!wagerService.delete(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            log.error("Error deleting wager", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
