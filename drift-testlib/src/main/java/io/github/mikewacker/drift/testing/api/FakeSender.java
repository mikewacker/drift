package io.github.mikewacker.drift.testing.api;

import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Fake {@link Sender} that stores the response that was sent.
 * <p>
 * Throws an exception if a response is sent twice.
 */
public interface FakeSender extends Sender {

    /**
     * Fake {@link Sender.StatusCode} that stores the response that was sent.
     * <p>
     * Throws an exception if a response is sent twice.
     */
    final class StatusCode implements Sender.StatusCode {

        private static final OptionalInt EMPTY = OptionalInt.empty();

        private final AtomicReference<OptionalInt> maybeStatusCode = new AtomicReference<>(EMPTY);

        /**
         * Creates a fake sender.
         *
         * @return a new {@code Sender.StatusCode}
         */
        public static FakeSender.StatusCode create() {
            return new FakeSender.StatusCode();
        }

        /**
         * Gets the response if it was sent.
         *
         * @return the response if it was sent, otherwise empty
         */
        public OptionalInt tryGet() {
            return maybeStatusCode.get();
        }

        @Override
        public void send(int statusCode) {
            if (!maybeStatusCode.compareAndSet(EMPTY, OptionalInt.of(statusCode))) {
                throw new IllegalStateException("response was already sent");
            }
        }

        private StatusCode() {}
    }

    /**
     * Fake {@link Sender.Value} that stores the response that was sent.
     * <p>
     * Throws an exception if a response is sent twice.
     *
     * @param <V> the type of the value
     */
    final class Value<V> implements Sender.Value<V> {

        // Null is empty; a static EMPTY field does not work with a generic type.
        private final AtomicReference<HttpOptional<V>> maybeResponse = new AtomicReference<>(null);

        /**
         * Creates a fake sender.
         *
         * @return a new {@code FakeSender.Value}
         * @param <V> the type of the value
         */
        public static <V> FakeSender.Value<V> create() {
            return new FakeSender.Value<>();
        }

        /**
         * Gets the response if it was sent.
         *
         * @return the response if it was sent, otherwise empty
         */
        public Optional<HttpOptional<V>> tryGet() {
            return Optional.ofNullable(maybeResponse.get());
        }

        @Override
        public void send(HttpOptional<V> maybeValue) {
            if (!maybeResponse.compareAndSet(null, maybeValue)) {
                throw new IllegalStateException("response was already sent");
            }
        }

        private Value() {}
    }
}
