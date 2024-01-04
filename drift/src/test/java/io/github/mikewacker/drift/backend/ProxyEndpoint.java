package io.github.mikewacker.drift.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.endpoint.HttpMethod;
import io.github.mikewacker.drift.endpoint.UndertowJsonApiHandler;
import io.github.mikewacker.drift.endpoint.UndertowRouter;
import io.undertow.server.HttpHandler;

/** Endpoint for {@code ProxyApi}. */
final class ProxyEndpoint {

    public static HttpHandler create() {
        ProxyApi proxyApi = ProxyService.create();

        HttpHandler statusCodeHandler = UndertowJsonApiHandler.builder().build(proxyApi::proxyStatusCode);
        HttpHandler textHandler =
                UndertowJsonApiHandler.builder(new TypeReference<String>() {}).build(proxyApi::proxyText);

        return UndertowRouter.builder()
                .addRoute(HttpMethod.GET, "/status-code", statusCodeHandler)
                .addRoute(HttpMethod.GET, "/text", textHandler)
                .build();
    }

    // static class
    private ProxyEndpoint() {}
}
