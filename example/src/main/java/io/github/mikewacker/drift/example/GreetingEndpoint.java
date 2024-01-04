package io.github.mikewacker.drift.example;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.endpoint.HttpMethod;
import io.github.mikewacker.drift.endpoint.UndertowArgs;
import io.github.mikewacker.drift.endpoint.UndertowJsonApiHandler;
import io.github.mikewacker.drift.endpoint.UndertowRouter;
import io.undertow.server.HttpHandler;
import java.util.function.Supplier;

/** HTTP handler for {@code GreetingApi}. */
public final class GreetingEndpoint {

    /** Creates an endpoint. */
    public static HttpHandler create(Supplier<String> salutationUrlProvider) {
        GreetingApi greetingApi = GreetingService.create(salutationUrlProvider);

        HttpHandler greetingHandler = UndertowJsonApiHandler.builder(new TypeReference<String>() {})
                .addArg(UndertowArgs.body(new TypeReference<String>() {}))
                .build(greetingApi::sendGreeting);
        HttpHandler healthHandler = UndertowJsonApiHandler.builder().build(greetingApi::healthCheck);

        return UndertowRouter.builder()
                .addRoute(HttpMethod.POST, "/greeting", greetingHandler)
                .addRoute(HttpMethod.GET, "/health", healthHandler)
                .build();
    }

    // static class
    private GreetingEndpoint() {}
}
