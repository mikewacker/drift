package io.github.mikewacker.drift.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.testing.api.FakeSender;
import io.github.mikewacker.drift.testing.api.StubDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ApiRequestTest {

    private FakeSender.Value<Integer> sender;
    private Dispatcher dispatcher;

    @BeforeEach
    public void createSenderAndDispatcher() {
        sender = FakeSender.Value.create();
        dispatcher = StubDispatcher.get();
    }

    @Test
    public void handleApiRequest_ZeroArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Void, Void, Void, Void, Void, Void, Void, Void> requestFactory =
                ApiRequest.Factory.zeroArg(Adder::add0);
        ApiRequest<Sender.Value<Integer>, Void, Void, Void, Void, Void, Void, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(0));
    }

    @Test
    public void handleApiRequest_OneArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Void, Void, Void, Void, Void, Void, Void> requestFactory =
                ApiRequest.Factory.oneArg(Adder::add1);
        ApiRequest<Sender.Value<Integer>, Integer, Void, Void, Void, Void, Void, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(1));
    }

    @Test
    public void handleApiRequest_TwoArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Integer, Void, Void, Void, Void, Void, Void> requestFactory =
                ApiRequest.Factory.twoArg(Adder::add2);
        ApiRequest<Sender.Value<Integer>, Integer, Integer, Void, Void, Void, Void, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.setArg2(2);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(3));
    }

    @Test
    public void handleApiRequest_ThreeArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Integer, Integer, Void, Void, Void, Void, Void>
                requestFactory = ApiRequest.Factory.threeArg(Adder::add3);
        ApiRequest<Sender.Value<Integer>, Integer, Integer, Integer, Void, Void, Void, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.setArg2(2);
        request.setArg3(3);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(6));
    }

    @Test
    public void handleApiRequest_FourArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Void, Void, Void, Void>
                requestFactory = ApiRequest.Factory.fourArg(Adder::add4);
        ApiRequest<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Void, Void, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.setArg2(2);
        request.setArg3(3);
        request.setArg4(4);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(10));
    }

    @Test
    public void handleApiRequest_FiveArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Void, Void, Void>
                requestFactory = ApiRequest.Factory.fiveArg(Adder::add5);
        ApiRequest<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Void, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.setArg2(2);
        request.setArg3(3);
        request.setArg4(4);
        request.setArg5(5);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(15));
    }

    @Test
    public void handleApiRequest_SixArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Integer, Void, Void>
                requestFactory = ApiRequest.Factory.sixArg(Adder::add6);
        ApiRequest<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Integer, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.setArg2(2);
        request.setArg3(3);
        request.setArg4(4);
        request.setArg5(5);
        request.setArg6(6);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(21));
    }

    @Test
    public void handleApiRequest_SevenArg() throws Exception {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Void>
                requestFactory = ApiRequest.Factory.sevenArg(Adder::add7);
        ApiRequest<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.setArg2(2);
        request.setArg3(3);
        request.setArg4(4);
        request.setArg5(5);
        request.setArg6(6);
        request.setArg7(7);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(28));
    }

    @Test
    public void handleApiRequest_EightArg() throws Exception {
        ApiRequest.Factory<
                        Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>
                requestFactory = ApiRequest.Factory.eightArg(Adder::add8);
        ApiRequest<Sender.Value<Integer>, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>
                request = requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.setArg1(1);
        request.setArg2(2);
        request.setArg3(3);
        request.setArg4(4);
        request.setArg5(5);
        request.setArg6(6);
        request.setArg7(7);
        request.setArg8(8);
        request.handle();
        assertThat(sender.tryGet()).hasValue(HttpOptional.of(36));
    }

    @Test
    public void sendErrorCode() {
        ApiRequest.Factory<Sender.Value<Integer>, Integer, Void, Void, Void, Void, Void, Void, Void> requestFactory =
                ApiRequest.Factory.oneArg(Adder::add1);
        ApiRequest<Sender.Value<Integer>, Integer, Void, Void, Void, Void, Void, Void, Void> request =
                requestFactory.createWithUnsetArgs(sender, dispatcher);
        request.sendErrorCode(HttpOptional.empty(400));
        assertThat(sender.tryGet()).hasValue(HttpOptional.empty(400));
    }
}
