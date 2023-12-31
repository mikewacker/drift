package io.github.mikewacker.drift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.json.JsonSerializationException;
import java.io.IOException;

/** Shared HTTP client that sends requests to a JSON API. */
public interface JsonApiClient extends ApiClient {

    /**
     * Creates a staged builder for a JSON API request whose response is only an HTTP status code.
     *
     * @return a request builder at the initial stage
     */
    static UrlStageRequestBuilder<SendStage<Integer>> requestBuilder() {
        return null; // TODO: Return the implementation.
    }

    /**
     * Creates a staged builder for a JSON API request whose response is an {@link HttpOptional} value.
     *
     * @param responseValueTypeRef a {@link TypeReference} for the response value
     * @return a request builder at the initial stage
     * @param <V> the type of the response value
     */
    static <V> UrlStageRequestBuilder<SendStage<HttpOptional<V>>> requestBuilder(
            TypeReference<V> responseValueTypeRef) {
        return null; // TODO: Return the implementation
    }

    /**
     * Post-build stage that can send this request.
     *
     * @param <R> the type of the response, either an HTTP {@code Integer} status code or an {@link HttpOptional} value
     */
    interface SendStage<R> {

        /**
         * Synchronously sends this request, returning the response.
         *
         * @return the response, either an HTTP status code or an {@link HttpOptional} value
         * @throws IOException if this request fails
         * @throws JsonSerializationException if the response has a body that cannot be deserialized from JSON
         */
        R execute() throws IOException;
    }
}
