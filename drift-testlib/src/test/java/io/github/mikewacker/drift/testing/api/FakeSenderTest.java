package io.github.mikewacker.drift.testing.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.mikewacker.drift.api.HttpOptional;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public final class FakeSenderTest {

    @Test
    public void sendAndGet_StatusCode() {
        FakeSender.StatusCode sender = FakeSender.StatusCode.create();
        sender.sendOk();
        assertThat(sender.tryGet()).hasValue(200);
    }

    @Test
    public void sendAndGet_Value() {
        FakeSender.Value<String> sender = FakeSender.Value.create();
        sender.sendValue("test");
        assertThat(sender.tryGet()).hasValue(HttpOptional.of("test"));
    }

    @Test
    public void tryGet_StatusCode() {
        FakeSender.StatusCode sender = FakeSender.StatusCode.create();
        assertThat(sender.tryGet()).isEmpty();
    }

    @Test
    public void tryGet_Value() {
        FakeSender.Value<String> sender = FakeSender.Value.create();
        assertThat(sender.tryGet()).isEmpty();
    }

    @Test
    public void error_SendTwice_StatusCode() {
        FakeSender.StatusCode sender = FakeSender.StatusCode.create();
        sender.sendOk();
        error_SendTwice(sender::sendOk);
    }

    @Test
    public void error_SendTwice_Value() {
        FakeSender.Value<String> sender = FakeSender.Value.create();
        sender.sendValue("test");
        error_SendTwice(() -> sender.sendValue("test"));
    }

    private void error_SendTwice(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(IllegalStateException.class).hasMessage("response was already sent");
    }
}
