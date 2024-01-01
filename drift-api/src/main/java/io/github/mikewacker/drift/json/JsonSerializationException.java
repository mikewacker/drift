package io.github.mikewacker.drift.json;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Unchecked exception signaling that a value cannot be serialized to JSON or deserialized from JSON. */
public class JsonSerializationException extends RuntimeException {

    /**
     * Creates an unchecked exception for when serialization fails.
     *
     * @param value the value that failed to serialize
     * @param cause the underlying cause of the exception
     * @return a {@code JsonSerializationException}
     */
    public static JsonSerializationException serialize(Object value, IOException cause) {
        String message =
                String.format("serialization failed: %s could not be serialized as JSON\n%s", value.getClass(), value);
        return new JsonSerializationException(message, cause);
    }

    /**
     * Creates an unchecked exception for when deserialization fails because the {@code Content-Type} is missing.
     * @return a {@code JsonSerializationException}
     */
    public static JsonSerializationException deserializeMissingContentType() {
        return new JsonSerializationException("deserialization failed: Content-Type is missing");
    }

    /**
     * Creates an unchecked exception for when deserialization fails
     * because the {@code Content-Type} is not {@code application/json}.
     *
     * @param contentType the value for the {@code Content-Type} header
     * @return a {@code JsonSerializationException}
     */
    public static JsonSerializationException deserializeInvalidContentType(String contentType) {
        String message = String.format("deserialization failed: Content-Type is not application/json\n%s", contentType);
        return new JsonSerializationException(message);
    }

    /**
     * Creates an unchecked exception for when deserialization fails because the JSON cannot be deserialized.
     *
     * @param rawValue the value as JSON that failed to deserialize
     * @param valueTypeRef a {@link TypeReference} for the value
     * @param cause the underlying cause of the exception
     * @return a {@code JsonSerializationException}
     */
    public static JsonSerializationException deserializeJson(
            byte[] rawValue, TypeReference<?> valueTypeRef, IOException cause) {
        String message = String.format(
                "deserialization failed: JSON could not be deserialized as %s\n%s",
                valueTypeRef.getType(), new String(rawValue, StandardCharsets.UTF_8));
        return new JsonSerializationException(message, cause);
    }

    private JsonSerializationException(String message) {
        super(message);
    }

    private JsonSerializationException(String message, IOException cause) {
        super(message, cause);
    }
}
