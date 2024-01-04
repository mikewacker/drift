package io.github.mikewacker.drift.endpoint;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.client.JsonApiClient;
import io.github.mikewacker.drift.testing.server.TestServer;
import io.github.mikewacker.drift.testing.server.TestUndertowServer;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class UndertowJsonApiTest {

    @RegisterExtension
    private static final TestServer<?> server = TestUndertowServer.register("test", GreetingHandler::create);

    @Test
    public void exchange_StatusCode() throws IOException {
        int statusCode = JsonApiClient.requestBuilder()
                .get(server.url("/health"))
                .build()
                .execute();
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void exchange_JsonValue() throws IOException {
        HttpOptional<String> maybeGreeting = JsonApiClient.requestBuilder(new TypeReference<String>() {})
                .post(server.url("/greeting"))
                .body("world")
                .build()
                .execute();
        assertThat(maybeGreeting).hasValue("Hello, world!");
    }

    @Test
    public void errorCode_NotFound() throws IOException {
        int statusCode =
                JsonApiClient.requestBuilder().get(server.url("/dne")).build().execute();
        assertThat(statusCode).isEqualTo(404);
    }

    @Test
    public void errorCode_MethodNotAllowed() throws IOException {
        int statusCode = JsonApiClient.requestBuilder()
                .get(server.url("/greeting"))
                .build()
                .execute();
        assertThat(statusCode).isEqualTo(405);
    }

    @Test
    public void error_AddRouteTwice() {
        UndertowRouter.Builder routerBuilder =
                UndertowRouter.builder().addRoute(HttpMethod.GET, "/path", HttpServerExchange::endExchange);
        assertThatThrownBy(() -> routerBuilder.addRoute(HttpMethod.GET, "/path", HttpServerExchange::endExchange))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("route already exists");
    }
}
