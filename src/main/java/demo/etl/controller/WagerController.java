package demo.etl.controller;

import demo.etl.dto.resp.GeneralResponse;
import demo.etl.entity.input.Wager;
import demo.etl.dto.req.WagerRequest;
import demo.etl.dto.resp.WagerResponse;
import demo.etl.service.WagerService;
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
    @Parameters({
            @Parameter(name = "request", description = "Search criteria request", required = true),
            @Parameter(name = "page", description = "Page number", required = true, example = "0"),
            @Parameter(name = "size", description = "Page size", required = true, example = "10")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return a page of wagers successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @GetMapping("/")
    public ResponseEntity<Page<WagerResponse>> list(@ModelAttribute WagerRequest request,
                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "10") int size){
        log.info("List wagers example={}, page={}, size={}", request, page, size);
        Wager searchCriteria = new Wager();
        if(request != null){
            BeanUtils.copyProperties(request, searchCriteria);
        }
        Page<Wager> result = wagerService.page(searchCriteria, page, size);
        return ResponseEntity.ok(result.map(wager -> WagerResponse.builder()
                .id(wager.getId())
                .wagerAmount(wager.getWagerAmount())
                .wagerTimestamp(wager.getWagerTimestamp())
                .accountId(wager.getAccountId()).build()));
    }

    @Operation(summary = "Get wager by giving its unique ID in the URL.")
    @Parameters({
            @Parameter(name = "id", description = "Wager id", required = true, example = "123e4567-e89b-12d3-a456-426614174000")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "get wager successfully"),
            @ApiResponse(responseCode = "404", description = "Wager not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @GetMapping("/{id}")
    public ResponseEntity<WagerResponse> get(@ValidUUID @PathVariable String id){
        log.info("Get wager id={}", id);
        Wager wager = wagerService.get(id);
        if(wager == null){
            //return ResponseEntity.notFound().build();
            throw new DataRetrievalFailureException("Wager not found");
        }
        return ResponseEntity.ok(WagerResponse.builder()
                .id(wager.getId())
                .wagerAmount(wager.getWagerAmount())
                .wagerTimestamp(wager.getWagerTimestamp())
                .accountId(wager.getAccountId()).build());
    }

    @Operation(summary = "Create a new wager record by sending the full object of the wager without the unique ID.")
    @Parameters({
            @Parameter(name = "request", description = "Wager object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Create wager successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "422", description = "Duplicate wager creation."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @PostMapping("/")
    public ResponseEntity<WagerResponse> add(@Valid @RequestBody WagerRequest request){
        log.info("Add wager={}", request);
        // convert request object to entity
        Wager wager = Wager.builder()
                .accountId(request.getAccountId())
                .wagerAmount(request.getWagerAmount())
                .wagerTimestamp(request.getWagerTimestamp()).build();
        wager = wagerService.add(wager);
        // convert entity to response object
        return ResponseEntity.status(HttpStatus.CREATED).body(
                WagerResponse.builder()
                        .id(wager.getId())
                        .wagerAmount(wager.getWagerAmount())
                        .wagerTimestamp(wager.getWagerTimestamp())
                        .accountId(wager.getAccountId()).build());
    }

    @Operation(summary = "Update wager by sending the full object of the wager with the new values and unique ID.")
    @Parameters({
            @Parameter(name = "id", description = "Wager id", required = true, example = "123e4567-e89b-12d3-a456-426614174000"),
            @Parameter(name = "request", description = "Wager object", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update wager successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "404", description = "Wager not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @PutMapping("/{id}")
    public ResponseEntity<WagerResponse> update(@ValidUUID @PathVariable String id, @Valid @RequestBody WagerRequest request){
        log.info("Update wager={}", request);
        Wager wager = Wager.builder()
                .id(id)
                .accountId(request.getAccountId())
                .wagerAmount(request.getWagerAmount())
                .wagerTimestamp(request.getWagerTimestamp()).build();
        Wager updatedWager = wagerService.update(wager);
        if(updatedWager == null){
            //return ResponseEntity.notFound().build();
            throw new DataRetrievalFailureException("Wager not found");
        }
        return ResponseEntity.ok(WagerResponse.builder()
                .id(updatedWager.getId())
                .wagerAmount(updatedWager.getWagerAmount())
                .wagerTimestamp(updatedWager.getWagerTimestamp())
                .accountId(updatedWager.getAccountId()).build());
    }

    @Operation(summary = "Delete wager by giving its unique ID in the URL.")
    @Parameters({
            @Parameter(name = "id", description = "Wager id", required = true)})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delete wager successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid parameter."),
            @ApiResponse(responseCode = "404", description = "Wager not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> delete(@ValidUUID @PathVariable String id){
        log.info("Delete wager id={}", id);
        if(!wagerService.delete(id)) {
            //return ResponseEntity.notFound().build();
            throw new DataRetrievalFailureException("Wager not found");
        }
        return ResponseEntity.ok(new GeneralResponse("success", MessageFormat.format("Delete wager summary {0} successfully", id)));
    }

}
