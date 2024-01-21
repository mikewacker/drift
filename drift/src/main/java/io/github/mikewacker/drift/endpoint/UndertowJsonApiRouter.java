package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.Optional;

/**
 * An HTTP handler for Undertow that routes each HTTP request to an HTTP handler for an API handler.
 * The HTTP handler for an API handler is an {@link UndertowJsonApiHandler}.
 */
public final class UndertowJsonApiRouter extends GenericJsonApiRouter<HttpServerExchange> implements HttpHandler {

    /**
     * Creates a router.
     *
     * @param httpHandlers a list of HTTP handlers for the API handlers
     * @return an {@link HttpHandler} that acts as a router
     */
    public static HttpHandler of(UndertowJsonApiHandler... httpHandlers) {
        UndertowJsonApiRouter router = new UndertowJsonApiRouter();
        for (UndertowJsonApiHandler httpHandler : httpHandlers) {
            router.addHttpHandler(httpHandler);
        }
        return router;
    }

    @Override
    protected Sender createErrorCodeSender(HttpServerExchange httpExchange) {
        return UndertowSender.StatusCode.create(httpExchange);
    }

    @Override
    protected Optional<HttpMethod> tryGetMethod(HttpServerExchange httpExchange) {
        String rawMethod = httpExchange.getRequestMethod().toString();
        try {
            HttpMethod method = HttpMethod.valueOf(rawMethod);
            return Optional.of(method);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    protected String getRelativePath(HttpServerExchange httpExchange) {
        return httpExchange.getRelativePath();
    }

    private UndertowJsonApiRouter() {}
}
