package io.github.mikewacker.drift.client;

/** HTTP client for an API. The staged request builder includes a post-build stage that can send the request. */
public interface ApiClient {

    /**
     * Staged builder for an API request that can set the HTTP method and the URL together.
     *
     * @param <S> the interface for the post-build stage that can send this request
     */
    interface UrlStageRequestBuilder<S> {

        /**
         * Uses a GET request at the specified URL.
         *
         * @param url the URL for this request
         * @return the next stage of this request builder
         */
        HeadersOrFinalStageRequestBuilder<S> get(String url);

        /**
         * Uses a PUT request at the specified URL.
         *
         * @param url the URL for this request
         * @return the next stage of this request builder
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> put(String url);

        /**
         * Uses a POST request at the specified URL.
         *
         * @param url the URL for this request
         * @return the next stage of this request builder
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> post(String url);

        /**
         * Uses a DELETE request at the specified URL.
         *
         * @param url the URL for this request
         * @return the next stage of this request builder
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> delete(String url);

        /**
         * Uses a PATCH request at the specified URL.
         *
         * @param url the URL for this request
         * @return the next stage of this request builder
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> patch(String url);

        /**
         * Uses a HEAD request at the specified URL.
         *
         * @param url the URL for this request
         * @return the next stage of this request builder
         */
        HeadersOrFinalStageRequestBuilder<S> head(String url);
    }

    /**
     * Staged builder for an API request that can add headers or build this request.
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
         * @return the same stage of this request builder
         */
        HeadersOrFinalStageRequestBuilder<S> header(String name, String value);
    }

    /**
     * Staged builder for an API request that can add headers, set the body, or build this request.
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
         * @return the same stage of this request builder
         */
        HeadersOrBodyOrFinalStageRequestBuilder<S> header(String name, String value);
    }

    /**
     * Staged builder for an API request that can set the body or build this request.
     *
     * @param <S> the interface for the post-build stage that can send this request
     */
    interface BodyOrFinalStageRequestBuilder<S> extends FinalStageRequestBuilder<S> {

        /**
         * Sets the body using a value.
         *
         * @param requestValue the value that will be serialized and used as the body
         * @return the next stage of this request builder
         */
        FinalStageRequestBuilder<S> body(Object requestValue);
    }

    /**
     * Staged builder for an API request that can build this request.
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
