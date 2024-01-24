package io.github.mikewacker.drift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/** Internal {@code JsonApiClient} implementation. */
final class JsonApiClientImpl extends AbstractOkHttpJsonApiClient implements JsonApiClient {

    private static final OkHttpClient client = new OkHttpClient();

    /** Corresponds to {@code JsonApiClient#requestBuilder()}. */
    public static ResponseTypeStageRequestBuilder requestBuilder() {
        return new ResponseTypeStageRequestBuilderImpl();
    }

    private JsonApiClientImpl() {}

    /** Internal {@code ResponseTypeStageRequestBuilder} implementation. */
    private static final class ResponseTypeStageRequestBuilderImpl implements ResponseTypeStageRequestBuilder {

        @Override
        public RouteStageRequestBuilder<SendStage<Integer>> statusCodeResponse() {
            return AbstractOkHttpJsonApiClient.statusCodeResponse(SendStageImpl::new);
        }

        @Override
        public <V> RouteStageRequestBuilder<SendStage<HttpOptional<V>>> jsonResponse(
                TypeReference<V> responseValueTypeRef) {
            return AbstractOkHttpJsonApiClient.jsonResponse(SendStageImpl::new, responseValueTypeRef);
        }
    }

    /** Internal {@code SendStage} implementation. */
    private record SendStageImpl<R>(Request rawRequest, ResponseAdapter<R> responseAdapter) implements SendStage<R> {

        @Override
        public R execute() throws IOException {
            Response rawResponse = client.newCall(rawRequest).execute();
            return responseAdapter.convert(rawResponse);
        }
    }
}
