package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.List;

/** An HTTP handler for Undertow that invokes an API handler, using JSON as the wire format. */
public final class UndertowJsonApiHandler implements HttpHandler, AdaptedApiHandler<HttpServerExchange> {

    private final AdaptedApiHandler<HttpServerExchange> delegate;

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

    private UndertowJsonApiHandler(AdaptedApiHandler<HttpServerExchange> delegate) {
        this.delegate = delegate;
    }

    private static final class UndertowPreArgStageBuilder
            extends GenericAdaptedApiHandler.PreArgStageBuilder<HttpServerExchange, UndertowJsonApiHandler> {

        @Override
        protected GenericAdaptedApiHandler.SenderFactory<HttpServerExchange, Sender.StatusCode>
                getStatusCodeSenderFactory() {
            return UndertowSender.StatusCode::create;
        }

        @Override
        protected <V>
                GenericAdaptedApiHandler.SenderFactory<HttpServerExchange, Sender.Value<V>>
                        getJsonValueSenderFactory() {
            return UndertowSender.JsonValue::create;
        }

        @Override
        protected GenericAdaptedApiHandler.DispatcherFactory<HttpServerExchange> getDispatcherFactory() {
            return UndertowDispatcher::create;
        }

        @Override
        protected GenericAdaptedApiHandler.HttpHandlerFactory<HttpServerExchange, UndertowJsonApiHandler>
                getHttpHandlerFactory() {
            return UndertowJsonApiHandler::new;
        }

        private UndertowPreArgStageBuilder() {}
    }
}
