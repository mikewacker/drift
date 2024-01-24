package io.github.mikewacker.drift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;

/**
 * HTTP client for an JSON API. The staged request builder includes a post-build stage that can send the request.
 * <p>
 * A sub-interface will...
 * <ul>
 *     <li>define a sub-interface for {@link BaseResponseTypeStageRequestBuilder}.
 *     <li>provide a {@code requestBuilder()} method that returns said sub-interface.
 * </ul>
 */
public interface BaseJsonApiClient {

    /**
     * Staged builder for a JSON API request that can set the type of the response.
     * <p>
     * A sub-interface will override the return type to {@code RouteStageRequestBuilder<S>} and bind {@code S}.
     */
    interface BaseResponseTypeStageRequestBuilder {

        /**
         * Sets the type of the response to only an HTTP status code.
         *
         * @return this request builder at the route stage
         */
        Object statusCodeResponse();

        /**
         * Sets the type of the response to an {@link HttpOptional} value that is deserialized from JSON.
         *
         * @param responseValueTypeRef a {@link TypeReference} for the response value
         * @return this request builder at the route stage
         * @param <V> the type of the response value
         */
        <V> Object jsonResponse(TypeReference<V> responseValueTypeRef);
    }

    /**
     * Staged builder for a JSON API request that can set the route: the HTTP method and the URL.
     *
     * @param <S> the interface for the post-build stage that can send this request
     */
    interface RouteStageRequestBuilder<S> {

        /**
         * Uses a GET request at the specified URL.
         *
         * @param url the URL for this request
         * @return this request builder at the headers or final stage
         */
        HeadersOrFinalStageRequestBuilder<S> get(String url);

        /**
         * Uses a PUT request at the specified URL.
         *
         * @param url the URL for this request
         * @return this request builder at the headers, body, or final stage
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> put(String url);

        /**
         * Uses a POST request at the specified URL.
         *
         * @param url the URL for this request
         * @return this request builder at the headers, body, or final stage
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> post(String url);

        /**
         * Uses a DELETE request at the specified URL.
         *
         * @param url the URL for this request
         * @return this request builder at the headers, body, or final stage
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> delete(String url);

        /**
         * Uses a PATCH request at the specified URL.
         *
         * @param url the URL for this request
         * @return this request builder at the headers, body, or final stage
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> patch(String url);

        /**
         * Uses a HEAD request at the specified URL.
         *
         * @param url the URL for this request
         * @return this request builder at the headers or final stage
         */
        HeadersOrFinalStageRequestBuilder<S> head(String url);
    }

    /**
     * Staged builder for a JSON API request that can add headers or build this request.
     *
     * @param <S> the interface for the post-build stage that can send this request
     */
    interface HeadersOrFinalStageRequestBuilder<S> extends FinalStageRequestBuilder<S> {

        /**
         * Adds an HTTP header.
         * <p>
         * Headers related to the request body, such as {@code Content-Type} or {@code Content-Length},
         * do not need to be added.
         *
         * @param name the name of the header
         * @param value the value of the header
         * @return this request builder at the headers or final stage
         */
        HeadersOrFinalStageRequestBuilder<S> header(String name, String value);
    }

    /**
     * Staged builder for a JSON API request that can add headers, set the body, or build this request.
     *
     * @param <S> the interface for the post-build stage that can send this request
     */
    interface HeadersOrBodyOrFinalStageRequestBuilder<S> extends BodyOrFinalStageRequestBuilder<S> {

        /**
         * Adds an HTTP header.
         * <p>
         * Headers related to the request body, such as {@code Content-Type} or {@code Content-Length},
         * do not need to be added.
         *
         * @param name the name of the header
         * @param value the value of the header
         * @return this request builder at the headers, body, or final stage
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> header(String name, String value);
    }

    /**
     * Staged builder for a JSON API request that can set the body or build this request.
     *
     * @param <S> the interface for the post-build stage that can send this request
     */
    interface BodyOrFinalStageRequestBuilder<S> extends FinalStageRequestBuilder<S> {

        /**
         * Sets the body using a value.
         *
         * @param requestValue the value that will be serialized and used as the body
         * @return this request builder at the final stage
         */
        FinalStageRequestBuilder<S> body(Object requestValue);
    }

    /**
     * Staged builder for a JSON API request that can build this request.
     *
     * @param <S> the interface for the post-build stage that can send this request
     */
    interface FinalStageRequestBuilder<S> {

        /**
         * Builds this request, returning a post-build stage that can send this request.
         *
         * @return the post-build stage that can send this request
         */
        S build();
    }
}
