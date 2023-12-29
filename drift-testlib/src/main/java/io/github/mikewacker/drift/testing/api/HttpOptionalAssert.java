package io.github.mikewacker.drift.testing.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mikewacker.drift.api.HttpOptional;

/**
 * Fluent assertions for {@link HttpOptional}.
 *
 * @param <V> the type of the value
 */
public final class HttpOptionalAssert<V> {

    private final HttpOptional<V> maybeValue;

    /** Verifies that the value is present. */
    public void isPresent() {
        assertThat(maybeValue.isPresent()).isTrue();
    }

    /**
     * Verifies that the value is present and is equal to the expected value.
     *
     * @param expectedValue the expected value
     */
    public void hasValue(V expectedValue) {
        isPresent();
        assertThat(maybeValue.get()).isEqualTo(expectedValue);
    }

    /** Verifies that the value is empty. */
    public void isEmpty() {
        assertThat(maybeValue.isEmpty()).isTrue();
    }

    /**
     * Verifies that the value is empty, and that the status code is equal to the expected error status code.
     *
     * @param expectedErrorCode the expected HTTP status code for the error
     */
    public void isEmptyWithErrorCode(int expectedErrorCode) {
        isEmpty();
        assertThat(maybeValue.statusCode()).isEqualTo(expectedErrorCode);
    }

    HttpOptionalAssert(HttpOptional<V> maybeValue) {
        this.maybeValue = maybeValue;
    }
}
