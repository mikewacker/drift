package io.github.mikewacker.drift.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import io.undertow.server.HttpHandler;

/** Endpoint for {@code Greeter}. */
public final class GreeterEndpoint {

    /** Creates an HTTP handler for the {@code Greeter}. */
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
    private GreeterEndpoint() {}
}
