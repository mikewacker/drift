package io.github.mikewacker.drift.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import io.undertow.server.HttpHandler;

/** Test {@code HttpHandler} for {@code Greeter}. */
final class GreetingHandler {

    public static HttpHandler create() {
        HttpHandler greetingHandler = UndertowJsonApiHandler.builder()
                .route(HttpMethod.POST, "/greeting")
                .jsonResponse(new TypeReference<String>() {})
                .arg(UndertowArgs.body(new TypeReference<String>() {}))
                .apiHandler(Greeter::sendGreeting)
                .build();
        HttpHandler healthHandler = UndertowJsonApiHandler.builder()
                .route(HttpMethod.GET, "/health")
                .statusCodeResponse()
                .apiHandler(Greeter::healthCheck)
                .build();

        return UndertowRouter.builder()
                .addRoute(HttpMethod.POST, "/greeting", greetingHandler)
                .addRoute(HttpMethod.GET, "/health", healthHandler)
                .build();
    }

    // static class
    private GreetingHandler() {}
}
