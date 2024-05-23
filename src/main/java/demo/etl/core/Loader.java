package demo.etl.core;

import demo.etl.entity.OutputType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for output data to another endpoint
 *
 * @param <T>
 * @param <P> parameter for loading
 */
public interface Loader<T extends OutputType> {

    /**
     * Load data to another endpoint.
     *
     * @param data
     */
    CompletableFuture<List<T>> load(List<T> data);
}
