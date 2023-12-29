package io.github.mikewacker.drift.testing.api;

import io.github.mikewacker.drift.api.HttpOptional;

/** Entry point for fluent assertions. */
@CheckReturnValue
public final class Assertions {

    /**
     * Begins a fluent assertion for an optional value.
     *
     * @param maybeValue the {@link HttpOptional} value
     * @return an {@link HttpOptionalAssert}
     * @param <V> the type of the value
     */
    public static <V> HttpOptionalAssert<V> assertThat(HttpOptional<V> maybeValue) {
        return new HttpOptionalAssert<>(maybeValue);
    }

    // static class
    private Assertions() {}
}
