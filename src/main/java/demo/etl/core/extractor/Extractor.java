package demo.etl.core.extractor;

import demo.etl.entity.InputType;

import java.util.List;

/**
 * Interface for extracting data from a source
 *
 * @param <T>
 * @param <P> parameter for extraction
 */
public interface Extractor<T extends InputType<T>, P> {

    /**
     * Extract data from a source with specific parameter
     *
     * @param param parameter for extraction
     * @param page page number
     * @param size page size
     * @return List of elements extracted from the source
     */
    List<T> extract(P param, int page, int size);

}
