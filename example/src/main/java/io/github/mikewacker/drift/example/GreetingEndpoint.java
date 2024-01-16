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

        HttpHandler greetingHandler = UndertowJsonApiHandler.builder()
                .route(HttpMethod.POST, "/greeting")
                .jsonResponse(new TypeReference<String>() {})
                .arg(UndertowArgs.body(new TypeReference<String>() {}))
                .apiHandler(greetingApi::sendGreeting)
                .build();
        HttpHandler healthHandler = UndertowJsonApiHandler.builder()
                .route(HttpMethod.GET, "/health")
                .statusCodeResponse()
                .apiHandler(greetingApi::healthCheck)
                .build();

        return UndertowRouter.builder()
                .addRoute(HttpMethod.POST, "/greeting", greetingHandler)
                .addRoute(HttpMethod.GET, "/health", healthHandler)
                .build();
    }

    // static class
    private GreetingEndpoint() {}
}
