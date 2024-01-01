package io.github.mikewacker.drift.json;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public final class JsonValuesTest {

    @Test
    public void serializeThenDeserialize() {
        String value = "test";
        byte[] rawValue = JsonValues.serialize(value);
        String rtValue = JsonValues.deserialize(rawValue, new TypeReference<>() {});
        assertThat(rtValue).isEqualTo(value);
    }

    @Test
    public void serializeThenTryDeserialize_HttpOptional() {
        String value = "test";
        byte[] rawValue = JsonValues.serialize(value);
        HttpOptional<String> maybeRtValue = JsonValues.tryDeserialize(rawValue, new TypeReference<>() {}, 400);
        assertThat(maybeRtValue).hasValue(value);
    }

    @Test
    public void serializeThenTryDeserialize_Optional() {
        String value = "test";
        byte[] rawValue = JsonValues.serialize(value);
        Optional<String> maybeRtValue = JsonValues.tryDeserialize(rawValue, new TypeReference<>() {});
        assertThat(maybeRtValue).hasValue(value);
    }

    @Test
    public void serializeBytesUsingUrlFriendlyBase64Encoding() {
        byte[] value = new byte[] {-5, -16, 0, 0};
        byte[] rawValue = JsonValues.serialize(value);
        assertThat(rawValue).asString(StandardCharsets.UTF_8).isEqualTo("\"-_AAAA\"");
    }

    @Test
    public void serializeFailed() {
        Object unserializableValue = new JsonValuesTest();
        assertThatThrownBy(() -> JsonValues.serialize(unserializableValue))
                .isInstanceOf(JsonSerializationException.class)
                .hasMessageStartingWith("serialization failed");
    }

    @Test
    public void deserializeFailed() {
        byte[] malformedRawValue = new byte[4];
        assertThatThrownBy(() -> JsonValues.deserialize(malformedRawValue, new TypeReference<String>() {}))
                .isInstanceOf(JsonSerializationException.class)
                .hasMessageStartingWith("deserialization failed: JSON");
    }

    @Test
    public void tryDeserializeFailed_HttpOptional() {
        byte[] malformedRawValue = new byte[4];
        HttpOptional<String> maybeValue = JsonValues.tryDeserialize(malformedRawValue, new TypeReference<>() {}, 400);
        assertThat(maybeValue).isEmptyWithErrorCode(400);
    }

    @Test
    public void tryDeserializeFailed_Optional() {
        byte[] malformedRawValue = new byte[4];
        Optional<String> maybeValue = JsonValues.tryDeserialize(malformedRawValue, new TypeReference<>() {});
        assertThat(maybeValue).isEmpty();
    }
}
