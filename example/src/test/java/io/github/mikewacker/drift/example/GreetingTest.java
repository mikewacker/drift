package io.github.mikewacker.drift.example;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.client.JsonApiClient;
import io.github.mikewacker.drift.testing.server.MockServer;
import io.github.mikewacker.drift.testing.server.TestServer;
import io.github.mikewacker.drift.testing.server.TestUndertowServer;
import io.undertow.server.HttpHandler;
import java.io.IOException;
import java.util.function.Supplier;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class GreetingTest {

    @RegisterExtension
    private static final TestServer<?> greetingServer =
            TestUndertowServer.register("greeting", GreetingTest::createGreetingHandler);

    @RegisterExtension
    private static final MockServer salutationServer = MockServer.register("salutation");

    @Test
    public void helloWorld() throws IOException {
        salutationServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json").setBody("\"Hello\""));
        HttpOptional<String> maybeGreeting = JsonApiClient.requestBuilder(new TypeReference<String>() {})
                .post(greetingServer.url("/greeting"))
                .body("world")
                .build()
                .execute();
        assertThat(maybeGreeting).hasValue("Hello, world!");
    }

    @Test
    public void healthCheck() throws IOException {
        int statusCode = JsonApiClient.requestBuilder()
                .get(greetingServer.url("/health"))
                .build()
                .execute();
        assertThat(statusCode).isEqualTo(200);
    }

    private static HttpHandler createGreetingHandler() {
        // Could also call "salutationServer.rootUrl()", but this works outside this class as well.
        Supplier<String> salutationUrlProvider =
                () -> TestServer.get("salutation").rootUrl();
        return GreetingEndpoint.create(salutationUrlProvider);
    }
}
