package demo.etl.core;

import demo.etl.entity.InputType;
import demo.etl.entity.OutputType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class EtlProcessor<T extends InputType, M extends OutputType, P> {

    protected Extractor<T, P> extractor;
    protected Transformer<T, M> transformer;
    protected Loader<M> loader;

    public void process(P param){
        log.info("Start processing");
        List<T> sourceData = extractor.extract(param);
        if(sourceData.isEmpty()){
            log.info("No data found");
            return;
        }
        log.info("Extracted {} data", sourceData.size());
        List<M> transformedData = transformer.transform(sourceData);
        if(transformedData.isEmpty()){
            log.info("No data transformed");
            return;
        }
        log.info("Transformed {} data", transformedData.size());
        loader.load(transformedData);
        log.info("End processing");
    }

}
