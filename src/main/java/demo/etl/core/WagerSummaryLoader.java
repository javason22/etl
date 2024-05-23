package demo.etl.core;

import demo.etl.entity.output.WagerSummary;
import demo.etl.repository.output.WagerSummaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
public class WagerSummaryLoader implements Loader<WagerSummary>{

    private final WagerSummaryRepository wagerSummaryRepository;


    @Async
    @Override
    public CompletableFuture<List<WagerSummary>> load(List<WagerSummary> data) {
        List<WagerSummary> result = wagerSummaryRepository.saveAll(data);
        return CompletableFuture.completedFuture(result);
    }


}
