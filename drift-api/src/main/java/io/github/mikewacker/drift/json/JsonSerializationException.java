package io.github.mikewacker.drift.json;

import java.io.IOException;

/** Unchecked exception signaling that a value cannot be serialized to JSON or deserialized from JSON. */
public class JsonSerializationException extends RuntimeException {

    /**
     * Creates an unchecked exception for when serialization fails.
     *
     * @param cause the underlying cause of the exception
     * @return a {@code JsonSerializationException}
     */
    public static JsonSerializationException serialize(IOException cause) {
        return new JsonSerializationException("serialization failed", cause);
    }

    /**
     * Creates an unchecked exception for when deserialization fails.
     *
     * @param cause the underlying cause of the exception
     * @return a {@code JsonSerializationException}
     */
    public static JsonSerializationException deserialize(IOException cause) {
        return new JsonSerializationException("deserialization failed", cause);
    }

    private JsonSerializationException(String message, IOException cause) {
        super(message, cause);
    }
}
