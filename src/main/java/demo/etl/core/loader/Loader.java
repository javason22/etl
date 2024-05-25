package demo.etl.core.loader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for output data to another endpoint
 *
 * @param <T> type of the data to be loaded. It can be a list of entities, DTOs, etc.
 */
public interface Loader<T> {

    /**
     * Load data to another endpoint.
     * The endpoint can be a database, a file, a message queue, etc.
     * The subclass should annotate @Async to implement the logic
     * to load the data and return the result as a CompletableFuture.
     *
     * @param data
     * @return CompletableFuture of the loaded data
     */
    CompletableFuture<List<T>> load(List<T> data);
}
