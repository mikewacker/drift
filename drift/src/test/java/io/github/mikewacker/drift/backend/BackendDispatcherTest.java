package io.github.mikewacker.drift.backend;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.client.JsonApiClient;
import io.github.mikewacker.drift.testing.server.MockServer;
import io.github.mikewacker.drift.testing.server.TestServer;
import io.github.mikewacker.drift.testing.server.TestUndertowServer;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class BackendDispatcherTest {

    @RegisterExtension
    private static final TestServer<?> frontendServer = TestUndertowServer.register("frontend", ProxyEndpoint::create);

    @RegisterExtension
    private static final MockServer backendServer = MockServer.register("backend");

    @Test
    public void backendRequest_StatusCodeResponse() throws IOException {
        backendServer.enqueue(new MockResponse());
        int statusCode = executeRequestWithStatusCodeResponse();
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void backendRequest_JsonValueResponse() throws IOException {
        backendServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json").setBody("\"test\""));
        HttpOptional<String> maybeText = executeRequestWithJsonValueResponse();
        assertThat(maybeText).hasValue("test");
    }

    @Test
    public void backendRequest_RequestFails() throws IOException {
        backendServer.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START));
        int statusCode = executeRequestWithStatusCodeResponse();
        assertThat(statusCode).isEqualTo(502);
    }

    @Test
    public void backendRequest_DeserializeResponseFails() throws IOException {
        backendServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json").setBody("{"));
        HttpOptional<String> maybeText = executeRequestWithJsonValueResponse();
        assertThat(maybeText).isEmptyWithErrorCode(502);
    }

    private static int executeRequestWithStatusCodeResponse() throws IOException {
        return JsonApiClient.requestBuilder()
                .statusCodeResponse()
                .get(frontendServer.url("/status-code"))
                .build()
                .execute();
    }

    private static HttpOptional<String> executeRequestWithJsonValueResponse() throws IOException {
        return JsonApiClient.requestBuilder()
                .jsonResponse(new TypeReference<String>() {})
                .get(frontendServer.url("/text"))
                .build()
                .execute();
    }
}
