package io.github.mikewacker.drift.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/** An HTTP handler for Undertow that invokes an API handler for a JSON API. */
public final class UndertowJsonApiHandler implements HttpHandler, AdaptedApiHandler<HttpServerExchange> {

    private final AdaptedApiHandler<HttpServerExchange> delegate;

    /**
     * Creates a builder for an HTTP handler that invokes an API handler that only sends an HTTP status code.
     *
     * @return a builder for an HTTP handler that invokes an API handler with zero or more arguments
     */
    public static ZeroArgBuilder<HttpServerExchange, UndertowJsonApiHandler, Sender.StatusCode> builder() {
        return GenericAdaptedApiHandler.builder(
                UndertowSender.StatusCode::create, UndertowDispatcher::create, UndertowJsonApiHandler::new);
    }

    /**
     * Creates a builder for an HTTP handler that invokes an API handler that sends an {@link HttpOptional} value.
     *
     * @param responseValueTypeRef a {@link TypeReference} for the response value
     * @return a builder for an HTTP handler that invokes an API handler with zero or more arguments
     * @param <V> the type of the response value
     */
    public static <V> ZeroArgBuilder<HttpServerExchange, UndertowJsonApiHandler, Sender.Value<V>> builder(
            TypeReference<V> responseValueTypeRef) {
        return GenericAdaptedApiHandler.builder(
                UndertowSender.JsonValue::create, UndertowDispatcher::create, UndertowJsonApiHandler::new);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            delegate.handleRequest(exchange);
        } catch (TunneledException e) {
            throw e.getCause();
        }
    }

    private UndertowJsonApiHandler(AdaptedApiHandler<HttpServerExchange> delegate) {
        this.delegate = delegate;
    }
}
