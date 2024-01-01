package io.github.mikewacker.drift.json;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public final class JsonDeserializationExceptionTest {

    @Test
    public void serialize() {
        IOException cause = new IOException();
        JsonSerializationException e = JsonSerializationException.serialize("test", cause);
        assertThat(e)
                .hasMessage("serialization failed: class java.lang.String could not be serialized as JSON\ntest")
                .hasCause(cause);
    }

    @Test
    public void deserializeMissingContentType() {
        JsonSerializationException e = JsonSerializationException.deserializeMissingContentType();
        assertThat(e).hasMessage("deserialization failed: Content-Type is missing");
    }

    @Test
    public void deserializeInvalidContentType() {
        JsonSerializationException e = JsonSerializationException.deserializeInvalidContentType("text/html");
        assertThat(e).hasMessage("deserialization failed: Content-Type is not application/json\ntext/html");
    }

    @Test
    public void deserializeJson() {
        byte[] rawValue = "{".getBytes(StandardCharsets.UTF_8);
        IOException cause = new IOException();
        JsonSerializationException e =
                JsonSerializationException.deserializeJson(rawValue, new TypeReference<String>() {}, cause);
        assertThat(e)
                .hasMessage("deserialization failed: JSON could not be deserialized as class java.lang.String\n{")
                .hasCause(cause);
    }
}
