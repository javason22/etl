package demo.etl.core.processor;

import demo.etl.core.extractor.Extractor;
import demo.etl.core.loader.Loader;
import demo.etl.core.transformer.Transformer;
import demo.etl.entity.InputType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * ETL Processor acts as the base class for ETL process.
 * It takes care of the ETL process by calling the extractor,
 * transformer and loader.
 *
 * @param <E> Extractor's input type. It should be the same as Transformer's input type.
 * @param <L> Loader's output type. It should be the same as Transformer's output type.
 * @param <P> Extractor's parameter type.
 */
@Slf4j
public class EtlProcessor<E extends InputType<E>, L, P> {

    protected Extractor<E, P> extractor;
    protected Transformer<E, L> transformer;
    protected Loader<L> loader;

    /**
     * Process the ETL transformation. It will call the extractor to extract the data,
     * with the given parameter and batch size for each batch iteration. Then it will
     * transform the data by Transformer and load by Loader
     *
     * @param param parameter for extraction in the extractor
     * @param batchSize size of batch to process
     */
    public void process(P param, int batchSize){
        log.info("Start processing");
        int batchCount = 0;
        boolean isFinalBatch = false;
        List<E> lastBatchData = new ArrayList<>();
        List<CompletableFuture<List<L>>> futures = new ArrayList<>();
        while(!isFinalBatch) {
            List<E> sourceData = extractor.extract(param, batchCount, batchSize);
            // final batch data checking
            if (sourceData.isEmpty() || sourceData.size() < batchSize) { // if no more wagers to process
                /*if(!lastBatchData.isEmpty()){ // if there are remaining wagers in the last batch waiting to be processed
                    //List<L> transformedData = transformer.transform(lastBatchData);
                    //futures.add(loader.load(transformedData));
                    // add the last batch remaining data to the beginning of current batch
                    sourceData.addAll(0, lastBatchData);
                    lastBatchData.clear(); // clear the last batch data
                }*/
                isFinalBatch = true;
            }
            if (!lastBatchData.isEmpty()){ // if there are remaining wagers in the last batch waiting to be processed
                // add the last batch remaining data to the beginning of current batch
                sourceData.addAll(0, lastBatchData);
                lastBatchData.clear(); // clear the last batch data
            }
            List<E> dataForProcess = new ArrayList<>(sourceData);
            if(!isFinalBatch) {
                // pop out the last data of same grouping in the batch
                E lastSourceData = sourceData.get(sourceData.size() - 1);
                // remove the data with same group as last data in the batch
                sourceData.stream()
                        .filter(data -> data.sameGroupToTransform(lastSourceData))
                        .forEach(wager -> {
                            lastBatchData.add(wager);
                        });
                dataForProcess = sourceData.stream().filter(data -> !lastBatchData.contains(data)).collect(Collectors.toList());
            }
            List<L> wagerSummaries = transformer.transform(dataForProcess);
            futures.add(loader.load(wagerSummaries));

            batchCount++;
            log.info("Processed batch: {} size of batch {}", batchCount, sourceData.size());
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        log.info("End processing");
    }

}
