package io.github.mikewacker.drift.json;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mikewacker.drift.api.HttpOptional;
import java.io.IOException;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * Static methods for serializing and deserializing objects as JSON.
 * Uses a URL-friendly base64 encoding for {@code byte[]} values.
 * <p>
 * Objects should be serializable using the default {@link ObjectMapper}, which has no registered modules.
 * If needed, more complex serializable types can be created using Java {@code record}'s
 * or <code>@{@link Value.Immutable}</code> types annotated with <code>@{@link JsonStyle}</code>.
 */
public final class JsonValues {

    private static final ObjectMapper mapper = new ObjectMapper().setBase64Variant(Base64Variants.MODIFIED_FOR_URL);

    /**
     * Serializes a value to JSON.
     *
     * @param value the value to serialize
     * @return the value as JSON
     * @throws JsonSerializationException if serialization fails
     */
    public static byte[] serialize(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw JsonSerializationException.serialize(value, e);
        }
    }

    /**
     * Deserializes a value from JSON.
     * <p>
     * Typically used for deserializing internal data, which should be well-formed.
     *
     * @param rawValue the value as JSON
     * @param valueTypeRef a {@link TypeReference} for the value
     * @return the deserialized value
     * @throws JsonSerializationException if deserialization fails
     * @param <V> the type of the value
     */
    public static <V> V deserialize(byte[] rawValue, TypeReference<V> valueTypeRef) {
        try {
            return mapper.readValue(rawValue, valueTypeRef);
        } catch (IOException e) {
            throw JsonSerializationException.deserializeJson(rawValue, valueTypeRef, e);
        }
    }

    /**
     * Deserializes a value from JSON, or returns empty.
     * <p>
     * Typically used for deserializing user data, which may be malformed.
     *
     * @param rawValue the value as JSON
     * @param valueTypeRef a {@link TypeReference} for the value
     * @param errorCode an HTTP status code for the error, if deserialization fails
     * @return an {@link HttpOptional} with the deserialized value,
     *     or an empty {@code HttpOptional} with an error status code if deserialization fails
     * @param <V> the type of the value
     */
    public static <V> HttpOptional<V> tryDeserialize(byte[] rawValue, TypeReference<V> valueTypeRef, int errorCode) {
        try {
            V value = deserialize(rawValue, valueTypeRef);
            return HttpOptional.of(value);
        } catch (JsonSerializationException e) {
            return HttpOptional.empty(errorCode);
        }
    }

    /**
     * Deserializes a value from JSON, or returns empty.
     * <p>
     * Typically used for deserializing user data, which may be malformed.
     *
     * @param rawValue the value as JSON
     * @param valueTypeRef a {@link TypeReference} for the value
     * @return an {@link Optional} with the deserialized value, or an empty {@code Optional} if deserialization fails
     * @param <V> the type of the value
     */
    public static <V> Optional<V> tryDeserialize(byte[] rawValue, TypeReference<V> valueTypeRef) {
        try {
            V value = deserialize(rawValue, valueTypeRef);
            return Optional.of(value);
        } catch (JsonSerializationException e) {
            return Optional.empty();
        }
    }

    // static class
    private JsonValues() {}
}
