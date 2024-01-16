package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.List;

/** An HTTP handler for Undertow that invokes an API handler, using JSON as the wire format. */
public final class UndertowJsonApiHandler implements HttpHandler, JsonApiHandler<HttpServerExchange> {

    private final JsonApiHandler<HttpServerExchange> delegate;

    /**
     * Creates a builder for an HTTP handler that invokes an API handler.
     *
     * @return a builder at the route stage
     */
    public static RouteStageBuilder<HttpServerExchange, UndertowJsonApiHandler> builder() {
        return new UndertowPreArgStageBuilder();
    }

    @Override
    public HttpMethod getMethod() {
        return delegate.getMethod();
    }

    @Override
    public List<String> getRelativePathSegments() {
        return delegate.getRelativePathSegments();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        try {
            delegate.handleRequest(exchange);
        } catch (TunneledException e) {
            throw e.getCause();
        }
    }

    private UndertowJsonApiHandler(JsonApiHandler<HttpServerExchange> delegate) {
        this.delegate = delegate;
    }

    private static final class UndertowPreArgStageBuilder
            extends GenericJsonApiHandler.PreArgStageBuilder<HttpServerExchange, UndertowJsonApiHandler> {

        @Override
        protected GenericJsonApiHandler.SenderFactory<HttpServerExchange, Sender.StatusCode>
                getStatusCodeSenderFactory() {
            return UndertowSender.StatusCode::create;
        }

        @Override
        protected <V>
                GenericJsonApiHandler.SenderFactory<HttpServerExchange, Sender.Value<V>> getJsonValueSenderFactory() {
            return UndertowSender.JsonValue::create;
        }

        @Override
        protected GenericJsonApiHandler.DispatcherFactory<HttpServerExchange> getDispatcherFactory() {
            return UndertowDispatcher::create;
        }

        @Override
        protected GenericJsonApiHandler.HttpHandlerFactory<HttpServerExchange, UndertowJsonApiHandler>
                getHttpHandlerFactory() {
            return UndertowJsonApiHandler::new;
        }

        private UndertowPreArgStageBuilder() {}
    }
}
