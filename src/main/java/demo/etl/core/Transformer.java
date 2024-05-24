package demo.etl.core;

import java.util.List;

/**
 * Transformer interface
 *
 * @param <T> input type
 * @param <K> output type
 */
public interface Transformer<T, K> {
    List<K> transform(List<T> data);
}
