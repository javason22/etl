package demo.etl.core.transformer;

import demo.etl.entity.InputType;

import java.util.List;

/**
 * Transformer interface
 * It transforms input data to another type.
 *
 * @param <T> input type
 * @param <K> output type
 */
public interface Transformer<T extends InputType<T>, K> {
    /**
     * The actual transformation logic should be implemented in this method.
     *
     * @param data input data
     * @return List of transformed data
     */
    List<K> transform(List<T> data);
}
