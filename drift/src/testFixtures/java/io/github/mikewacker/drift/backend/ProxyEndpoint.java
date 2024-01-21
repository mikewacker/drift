package io.github.mikewacker.drift.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.endpoint.HttpMethod;
import io.github.mikewacker.drift.endpoint.UndertowJsonApiHandler;
import io.github.mikewacker.drift.endpoint.UndertowRouter;
import io.undertow.server.HttpHandler;

/** Endpoint for {@code ProxyApi}. */
final class ProxyEndpoint {

    /** Creates an HTTP handler for {@code ProxyApi}. */
    public static HttpHandler create() {
        ProxyApi proxyApi = ProxyService.create();

        HttpHandler statusCodeHandler = UndertowJsonApiHandler.builder()
                .route(HttpMethod.GET, "/status-code")
                .statusCodeResponse()
                .apiHandler(proxyApi::proxyStatusCode)
                .build();
        HttpHandler textHandler = UndertowJsonApiHandler.builder()
                .route(HttpMethod.GET, "/text")
                .jsonResponse(new TypeReference<String>() {})
                .apiHandler(proxyApi::proxyText)
                .build();

        return UndertowRouter.builder()
                .addRoute(HttpMethod.GET, "/status-code", statusCodeHandler)
                .addRoute(HttpMethod.GET, "/text", textHandler)
                .build();
    }

    // static class
    private ProxyEndpoint() {}
}
