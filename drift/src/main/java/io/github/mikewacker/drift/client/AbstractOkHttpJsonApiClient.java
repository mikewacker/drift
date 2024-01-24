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
 * Abstract implementation for a sub-interface of {@link BaseJsonApiClient} that is internally backed by {@code OkHttp}.
 * <p>
 * A concrete implementation will...
 * <ul>
 *     <li>implement the sub-interface of {@link BaseResponseTypeStageRequestBuilder}.
 *     <li>implement {@code S}, the interface for the post-build stage that can send the request.
 * </ul>
 */
public abstract class AbstractOkHttpJsonApiClient implements BaseJsonApiClient {

    /**
     * Sets the type of the response to only an HTTP status code.
     * Called by the implementation for the sub-interface of {@link BaseResponseTypeStageRequestBuilder}.
     *
     * @param sendStageFactory a factory for the post-build stage that can send this request
     * @return a request builder at the route stage
     * @param <S> the interface for the post-build stage that can send this request
     */
    protected static <S> RouteStageRequestBuilder<S> statusCodeResponse(SendStageFactory<S, Integer> sendStageFactory) {
        return new RequestBuilder<>(sendStageFactory, Response::code);
    }

    /**
     * Sets the type of the response to an {@link HttpOptional} value that is deserialized from JSON.
     * Called by the implementation for the sub-interface of {@link BaseResponseTypeStageRequestBuilder}.
     *
     * @param sendStageFactory a factory for the post-build stage that can send this request
     * @param responseValueTypeRef a {@link TypeReference} for the response value
     * @return a request builder at the route stage
     * @param <S> the interface for the post-build stage that can send this request
     * @param <V> the type of the response value
     */
    protected static <S, V> RouteStageRequestBuilder<S> jsonResponse(
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

    /** Internal request builder, not including the response type stage. */
    private static final class RequestBuilder<S, R>
            implements RouteStageRequestBuilder<S>,
                    HeadersOrFinalStageRequestBuilder<S>,
                    HeadersOrBodyOrFinalStageRequestBuilder<S> {

        private static final MediaType JSON_CONTENT_TYPE = MediaType.get("application/json");
        private static final RequestBody EMPTY_BODY = RequestBody.create(new byte[0]);

        private final SendStageFactory<S, R> sendStageFactory;
        private final ResponseAdapter<R> responseAdapter;

        private final Request.Builder rawRequestBuilder = new Request.Builder();

        // Request.Builder sets the HTTP method and the body together, while this builder sets them in separate methods.
        private String method = null;
        private RequestBody body = EMPTY_BODY;

        @Override
        public RequestBuilder<S, R> get(String url) {
            return method("GET", url);
        }

        @Override
        public RequestBuilder<S, R> put(String url) {
            return method("PUT", url);
        }

        @Override
        public RequestBuilder<S, R> post(String url) {
            return method("POST", url);
        }

        @Override
        public RequestBuilder<S, R> delete(String url) {
            return method("DELETE", url);
        }

        @Override
        public RequestBuilder<S, R> patch(String url) {
            return method("PATCH", url);
        }

        @Override
        public RequestBuilder<S, R> head(String url) {
            return method("HEAD", url);
        }

        @Override
        public RequestBuilder<S, R> header(String name, String value) {
            rawRequestBuilder.addHeader(name, value);
            return this;
        }

        @Override
        public RequestBuilder<S, R> body(Object requestValue) {
            byte[] rawRequestValue = JsonValues.serialize(requestValue);
            body = RequestBody.create(rawRequestValue, JSON_CONTENT_TYPE);
            return this;
        }

        @Override
        public S build() {
            switch (method) {
                case "GET" -> rawRequestBuilder.get();
                case "HEAD" -> rawRequestBuilder.head();
                default -> rawRequestBuilder.method(method, body);
            }
            Request rawRequest = rawRequestBuilder.build();
            return sendStageFactory.create(rawRequest, responseAdapter);
        }

        /** Sets the HTTP method and the URL. */
        private RequestBuilder<S, R> method(String method, String url) {
            this.method = method;
            rawRequestBuilder.url(url);
            return this;
        }

        private RequestBuilder(SendStageFactory<S, R> sendStageFactory, ResponseAdapter<R> responseAdapter) {
            this.sendStageFactory = sendStageFactory;
            this.responseAdapter = responseAdapter;
        }
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
