package demo.etl.core;

import demo.etl.entity.InputType;
import demo.etl.entity.OutputType;

import java.util.List;

/**
 * Transformer interface
 *
 * @param <T> input type
 * @param <K> output type
 * @param <P> parameter type
 */
public interface Transformer<T extends InputType, K extends OutputType, P> {
    List<K> transform(List<T> data, P params);
}
