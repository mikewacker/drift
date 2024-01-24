package io.github.mikewacker.drift.endpoint;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.client.JsonApiClient;
import io.github.mikewacker.drift.testing.server.TestServer;
import io.github.mikewacker.drift.testing.server.TestUndertowServer;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class UndertowArgsTest {

    @RegisterExtension
    private static final TestServer<?> server =
            TestUndertowServer.register("test", () -> UndertowArgsTest::handleRequest);

    private static ArgExtractor.Async<HttpServerExchange, Integer> argExtractor;

    @BeforeEach
    public void reset() {
        argExtractor = null;
    }

    @Test
    public void body() throws IOException {
        set(UndertowArgs.body(new TypeReference<>() {}));
        HttpOptional<Integer> maybeArg = JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<Integer>() {})
                .put(server.rootUrl())
                .body(1)
                .build()
                .execute();
        assertThat(maybeArg).hasValue(1);
    }

    @Test
    public void queryParam() throws IOException {
        set(UndertowArgs.queryParam("param", new TypeReference<>() {}));
        HttpOptional<Integer> maybeArg = JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<Integer>() {})
                .get(server.url("/path?param=1"))
                .build()
                .execute();
        assertThat(maybeArg).hasValue(1);
    }

    @Test
    public void badRequest_Body_DeserializeFailed() throws IOException {
        set(UndertowArgs.body(new TypeReference<>() {}));
        HttpOptional<Integer> maybeArg = JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<Integer>() {})
                .put(server.rootUrl())
                .body("a")
                .build()
                .execute();
        assertThat(maybeArg).isEmptyWithErrorCode(400);
    }

    @Test
    public void badRequest_QueryParam_Missing() throws IOException {
        set(UndertowArgs.queryParam("param", new TypeReference<>() {}));
        HttpOptional<Integer> maybeArg = JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<Integer>() {})
                .get(server.rootUrl())
                .build()
                .execute();
        assertThat(maybeArg).isEmptyWithErrorCode(400);
    }

    @Test
    public void badRequest_QueryParam_MultipleValues() throws IOException {
        set(UndertowArgs.queryParam("param", new TypeReference<>() {}));
        HttpOptional<Integer> maybeArg = JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<Integer>() {})
                .get(server.url("/path?param=1&param=2"))
                .build()
                .execute();
        assertThat(maybeArg).isEmptyWithErrorCode(400);
    }

    @Test
    public void badRequest_QueryParam_DeserializeFailed() throws IOException {
        set(UndertowArgs.queryParam("param", new TypeReference<>() {}));
        HttpOptional<Integer> maybeArg = JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<Integer>() {})
                .get(server.url("/path?param=a"))
                .build()
                .execute();
        assertThat(maybeArg).isEmptyWithErrorCode(400);
    }

    private static void set(ArgExtractor<HttpServerExchange, Integer> argExtractor) {
        UndertowArgsTest.argExtractor = argExtractor.async();
    }

    private static void set(ArgExtractor.Async<HttpServerExchange, Integer> argExtractor) {
        UndertowArgsTest.argExtractor = argExtractor;
    }

    /** Test {@code HttpHandler} that extracts an argument and sends it as the response. */
    private static void handleRequest(HttpServerExchange httpExchange) throws Exception {
        Sender.Value<Integer> sender = UndertowSender.JsonValue.create(httpExchange);
        argExtractor.tryExtract(httpExchange, maybeArg -> onArgExtracted(sender, maybeArg));
    }

    private static void onArgExtracted(Sender.Value<Integer> sender, HttpOptional<Integer> maybeArg) {
        sender.send(maybeArg);
    }
}
