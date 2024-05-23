package demo.etl.core;

import demo.etl.entity.OutputType;

import java.util.List;

/**
 * Interface for output data to another endpoint
 *
 * @param <T>
 */
public interface Loader<T extends OutputType, P> {

    /**
     * Load data to another endpoint. It will clear all the data in the endpoint before loading
     *
     * @param data
     */
    void load(List<T> data);

    /**
     * Load data to another endpoint. The function will clear data hit by the parameter before loading
     *
     * @param data
     * @param param
     */
    void load(List<T> data, P param);
}
