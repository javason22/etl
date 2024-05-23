package demo.etl.core;

import demo.etl.entity.InputType;
import demo.etl.entity.OutputType;

import java.util.List;

/**
 * Transformer interface
 *
 * @param <T> input type
 * @param <K> output type
 */
public interface Transformer<T extends InputType, K extends OutputType> {
    List<K> transform(List<T> data);
}
