package io.github.mikewacker.drift.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.testing.api.StubDispatcher;
import java.util.List;

/** Stub HTTP handler. */
final class StubHttpHandler implements JsonApiHandler<StubHttpExchange> {

    private static Integer statusCode = null;
    private static HttpOptional<Object> maybeValue = null;

    private final JsonApiHandler<StubHttpExchange> delegate;

    /** Gets the status code that was sent. */
    public static int getStatusCodeSent() {
        assertThat(statusCode).isNotNull();
        return statusCode;
    }

    /** Gets the value or error status code that was sent. */
    public static HttpOptional<Object> getValueOrErrorCodeSent() {
        assertThat(maybeValue).isNotNull();
        return maybeValue;
    }

    /** Creates a builder. */
    public static RouteStageBuilder<StubHttpExchange, StubHttpHandler> builder() {
        return new StubPreArgStateBuilder();
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
    public void handleRequest(StubHttpExchange httpExchange) throws Exception {
        statusCode = null;
        maybeValue = null;
        delegate.handleRequest(httpExchange);
    }

    private StubHttpHandler(JsonApiHandler<StubHttpExchange> delegate) {
        this.delegate = delegate;
    }

    /** Internal {@code PreArgStageBuilder} implementation. */
    private static final class StubPreArgStateBuilder
            extends GenericJsonApiHandler.PreArgStageBuilder<StubHttpExchange, StubHttpHandler> {

        @Override
        protected GenericJsonApiHandler.SenderFactory<StubHttpExchange, Sender.StatusCode>
                getStatusCodeSenderFactory() {
            return httpExchange -> (statusCode -> StubHttpHandler.statusCode = statusCode);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <V>
                GenericJsonApiHandler.SenderFactory<StubHttpExchange, Sender.Value<V>> getJsonValueSenderFactory() {
            return httpExchange -> (maybeValue -> StubHttpHandler.maybeValue = (HttpOptional<Object>) maybeValue);
        }

        @Override
        protected GenericJsonApiHandler.DispatcherFactory<StubHttpExchange> getDispatcherFactory() {
            return httpExchange -> StubDispatcher.get();
        }

        @Override
        protected GenericJsonApiHandler.HttpHandlerFactory<StubHttpExchange, StubHttpHandler> getHttpHandlerFactory() {
            return StubHttpHandler::new;
        }
    }
}
