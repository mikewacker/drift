package io.github.mikewacker.drift.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.testing.api.FakeSender;
import io.github.mikewacker.drift.testing.api.StubDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class GenericAdaptedApiHandlerTest {

    private static FakeSender.Value<Integer> sender;
    private static Dispatcher dispatcher;

    @BeforeEach
    public void createSenderAndDispatcher() {
        sender = FakeSender.Value.create();
        dispatcher = StubDispatcher.get();
    }

    @Test
    public void handleHttpRequest_ZeroArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder().build(Adder::add0);
        StubHttpExchange httpExchange = StubHttpExchange.create(null, null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(0));
    }

    @Test
    public void handleHttpRequest_OneArgApiRequest() throws Exception {
        StubHttpHandler httpHandler =
                StubHttpHandler.builder().addArg(StubHttpExchange::maybeAddend1).build(Adder::add1);
        StubHttpExchange httpExchange = StubHttpExchange.create("1", null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(1));
    }

    @Test
    public void handleHttpRequest_TwoArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .addArg(StubHttpExchange::maybeAddend1)
                .addArg(StubHttpExchange::maybeAddend2)
                .build(Adder::add2);
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(3));
    }

    @Test
    public void handleHttpRequest_ThreeArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .addArg(StubHttpExchange::maybeAddend1)
                .addArg(StubHttpExchange::maybeAddend2)
                .addArg(StubHttpExchange::maybeAddend3)
                .build(Adder::add3);
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(6));
    }

    @Test
    public void handleHttpRequest_FourArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .addArg(StubHttpExchange::maybeAddend1)
                .addArg(StubHttpExchange::maybeAddend2)
                .addArg(StubHttpExchange::maybeAddend3)
                .addArg(StubHttpExchange::maybeAddend4)
                .build(Adder::add4);
        StubHttpExchange httpExchange = StubHttpExchange.create("1", "2", "3", "4");
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(10));
    }

    @Test
    public void handleHttpRequest_ArgExtractorFails() throws Exception {
        StubHttpHandler httpHandler =
                StubHttpHandler.builder().addArg(StubHttpExchange::maybeAddend1).build(Adder::add1);
        StubHttpExchange httpExchange = StubHttpExchange.create("a", null, null, null);
        httpHandler.handleRequest(httpExchange);
        assertThat(sender.tryGet()).hasValue(HttpOptional.empty(400));
    }

    /** Stub HTTP handler. */
    private static final class StubHttpHandler implements AdaptedApiHandler<StubHttpExchange> {

        private final AdaptedApiHandler<StubHttpExchange> delegate;

        public static ZeroArgBuilder<StubHttpExchange, StubHttpHandler, Sender.Value<Integer>> builder() {
            return GenericAdaptedApiHandler.builder(
                    StubHttpExchange::sender, StubHttpExchange::dispatcher, StubHttpHandler::new);
        }

        @Override
        public void handleRequest(StubHttpExchange exchange) throws Exception {
            delegate.handleRequest(exchange);
        }

        private StubHttpHandler(AdaptedApiHandler<StubHttpExchange> delegate) {
            this.delegate = delegate;
        }
    }

    /** Stub HTTP exchange. */
    private record StubHttpExchange(
            Sender.Value<Integer> sender,
            HttpOptional<Integer> maybeAddend1,
            HttpOptional<Integer> maybeAddend2,
            HttpOptional<Integer> maybeAddend3,
            HttpOptional<Integer> maybeAddend4,
            Dispatcher dispatcher) {

        public static StubHttpExchange create(String arg1, String arg2, String arg3, String arg4) {
            Sender.Value<Integer> sender = GenericAdaptedApiHandlerTest.sender;
            HttpOptional<Integer> maybeAddend1 = tryExtractAddend(arg1);
            HttpOptional<Integer> maybeAddend2 = tryExtractAddend(arg2);
            HttpOptional<Integer> maybeAddend3 = tryExtractAddend(arg3);
            HttpOptional<Integer> maybeAddend4 = tryExtractAddend(arg4);
            Dispatcher dispatcher = GenericAdaptedApiHandlerTest.dispatcher;
            return new StubHttpExchange(sender, maybeAddend1, maybeAddend2, maybeAddend3, maybeAddend4, dispatcher);
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
