package io.github.mikewacker.drift.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.client.JsonApiClient;
import io.github.mikewacker.drift.testing.api.Assertions;
import io.github.mikewacker.drift.testing.server.TestServer;
import io.github.mikewacker.drift.testing.server.TestUndertowServer;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class UndertowSenderTest {

    @RegisterExtension
    private static final TestServer<?> server =
            TestUndertowServer.register("test", () -> UndertowSenderTest::handleRequest);

    @Test
    public void send_StatusCode_Ok() throws IOException {
        int statusCode = executeStatusCodeRequest("/status-code/ok");
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void send_StatusCode_Forbidden() throws IOException {
        int statusCode = executeStatusCodeRequest("/status-code/forbidden");
        assertThat(statusCode).isEqualTo(403);
    }

    @Test
    public void send_StatusCode_SendTwice() throws IOException {
        int statusCode = executeStatusCodeRequest("/status-code/send-twice");
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void send_JsonValue_Ok() throws IOException {
        HttpOptional<String> maybeText = executeTextRequest("/text/ok");
        Assertions.assertThat(maybeText).hasValue("test");
    }

    @Test
    public void send_JsonValue_Forbidden() throws IOException {
        HttpOptional<String> maybeText = executeTextRequest("/text/forbidden");
        Assertions.assertThat(maybeText).isEmptyWithErrorCode(403);
    }

    @Test
    public void send_JsonValue_SendTwice() throws IOException {
        HttpOptional<String> maybeText = executeTextRequest("/text/send-twice");
        Assertions.assertThat(maybeText).hasValue("first");
    }

    private static int executeStatusCodeRequest(String path) throws IOException {
        return JsonApiClient.requestBuilder()
                .statusCodeResponse()
                .get(server.url(path))
                .build()
                .execute();
    }

    private static HttpOptional<String> executeTextRequest(String path) throws IOException {
        return JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<String>() {})
                .get(server.url(path))
                .build()
                .execute();
    }

    /** Test {@code HttpHandler} that uses an {@code UndertowSender}. */
    private static void handleRequest(HttpServerExchange httpExchange) {
        Sender.StatusCode statusCodeSender = UndertowSender.StatusCode.create(httpExchange);
        Sender.Value<String> valueSender = UndertowSender.JsonValue.create(httpExchange);
        switch (httpExchange.getRequestPath()) {
            case "/status-code/ok" -> statusCodeSender.sendOk();
            case "/status-code/forbidden" -> statusCodeSender.sendErrorCode(StatusCodes.FORBIDDEN);
            case "/status-code/send-twice" -> sendStatusCodeTwice(statusCodeSender);
            case "/text/ok" -> valueSender.sendValue("test");
            case "/text/forbidden" -> valueSender.sendErrorCode(StatusCodes.FORBIDDEN);
            case "/text/send-twice" -> sendValueTwice(valueSender);
            default -> statusCodeSender.sendErrorCode(StatusCodes.NOT_FOUND);
        }
    }

    private static void sendStatusCodeTwice(Sender.StatusCode sender) {
        sender.sendOk();
        sender.sendErrorCode(403);
    }

    private static void sendValueTwice(Sender.Value<String> sender) {
        sender.sendValue("first");
        sender.sendValue("second");
    }
}
