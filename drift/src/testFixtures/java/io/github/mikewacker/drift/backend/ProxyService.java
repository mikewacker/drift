package io.github.mikewacker.drift.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.testing.server.TestServer;

/** Service for {@code ProxyApi} that uses a {@code BackendDispatcher}. */
final class ProxyService implements ProxyApi {

    private final BackendDispatcher backendDispatcher = BackendDispatcher.create();

    /** Creates a service for {@code ProxyApi}. */
    public static ProxyApi create() {
        return new ProxyService();
    }

    @Override
    public void proxyStatusCode(Sender.StatusCode sender, Dispatcher dispatcher) {
        backendDispatcher
                .requestBuilder()
                .statusCodeResponse()
                .get(TestServer.get("backend").rootUrl())
                .build()
                .dispatch(sender, dispatcher, this::onStatusCodeReceived);
    }

    @Override
    public void proxyText(Sender.Value<String> sender, Dispatcher dispatcher) {
        backendDispatcher
                .requestBuilder()
                .jsonResponse(new TypeReference<String>() {})
                .get(TestServer.get("backend").rootUrl())
                .build()
                .dispatch(sender, dispatcher, this::onTextReceived);
    }

    /** Sends the status code that was received from the backend server. */
    private void onStatusCodeReceived(Sender.StatusCode sender, int statusCode, Dispatcher dispatcher) {
        sender.send(statusCode);
    }

    /** Sends the text or error status code that was received from the backend server. */
    private void onTextReceived(Sender.Value<String> sender, HttpOptional<String> maybeText, Dispatcher dispatcher) {
        sender.send(maybeText);
    }

    private ProxyService() {}
}
