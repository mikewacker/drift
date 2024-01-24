package io.github.mikewacker.drift.api;

/**
 * Response sender that can send an HTTP status code for an error.
 * <p>
 * A sub-interface will define a method to send a successful response.
 */
public interface Sender {

    /**
     * Sends an error status code.
     *
     * @param errorCode an HTTP status code for the error
     */
    void sendErrorCode(int errorCode);

    /**
     * Sends an error status code.
     * <p>
     * Often used when errors need to be propagated.
     *
     * @param emptyValue the empty {@link HttpOptional} value
     * @param <U> the type of the value
     */
    default <U> void sendErrorCode(HttpOptional<U> emptyValue) {
        sendErrorCode(emptyValue.statusCode());
    }

    /** Response sender that only sends an HTTP status code. */
    interface StatusCode extends Sender {

        /** Sends a 200 status code. */
        default void sendOk() {
            send(200);
        }

        @Override
        default void sendErrorCode(int errorCode) {
            send(errorCode);
        }

        /**
         * Sends a status code.
         *
         * @param statusCode an HTTP status code
         */
        void send(int statusCode);
    }

    /**
     * Response sender that sends a serialized value for a successful response.
     *
     * @param <V> the type of the value
     */
    interface Value<V> extends Sender {

        /**
         * Serializes and sends a value.
         *
         * @param value the value to send
         */
        default void sendValue(V value) {
            send(HttpOptional.of(value));
        }

        @Override
        default void sendErrorCode(int errorCode) {
            send(HttpOptional.empty(errorCode));
        }

        /**
         * Serializes and sends a value, or sends an error status code if the value is empty.
         *
         * @param maybeValue the {@link HttpOptional} value
         */
        void send(HttpOptional<V> maybeValue);
    }
}
