package io.github.mikewacker.drift.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.endpoint.HttpMethod;
import io.github.mikewacker.drift.endpoint.UndertowJsonApiHandler;
import io.github.mikewacker.drift.endpoint.UndertowJsonApiRouter;
import io.undertow.server.HttpHandler;

/** Endpoint for {@code ProxyApi}. */
final class ProxyEndpoint {

    /** Creates an HTTP handler for {@code ProxyApi}. */
    public static HttpHandler create() {
        ProxyApi proxyApi = ProxyService.create();
        return UndertowJsonApiRouter.of(
                UndertowJsonApiHandler.builder()
                        .route(HttpMethod.GET, "/status-code")
                        .statusCodeResponse()
                        .apiHandler(proxyApi::proxyStatusCode)
                        .build(),
                UndertowJsonApiHandler.builder()
                        .route(HttpMethod.GET, "/text")
                        .jsonResponse(new TypeReference<String>() {})
                        .apiHandler(proxyApi::proxyText)
                        .build());
    }

    // static class
    private ProxyEndpoint() {}
}
