package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.HttpOptional;

/**
 * Synchronous extractor that gets an argument for the API request from the underlying HTTP request.
 *
 * @param <E> the type of the underlying HTTP exchange
 * @param <A> the type of the argument for the API request
 */
@FunctionalInterface
public interface ArgExtractor<E, A> {

    /**
     * Synchronously extracts an argument for the API request from the underlying HTTP request.
     *
     * @param httpExchange the underlying HTTP exchange
     * @return an argument for the API request, or an error status code
     */
    HttpOptional<A> tryExtract(E httpExchange);

    /**
     * Adapts this extractor to the asynchronous interface.
     *
     * @return this extractor with an asynchronous interface
     */
    default ArgExtractor.Async<E, A> async() {
        return (exchange, callback) -> {
            HttpOptional<A> maybeArg = tryExtract(exchange);
            callback.onArgExtracted(maybeArg);
        };
    }

    /**
     * Asynchronous extractor that gets an argument for the API request from the underlying HTTP request.
     * An implementation may be synchronous or asynchronous; the asynchronous interface can be used for either variant.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <A> the type of the argument for the API request
     */
    @FunctionalInterface
    interface Async<E, A> {

        /**
         * Asynchronously extracts an argument for the API request from the underlying HTTP request.
         *
         * @param exchange the underlying HTTP exchange
         * @param callback the callback for when an argument for the API request has been extracted
         * @throws Exception for any exceptions that the callback may throw, if it is synchronously called
         */
        void tryExtract(E exchange, Callback<A> callback) throws Exception;
    }

    /**
     * Callback for when an argument for the API request has been extracted.
     *
     * @param <A> the type of the argument for the API request
     */
    @FunctionalInterface
    interface Callback<A> {

        /**
         * Called when an argument for the API request has been extracted.
         *
         * @param maybeArg an argument for the API request, or an error status code
         * @throws Exception for any exceptions that this callback may throw
         */
        void onArgExtracted(HttpOptional<A> maybeArg) throws Exception;
    }
}
