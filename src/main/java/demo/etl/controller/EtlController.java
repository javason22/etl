package demo.etl.controller;

import demo.etl.service.EtlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/etl")
@Slf4j
@Tag(name = "ETL", description = "APIs for ETL operations")
public class EtlController {

    private final EtlService etlService;

    @PostMapping("/daily-wager-summary/transform")
    public ResponseEntity<Void> triggerEtlTransform(@RequestParam LocalDate date) {
        log.info("Triggering ETL transform for date={}", date);
        etlService.transformDailyWagersToWagerSummaries(date);
        return ResponseEntity.ok().build();
    }
}
