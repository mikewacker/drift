package io.github.mikewacker.drift.testing.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.json.JsonValues;

/** Static methods for testing JSON serialization. */
public final class JsonTester {

    /**
     * Serializes a value as JSON and then deserializes the JSON back to a value,
     * verifying that the deserialized value equals the original value.
     *
     * @param value the value to serialize then deserialize
     * @param valueTypeRef a {@link TypeReference} for the value
     * @return the deserialized value
     * @param <V> the type of the value
     */
    public static <V> V serializeThenDeserialize(V value, TypeReference<V> valueTypeRef) {
        byte[] rawValue = JsonValues.serialize(value);
        V rtValue = JsonValues.deserialize(rawValue, valueTypeRef);
        assertThat(rtValue).isEqualTo(value);
        return rtValue;
    }

    // static class
    private JsonTester() {}
}
