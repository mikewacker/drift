package io.github.mikewacker.drift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.json.JsonSerializationException;
import io.github.mikewacker.drift.json.JsonValues;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Abstract JSON {@link ApiClient} that is internally backed by {@code OkHttp}.
 * <p>
 * Implementations will...
 * <ul>
 *     <li>define {@code <S>}, the interface for the post-build stage that can send the request
 *     <li>provide {@code requestBuilder()} and <code>requestBuilder({@link TypeReference}&lt;V&gt;)</code> methods.
 * </ul>
 */
public abstract class AbstractOkHttpJsonApiClient implements ApiClient {

    // If a generic method has multiple type parameters (e.g., S and V),
    // an @Override of that method cannot bind some type parameters (e.g., bind S but not V).
    // This limitation of Java influences the design of this class.
    // (Also, S may depend on V in the implementation. Thus, S also cannot be defined in the class declaration.)

    /**
     * Creates a staged builder for a JSON API request whose response is only an HTTP status code.
     *
     * @param sendStageFactory a factory for the post-build stage that can send this request
     * @return a request builder at the initial stage
     * @param <S> the interface for the post-build stage that can send this request
     */
    protected final <S> UrlStageRequestBuilder<S> requestBuilder(SendStageFactory<S, Integer> sendStageFactory) {
        return new RequestBuilder<>(sendStageFactory, Response::code);
    }

    /**
     * Creates a staged builder for a JSON API request whose response is an {@link HttpOptional} value.
     *
     * @param sendStageFactory a factory for the post-build stage that can send this request
     * @param responseValueTypeRef a {@link TypeReference} for the response value
     * @return a request builder at the initial stage
     * @param <S> the interface for the post-build stage that can send this request
     * @param <V> the type of the response value
     */
    protected final <S, V> UrlStageRequestBuilder<S> requestBuilder(
            SendStageFactory<S, HttpOptional<V>> sendStageFactory, TypeReference<V> responseValueTypeRef) {
        ResponseAdapter<HttpOptional<V>> responseAdapter = new JsonValueResponseAdapter<>(responseValueTypeRef);
        return new RequestBuilder<>(sendStageFactory, responseAdapter);
    }

    /** Default constructor. */
    protected AbstractOkHttpJsonApiClient() {}

    /**
     * Factory that creates the post-build stage that can send this request.
     *
     * @param <S> the interface for the post-build stage that can send this request
     * @param <R> the type of the response, either an HTTP {@code Integer} status code or an {@link HttpOptional} value
     */
    @FunctionalInterface
    protected interface SendStageFactory<S, R> {

        /**
         * Creates the post-build stage that can send this request.
         *
         * @param rawRequest the raw {@link Request}
         * @param responseAdapter the adapter for the raw {@link Response}
         * @return the post-build stage that can send this request.
         */
        S create(Request rawRequest, ResponseAdapter<R> responseAdapter);
    }

    /**
     * Adapter that converts the raw {@link Response} to the response.
     *
     * @param <R> the type of the response, either an HTTP {@code Integer} status code or an {@link HttpOptional} value
     */
    @FunctionalInterface
    protected interface ResponseAdapter<R> {

        /**
         * Converts the raw {@link Response} to the response.
         *
         * @param rawResponse the raw {@code Response}
         * @return the response, either an HTTP status code or an {@link HttpOptional} value
         * @throws IOException if the request fails
         * @throws JsonSerializationException if the raw response has a body that cannot be deserialized from JSON
         */
        R convert(Response rawResponse) throws IOException;
    }

    /** Internal builder for a JSON API request. */
    private static final class RequestBuilder<S, V>
            implements UrlStageRequestBuilder<S>,
                    HeadersOrFinalStageRequestBuilder<S>,
                    HeadersOrBodyOrFinalStageRequestBuilder<S> {

        private static final MediaType JSON_CONTENT_TYPE = MediaType.get("application/json");
        private static final RequestBody EMPTY_BODY = RequestBody.create(new byte[0]);

        private final SendStageFactory<S, V> sendStageFactory;
        private final ResponseAdapter<V> responseAdapter;

        private final Request.Builder rawRequestBuilder = new Request.Builder();
        private RequestBuilderStage stage = RequestBuilderStage.URL;

        // Request.Builder sets the HTTP method and the body together, while this builder sets them in separate methods.
        private String method = null;
        private RequestBody body = EMPTY_BODY;

        @Override
        public RequestBuilder<S, V> get(String url) {
            return method("GET", url);
        }

        @Override
        public RequestBuilder<S, V> put(String url) {
            return method("PUT", url);
        }

        @Override
        public RequestBuilder<S, V> post(String url) {
            return method("POST", url);
        }

        @Override
        public RequestBuilder<S, V> delete(String url) {
            return method("DELETE", url);
        }

        @Override
        public RequestBuilder<S, V> patch(String url) {
            return method("PATCH", url);
        }

        @Override
        public RequestBuilder<S, V> head(String url) {
            return method("HEAD", url);
        }

        @Override
        public RequestBuilder<S, V> header(String name, String value) {
            checkAndAdvanceStage(RequestBuilderStage.HEADERS, RequestBuilderStage.HEADERS);
            rawRequestBuilder.addHeader(name, value);
            return this;
        }

        @Override
        public RequestBuilder<S, V> body(Object requestValue) {
            checkAndAdvanceStage(RequestBuilderStage.BODY, RequestBuilderStage.FINAL);
            byte[] rawRequestValue = JsonValues.serialize(requestValue);
            body = RequestBody.create(rawRequestValue, JSON_CONTENT_TYPE);
            return this;
        }

        @Override
        public S build() {
            checkAndAdvanceStage(RequestBuilderStage.FINAL, RequestBuilderStage.SEND);
            switch (method) {
                case "GET" -> rawRequestBuilder.get();
                case "HEAD" -> rawRequestBuilder.head();
                default -> rawRequestBuilder.method(method, body);
            }
            Request rawRequest = rawRequestBuilder.build();
            return sendStageFactory.create(rawRequest, responseAdapter);
        }

        /** Sets the HTTP method and the URL. */
        private RequestBuilder<S, V> method(String method, String url) {
            checkAndAdvanceStage(RequestBuilderStage.URL, RequestBuilderStage.HEADERS);
            this.method = method;
            rawRequestBuilder.url(url);
            return this;
        }

        /** Checks the current builder stage, and also advances the builder stage. */
        private void checkAndAdvanceStage(RequestBuilderStage expected, RequestBuilderStage next) {
            if (stage.compareTo(expected) > 0) {
                throw new IllegalStateException("stage already completed");
            }

            stage = next;
        }

        private RequestBuilder(SendStageFactory<S, V> sendStageFactory, ResponseAdapter<V> responseAdapter) {
            this.sendStageFactory = sendStageFactory;
            this.responseAdapter = responseAdapter;
        }
    }

    /** Stages for the {@code RequestBuilder}. */
    private enum RequestBuilderStage {
        URL,
        HEADERS,
        BODY,
        FINAL,
        SEND,
    }

    /** Reads the response body and deserializes it from JSON, or returns an error status code. */
    private record JsonValueResponseAdapter<V>(TypeReference<V> responseValueTypeRef)
            implements ResponseAdapter<HttpOptional<V>> {

        @Override
        public HttpOptional<V> convert(Response rawResponse) throws IOException {
            if (!rawResponse.isSuccessful()) {
                return HttpOptional.empty(rawResponse.code());
            }

            checkContentType(rawResponse);
            byte[] rawResponseValue = rawResponse.body().bytes();
            V responseValue = JsonValues.deserialize(rawResponseValue, responseValueTypeRef);
            return HttpOptional.of(responseValue);
        }

        /** Checks that the {@code Content-Type} is {@code application/json}. */
        private void checkContentType(Response rawResponse) {
            String rawContentType = rawResponse.header("Content-Type");
            if (rawContentType == null) {
                throw JsonSerializationException.deserializeMissingContentType();
            }

            MediaType contentType = MediaType.parse(rawContentType);
            if (contentType == null
                    || !contentType.type().equals("application")
                    || !contentType.subtype().equals("json")) {
                throw JsonSerializationException.deserializeInvalidContentType(rawContentType);
            }
        }
    }
}
