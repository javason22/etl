package demo.etl.entity;

/**
 * Input type interface to be used in ETL Extractor
 *
 * @param <T> the type of input
 */
public interface InputType<T> {

    /**
     * Check if the input is the same group to transform
     *
     * @param comparedTo the input to compare
     * @return true if the input is the same group to transform
     */
    boolean sameGroupToTransform(T comparedTo);
}
