package io.github.mikewacker.drift.endpoint;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.client.JsonApiClient;
import io.github.mikewacker.drift.testing.server.TestServer;
import io.github.mikewacker.drift.testing.server.TestUndertowServer;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class UndertowDispatcherTest {

    @RegisterExtension
    private static final TestServer<?> server =
            TestUndertowServer.register("test", () -> UndertowDispatcherTest::handleRequest);

    @Test
    public void dispatch() throws IOException {
        HttpOptional<String> maybeValue = executeRequest("/dispatch/ok");
        assertThat(maybeValue).hasValue("test");
    }

    @Test
    public void dispatch_UncaughtExceptionInHandler() throws IOException {
        HttpOptional<String> maybeValue = executeRequest("/dispatch/error");
        assertThat(maybeValue).isEmptyWithErrorCode(500);
    }

    @Test
    public void dispatched_Worker() throws IOException {
        HttpOptional<String> maybeValue = executeRequest("/dispatched/ok/worker");
        assertThat(maybeValue).hasValue("test");
    }

    @Test
    public void dispatched_IoThread() throws IOException {
        HttpOptional<String> maybeValue = executeRequest("/dispatched/ok/io-thread");
        assertThat(maybeValue).hasValue("test");
    }

    @Test
    public void dispatched_UncaughtExceptionInHandler() throws IOException {
        HttpOptional<String> maybeValue = executeRequest("/dispatched/error");
        assertThat(maybeValue).isEmptyWithErrorCode(500);
    }

    private static HttpOptional<String> executeRequest(String path) throws IOException {
        return JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<String>() {})
                .get(server.url(path))
                .build()
                .execute();
    }

    /** Test {@code HttpHandler} that uses an {@code UndertowDispatcher}. */
    private static void handleRequest(HttpServerExchange httpExchange) {
        Sender.Value<String> sender = UndertowSender.JsonValue.create(httpExchange);
        Dispatcher dispatcher = UndertowDispatcher.create(httpExchange);
        if (!dispatcher.isInIoThread()) {
            sender.sendErrorCode(418);
            return;
        }

        switch (httpExchange.getRequestPath()) {
            case "/dispatch/ok" -> dispatcher.dispatch(sender, UndertowDispatcherTest::workerHandler);
            case "/dispatch/error" -> dispatcher.dispatch(sender, UndertowDispatcherTest::badHandler);
            case "/dispatched/ok/worker" -> dispatchManually(
                    dispatcher.getWorker(), sender, dispatcher, UndertowDispatcherTest::workerHandler);
            case "/dispatched/ok/io-thread" -> dispatchManually(
                    dispatcher.getIoThread(), sender, dispatcher, UndertowDispatcherTest::ioThreadHandler);
            case "/dispatched/error" -> dispatchManually(
                    dispatcher.getWorker(), sender, dispatcher, UndertowDispatcherTest::badHandler);
            default -> sender.sendErrorCode(StatusCodes.NOT_FOUND);
        }
    }

    private static void dispatchManually(
            Executor executor,
            Sender.Value<String> sender,
            Dispatcher dispatcher,
            ApiHandler.ZeroArg<Sender.Value<String>> handler) {
        Dispatcher.DispatchedHandler dispatchedHandler = () -> handler.handleRequest(sender, dispatcher);
        executor.execute(() -> dispatcher.executeHandler(dispatchedHandler));
        dispatcher.dispatched();
    }

    private static void workerHandler(Sender.Value<String> sender, Dispatcher dispatcher) {
        if (dispatcher.isInIoThread()) {
            sender.sendErrorCode(418);
            return;
        }

        sender.sendValue("test");
    }

    private static void ioThreadHandler(Sender.Value<String> sender, Dispatcher dispatcher) {
        if (!dispatcher.isInIoThread()) {
            sender.sendErrorCode(418);
            return;
        }

        sender.sendValue("test");
    }

    private static void badHandler(Sender.Value<String> sender, Dispatcher dispatcher) {
        throw new RuntimeException();
    }
}
