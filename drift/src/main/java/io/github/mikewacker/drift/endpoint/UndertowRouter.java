package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * An HTTP handler for Undertow that routes HTTP requests to HTTP handlers for API handlers.
 * The router uses the relative path of the HTTP request.
 * <p>
 * A route can be created to any HTTP handler, not just an HTTP handler for an API handler.
 */
public class UndertowRouter implements HttpHandler {

    private final Map<Route, HttpHandler> router;
    private final Set<String> relativePaths;

    /**
     * Creates a builder for a router.
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Sender errorCodeSender = UndertowSender.StatusCode.create(exchange);
        HttpMethod method;
        try {
            method = HttpMethod.valueOf(exchange.getRequestMethod().toString());
        } catch (IllegalArgumentException e) {
            errorCodeSender.sendErrorCode(StatusCodes.BAD_REQUEST);
            return;
        }

        String relativePath = exchange.getRelativePath();
        Route route = new Route(method, relativePath);
        Optional<HttpHandler> maybeHandler = Optional.ofNullable(router.get(route));
        if (maybeHandler.isEmpty()) {
            int errorCode =
                    relativePaths.contains(relativePath) ? StatusCodes.METHOD_NOT_ALLOWED : StatusCodes.NOT_FOUND;
            errorCodeSender.sendErrorCode(errorCode);
            return;
        }
        HttpHandler handler = maybeHandler.get();

        handler.handleRequest(exchange);
    }

    private UndertowRouter(Map<Route, HttpHandler> router, Set<String> relativePaths) {
        this.router = router;
        this.relativePaths = relativePaths;
    }

    /** Builder for a router. */
    public static final class Builder {

        private final Map<Route, HttpHandler> router = new HashMap<>();
        private final Set<String> relativePaths = new HashSet<>();

        /**
         * Adds a route.
         *
         * @param method the HTTP method of the route
         * @param relativePath the relative path of the route
         * @param handler the HTTP handler that the route leads to
         * @return this builder
         */
        public Builder addRoute(HttpMethod method, String relativePath, HttpHandler handler) {
            relativePath = relativePath.replaceFirst("^/?", "/");
            Route route = new Route(method, relativePath);
            HttpHandler currentHandler = router.putIfAbsent(route, handler);
            if (currentHandler != null) {
                throw new IllegalStateException("route already exists");
            }

            relativePaths.add(relativePath);
            return this;
        }

        /**
         * Builds the router.
         *
         * @return the router
         */
        public UndertowRouter build() {
            return new UndertowRouter(Map.copyOf(router), Set.copyOf(relativePaths));
        }

        private Builder() {}
    }

    /** Route defined by an HTTP method and a relative path. */
    private record Route(HttpMethod method, String relativePath) {}
}
