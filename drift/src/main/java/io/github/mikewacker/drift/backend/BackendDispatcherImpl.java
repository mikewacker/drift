package io.github.mikewacker.drift.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.client.AbstractOkHttpJsonApiClient;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/** Internal {@code BackendDispatcher} implementation. */
final class BackendDispatcherImpl extends AbstractOkHttpJsonApiClient implements BackendDispatcher {

    private final DispatcherOkHttpClientProvider clientProvider = DispatcherOkHttpClientProvider.create();

    /** Corresponds to {@code BackendDispatcher#create()}. */
    public static BackendDispatcher create() {
        return new BackendDispatcherImpl();
    }

    @Override
    public UrlStageRequestBuilder<DispatchStage<Integer>> requestBuilder() {
        return requestBuilder(DispatchStageImpl::new);
    }

    @Override
    public <V> UrlStageRequestBuilder<DispatchStage<HttpOptional<V>>> requestBuilder(
            TypeReference<V> responseValueTypeRef) {
        return requestBuilder(DispatchStageImpl::new, responseValueTypeRef);
    }

    private BackendDispatcherImpl() {}

    /** Internal {@code DispatchStage} implementation. */
    private final class DispatchStageImpl<R> implements DispatchStage<R> {

        private final Request rawRequest;
        private final ResponseAdapter<R> responseAdapter;

        private final AtomicBoolean wasSent = new AtomicBoolean(false);

        @Override
        public <S extends Sender> void dispatch(S sender, Dispatcher dispatcher, ApiHandler.OneArg<S, R> callback) {
            if (wasSent.getAndSet(true)) {
                throw new IllegalStateException("request was already sent");
            }

            OkHttpClient client = clientProvider.get(dispatcher);
            AdaptedCallback<S, R> adaptedCallback =
                    new AdaptedCallback<>(sender, responseAdapter, dispatcher, callback);
            client.newCall(rawRequest).enqueue(adaptedCallback);
            dispatcher.dispatched();
        }

        private DispatchStageImpl(Request rawRequest, ResponseAdapter<R> responseAdapter) {
            this.rawRequest = rawRequest;
            this.responseAdapter = responseAdapter;
        }
    }

    /**
     * Adapts an {@code ApiHandler.OneArg} to a {@code Callback}.
     * <p>
     * A lambda function can adapt {@code ApiHandler}'s with more arguments to a {@code ApiHandler.OneArg}.
     */
    private record AdaptedCallback<S extends Sender, R>(
            S sender, ResponseAdapter<R> responseAdapter, Dispatcher dispatcher, ApiHandler.OneArg<S, R> callback)
            implements Callback {

        @Override
        public void onResponse(Call call, Response rawResponse) {
            dispatcher.executeHandler(() -> onRawResponseReceived(rawResponse));
        }

        @Override
        public void onFailure(Call call, IOException e) {
            dispatcher.executeHandler(this::onFailure);
        }

        /** Called when the raw {@code Response} is received. */
        private void onRawResponseReceived(Response rawResponse) throws Exception {
            R response;
            try {
                response = responseAdapter.convert(rawResponse);
            } catch (Exception e) {
                onFailure();
                return;
            }

            callback.handleRequest(sender, response, dispatcher);
        }

        /** Called when an error occurs. */
        private void onFailure() {
            sender.sendErrorCode(502);
        }
    }
}
