package demo.etl.core;

import demo.etl.entity.InputType;

import java.util.List;

/**
 * Interface for extracting data from a source
 *
 * @param <T>
 * @param <P> parameter for extraction
 */
public interface Extractor<T extends InputType, P> {

    /**
     * Extract data from a source with certain parameter
     *
     * @param param parameter for extraction
     * @return List of elements extracted from the source
     */
    List<T> extract(P param);

}
