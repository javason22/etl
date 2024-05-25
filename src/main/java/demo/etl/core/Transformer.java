package demo.etl.core;

import demo.etl.entity.InputType;

import java.util.List;

/**
 * Transformer interface
 *
 * @param <T> input type
 * @param <K> output type
 */
public interface Transformer<T extends InputType<T>, K> {
    List<K> transform(List<T> data);
}
