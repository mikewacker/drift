package io.github.mikewacker.drift.endpoint;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.client.JsonApiClient;
import io.github.mikewacker.drift.testing.server.TestServer;
import io.github.mikewacker.drift.testing.server.TestUndertowServer;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class UndertowJsonApiTest {

    @RegisterExtension
    private static final TestServer<?> server = TestUndertowServer.register("test", GreeterEndpoint::create);

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
}
