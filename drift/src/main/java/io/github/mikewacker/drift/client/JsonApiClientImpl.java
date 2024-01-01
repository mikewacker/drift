package io.github.mikewacker.drift.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/** Internal {@code JsonApiClient} implementation. */
final class JsonApiClientImpl extends AbstractOkHttpJsonApiClient implements JsonApiClient {

    private static final JsonApiClientImpl instance = new JsonApiClientImpl();

    private static final OkHttpClient client = new OkHttpClient();

    /** Corresponds to {@code JsonApiClient#requestBuilder()}. */
    public static UrlStageRequestBuilder<SendStage<Integer>> requestBuilder() {
        return instance.requestBuilder(SendStageImpl::new);
    }

    /** Corresponds to {@code JsonApiClient#requestBuilder(TypeReference)}. */
    public static <V> UrlStageRequestBuilder<SendStage<HttpOptional<V>>> requestBuilder(
            TypeReference<V> responseValueTypeRef) {
        return instance.requestBuilder(SendStageImpl::new, responseValueTypeRef);
    }

    private JsonApiClientImpl() {}

    /** Internal {@code SendStage} implementation. */
    private static final class SendStageImpl<R> implements SendStage<R> {

        private final Request rawRequest;
        private final ResponseAdapter<R> responseAdapter;

        private final AtomicBoolean wasSent = new AtomicBoolean(false);

        @Override
        public R execute() throws IOException {
            if (wasSent.getAndSet(true)) {
                throw new IllegalStateException("request was already sent");
            }

            Response rawResponse = client.newCall(rawRequest).execute();
            return responseAdapter.convert(rawResponse);
        }

        private SendStageImpl(Request rawRequest, ResponseAdapter<R> responseAdapter) {
            this.rawRequest = rawRequest;
            this.responseAdapter = responseAdapter;
        }
    }
}
