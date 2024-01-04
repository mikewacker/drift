package io.github.mikewacker.drift.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import io.undertow.server.HttpHandler;

/** Test {@code HttpHandler} for {@code Greeter}. */
final class GreetingHandler {

    public static HttpHandler create() {
        HttpHandler greetingHandler = UndertowJsonApiHandler.builder(new TypeReference<String>() {})
                .addArg(UndertowArgs.body(new TypeReference<String>() {}))
                .build(Greeter::sendGreeting);
        HttpHandler healthHandler = UndertowJsonApiHandler.builder().build(Greeter::healthCheck);

        return UndertowRouter.builder()
                .addRoute(HttpMethod.POST, "/greeting", greetingHandler)
                .addRoute(HttpMethod.GET, "/health", healthHandler)
                .build();
    }

    // static class
    private GreetingHandler() {}
}
