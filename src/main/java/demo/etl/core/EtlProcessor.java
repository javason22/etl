package demo.etl.core;

import demo.etl.entity.InputType;
import demo.etl.entity.input.Wager;
import demo.etl.entity.output.WagerSummary;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public abstract class EtlProcessor<T extends InputType<T>, M, P> {

    protected Extractor<T, P> extractor;
    protected Transformer<T, M> transformer;
    protected Loader<M> loader;

    public void process(P param, int batchSize){
        log.info("Start processing");
        int batchCount = 0;
        List<T> lastBatchData = new ArrayList<>();
        List<CompletableFuture<List<M>>> futures = new ArrayList<>();
        while(true) {
            List<T> sourceData = extractor.extract(param, batchCount, batchSize);
            if (sourceData.isEmpty()) { // if no more wagers to process
                if(!lastBatchData.isEmpty()){ // if there are still wagers in the last batch waiting to be processed
                    List<M> transformedData = transformer.transform(lastBatchData);
                    futures.add(loader.load(transformedData));
                }
                break;
            }
            if (!lastBatchData.isEmpty()){
                sourceData.addAll(0, lastBatchData);
                lastBatchData.clear();
            }
            T lastSourceData = sourceData.get(sourceData.size() - 1);
            // remove the data with same group as last data in the batch
            sourceData.stream()
                    .filter(data -> data.sameGroupToTransform(lastSourceData))
                    .forEach(wager ->{
                        lastBatchData.add(wager);
                    });
            List<T> reducedData = sourceData.stream().filter(data -> !lastBatchData.contains(data)).collect(Collectors.toList());
            List<M> wagerSummaries = transformer.transform(reducedData);
            futures.add(loader.load(wagerSummaries));

            batchCount++;
            log.info("Processed batch: {} size of batch {}", batchCount, sourceData.size());
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        log.info("End processing");
    }

}
