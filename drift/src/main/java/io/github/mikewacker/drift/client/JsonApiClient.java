package io.github.mikewacker.drift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.json.JsonSerializationException;
import java.io.IOException;

/** Shared HTTP client that sends requests to a JSON API. */
public interface JsonApiClient extends BaseJsonApiClient {

    /**
     * Creates a staged builder for a JSON API request.
     *
     * @return a request builder at the response type stage
     */
    static ResponseTypeStageRequestBuilder requestBuilder() {
        return JsonApiClientImpl.requestBuilder();
    }

    /** Staged builder for a JSON API request that can set the type of the response. */
    interface ResponseTypeStageRequestBuilder extends BaseResponseTypeStageRequestBuilder {

        @Override
        RouteStageRequestBuilder<SendStage<Integer>> statusCodeResponse();

        @Override
        <V> RouteStageRequestBuilder<SendStage<HttpOptional<V>>> jsonResponse(TypeReference<V> responseValueTypeRef);
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
