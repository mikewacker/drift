package io.github.mikewacker.drift.testing.api;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.mikewacker.drift.api.HttpOptional;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public final class HttpOptionalAssertTest {

    @Test
    public void of() {
        HttpOptional<String> maybeValue = HttpOptional.of("a");
        assertThat(maybeValue).isPresent();
        assertThat(maybeValue).hasValue("a");
        assertionFailed(() -> assertThat(maybeValue).hasValue("b"));
        assertionFailed(() -> assertThat(maybeValue).isEmpty());
        assertionFailed(() -> assertThat(maybeValue).isEmptyWithErrorCode(500));
    }

    @Test
    public void empty() {
        HttpOptional<String> maybeValue = HttpOptional.empty(500);
        assertionFailed(() -> assertThat(maybeValue).isPresent());
        assertionFailed(() -> assertThat(maybeValue).hasValue("a"));
        assertThat(maybeValue).isEmpty();
        assertThat(maybeValue).isEmptyWithErrorCode(500);
        assertionFailed(() -> assertThat(maybeValue).isEmptyWithErrorCode(400));
    }

    private static void assertionFailed(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(AssertionError.class);
    }
}
