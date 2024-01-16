package io.github.mikewacker.drift.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.testing.api.FakeSender;
import io.github.mikewacker.drift.testing.api.StubDispatcher;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class GenericJsonApiHandlerTest {

    private static FakeSender.Value<Object> sender;

    @BeforeEach
    public void reset() {
        sender = null;
    }

    @Test
    public void handleHttpRequest_ZeroArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .apiHandler(Adder::add0)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create(null, null, null, null, null, null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(0));
    }

    @Test
    public void handleHttpRequest_OneArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .apiHandler(Adder::add1)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", null, null, null, null, null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(1));
    }

    @Test
    public void handleHttpRequest_TwoArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .arg(StubHttpExchange::maybeAddend2)
                .apiHandler(Adder::add2)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", null, null, null, null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(3));
    }

    @Test
    public void handleHttpRequest_ThreeArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .arg(StubHttpExchange::maybeAddend2)
                .arg(StubHttpExchange::maybeAddend3)
                .apiHandler(Adder::add3)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", null, null, null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(6));
    }

    @Test
    public void handleHttpRequest_FourArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .arg(StubHttpExchange::maybeAddend2)
                .arg(StubHttpExchange::maybeAddend3)
                .arg(StubHttpExchange::maybeAddend4)
                .apiHandler(Adder::add4)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", "4", null, null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(10));
    }

    @Test
    public void handleHttpRequest_FiveArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .arg(StubHttpExchange::maybeAddend2)
                .arg(StubHttpExchange::maybeAddend3)
                .arg(StubHttpExchange::maybeAddend4)
                .arg(StubHttpExchange::maybeAddend5)
                .apiHandler(Adder::add5)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", "4", "5", null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(15));
    }

    @Test
    public void handleHttpRequest_SixArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .arg(StubHttpExchange::maybeAddend2)
                .arg(StubHttpExchange::maybeAddend3)
                .arg(StubHttpExchange::maybeAddend4)
                .arg(StubHttpExchange::maybeAddend5)
                .arg(StubHttpExchange::maybeAddend6)
                .apiHandler(Adder::add6)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", "4", "5", "6", null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(21));
    }

    @Test
    public void handleHttpRequest_SevenArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .arg(StubHttpExchange::maybeAddend2)
                .arg(StubHttpExchange::maybeAddend3)
                .arg(StubHttpExchange::maybeAddend4)
                .arg(StubHttpExchange::maybeAddend5)
                .arg(StubHttpExchange::maybeAddend6)
                .arg(StubHttpExchange::maybeAddend7)
                .apiHandler(Adder::add7)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", "4", "5", "6", "7", null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(28));
    }

    @Test
    public void handleHttpRequest_EightArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .arg(StubHttpExchange::maybeAddend2)
                .arg(StubHttpExchange::maybeAddend3)
                .arg(StubHttpExchange::maybeAddend4)
                .arg(StubHttpExchange::maybeAddend5)
                .arg(StubHttpExchange::maybeAddend6)
                .arg(StubHttpExchange::maybeAddend7)
                .arg(StubHttpExchange::maybeAddend8)
                .apiHandler(Adder::add8)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", "4", "5", "6", "7", "8");
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(36));
    }

    @Test
    public void handleHttpRequest_ArgExtractorFails() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubHttpExchange::maybeAddend1)
                .apiHandler(Adder::add1)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.create("a", null, null, null, null, null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.empty(400));
    }

    @Test
    public void getRoute() {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .apiHandler(Adder::add0)
                .build();
        assertThat(httpHandler.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpHandler.getRelativePathSegments()).containsExactly("some", "path");
    }

    /** Stub HTTP handler. */
    private static final class StubHttpHandler implements JsonApiHandler<StubHttpExchange> {

        private final JsonApiHandler<StubHttpExchange> delegate;

        public static RouteStageBuilder<StubHttpExchange, StubHttpHandler> builder() {
            return new StubPreArgStageBuilder();
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
        public void handleRequest(StubHttpExchange exchange) throws Exception {
            delegate.handleRequest(exchange);
        }

        private StubHttpHandler(JsonApiHandler<StubHttpExchange> delegate) {
            this.delegate = delegate;
        }

        private static final class StubPreArgStageBuilder
                extends GenericJsonApiHandler.PreArgStageBuilder<StubHttpExchange, StubHttpHandler> {

            @Override
            protected GenericJsonApiHandler.SenderFactory<StubHttpExchange, Sender.StatusCode>
                    getStatusCodeSenderFactory() {
                return he -> FakeSender.StatusCode.create();
            }

            @Override
            protected <V>
                    GenericJsonApiHandler.SenderFactory<StubHttpExchange, Sender.Value<V>> getJsonValueSenderFactory() {
                return StubPreArgStageBuilder::createJsonValueSender;
            }

            @Override
            protected GenericJsonApiHandler.DispatcherFactory<StubHttpExchange> getDispatcherFactory() {
                return he -> StubDispatcher.get();
            }

            @Override
            protected GenericJsonApiHandler.HttpHandlerFactory<StubHttpExchange, StubHttpHandler>
                    getHttpHandlerFactory() {
                return StubHttpHandler::new;
            }

            @SuppressWarnings("unchecked")
            private static <V> Sender.Value<V> createJsonValueSender(StubHttpExchange httpExchange) {
                FakeSender.Value<V> sender = FakeSender.Value.create();
                GenericJsonApiHandlerTest.sender = (FakeSender.Value<Object>) sender;
                return sender;
            }

            private StubPreArgStageBuilder() {}
        }
    }

    /** Stub HTTP exchange. */
    private record StubHttpExchange(
            HttpOptional<Integer> maybeAddend1,
            HttpOptional<Integer> maybeAddend2,
            HttpOptional<Integer> maybeAddend3,
            HttpOptional<Integer> maybeAddend4,
            HttpOptional<Integer> maybeAddend5,
            HttpOptional<Integer> maybeAddend6,
            HttpOptional<Integer> maybeAddend7,
            HttpOptional<Integer> maybeAddend8) {

        public static StubHttpExchange create(
                String arg1,
                String arg2,
                String arg3,
                String arg4,
                String arg5,
                String arg6,
                String arg7,
                String arg8) {
            HttpOptional<Integer> maybeAddend1 = tryExtractAddend(arg1);
            HttpOptional<Integer> maybeAddend2 = tryExtractAddend(arg2);
            HttpOptional<Integer> maybeAddend3 = tryExtractAddend(arg3);
            HttpOptional<Integer> maybeAddend4 = tryExtractAddend(arg4);
            HttpOptional<Integer> maybeAddend5 = tryExtractAddend(arg5);
            HttpOptional<Integer> maybeAddend6 = tryExtractAddend(arg6);
            HttpOptional<Integer> maybeAddend7 = tryExtractAddend(arg7);
            HttpOptional<Integer> maybeAddend8 = tryExtractAddend(arg8);
            return new StubHttpExchange(
                    maybeAddend1,
                    maybeAddend2,
                    maybeAddend3,
                    maybeAddend4,
                    maybeAddend5,
                    maybeAddend6,
                    maybeAddend7,
                    maybeAddend8);
        }

        private static HttpOptional<Integer> tryExtractAddend(String arg) {
            try {
                int addend = Integer.parseInt(arg);
                return HttpOptional.of(addend);
            } catch (NumberFormatException e) {
                return HttpOptional.empty(400);
            }
        }
    }
}
