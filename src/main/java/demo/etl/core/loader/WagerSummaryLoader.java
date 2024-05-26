package demo.etl.core.loader;

import demo.etl.core.loader.Loader;
import demo.etl.entity.output.WagerSummary;
import demo.etl.service.WagerSummaryService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
public class WagerSummaryLoader implements Loader<WagerSummary> {

    private final WagerSummaryService wagerSummaryService;


    @Async
    @Override
    public CompletableFuture<List<WagerSummary>> load(List<WagerSummary> data) {
        List<WagerSummary> result = wagerSummaryService.saveAll(data);
        return CompletableFuture.completedFuture(result);
    }

}
