package io.github.mikewacker.drift.client;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.json.JsonSerializationException;
import io.github.mikewacker.drift.testing.server.MockServer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class JsonApiClientTest {

    @RegisterExtension
    private static final MockServer mockServer = MockServer.register("test");

    @Test
    public void statusCodeResponse() throws IOException {
        mockServer.enqueue(new MockResponse());
        int statusCode =
                JsonApiClient.requestBuilder().get(mockServer.rootUrl()).build().execute();
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void jsonValueResponse_Ok() throws IOException {
        mockServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json").setBody("\"test\""));
        HttpOptional<String> maybeText = JsonApiClient.requestBuilder(new TypeReference<String>() {})
                .get(mockServer.rootUrl())
                .build()
                .execute();
        assertThat(maybeText).hasValue("test");
    }

    @Test
    public void jsonValueResponse_ErrorCode() throws IOException {
        mockServer.enqueue(new MockResponse().setResponseCode(500));
        HttpOptional<String> maybeText = JsonApiClient.requestBuilder(new TypeReference<String>() {})
                .get(mockServer.rootUrl())
                .build()
                .execute();
        assertThat(maybeText).isEmptyWithErrorCode(500);
    }

    @Test
    public void get() throws Exception {
        method(
                () -> JsonApiClient.requestBuilder()
                        .get(mockServer.rootUrl())
                        .build()
                        .execute(),
                "GET");
    }

    @Test
    public void put() throws Exception {
        method(
                () -> JsonApiClient.requestBuilder()
                        .put(mockServer.rootUrl())
                        .build()
                        .execute(),
                "PUT");
    }

    @Test
    public void post() throws Exception {
        method(
                () -> JsonApiClient.requestBuilder()
                        .post(mockServer.rootUrl())
                        .build()
                        .execute(),
                "POST");
    }

    @Test
    public void delete() throws Exception {
        method(
                () -> JsonApiClient.requestBuilder()
                        .delete(mockServer.rootUrl())
                        .build()
                        .execute(),
                "DELETE");
    }

    @Test
    public void patch() throws Exception {
        method(
                () -> JsonApiClient.requestBuilder()
                        .patch(mockServer.rootUrl())
                        .build()
                        .execute(),
                "PATCH");
    }

    @Test
    public void head() throws Exception {
        method(
                () -> JsonApiClient.requestBuilder()
                        .head(mockServer.rootUrl())
                        .build()
                        .execute(),
                "HEAD");
    }

    private void method(Callable<Integer> requestSender, String expectedMethod) throws Exception {
        mockServer.enqueue(new MockResponse());
        int statusCode = requestSender.call();
        assertThat(statusCode).isEqualTo(200);

        RecordedRequest recordedRequest = takeRecordedRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo(expectedMethod);
    }

    @Test
    public void headers() throws Exception {
        mockServer.enqueue(new MockResponse());
        int statusCode = JsonApiClient.requestBuilder()
                .get(mockServer.rootUrl())
                .header("User-Agent", "agent")
                .header("Test-Header", "value")
                .build()
                .execute();
        assertThat(statusCode).isEqualTo(200);

        RecordedRequest recordedRequest = takeRecordedRequest();
        assertThat(recordedRequest.getHeader("User-Agent")).isEqualTo("agent");
        assertThat(recordedRequest.getHeader("Test-Header")).isEqualTo("value");
    }

    @Test
    public void body() throws Exception {
        mockServer.enqueue(new MockResponse());
        int statusCode = JsonApiClient.requestBuilder()
                .post(mockServer.rootUrl())
                .body("test")
                .build()
                .execute();
        assertThat(statusCode).isEqualTo(200);

        RecordedRequest recordedRequest = takeRecordedRequest();
        assertThat(recordedRequest.getHeader("Content-Type")).isEqualTo("application/json");
        assertThat(recordedRequest.getBody().readString(StandardCharsets.UTF_8)).isEqualTo("\"test\"");
    }

    @Test
    public void error_JsonValueResponse_MissingContentType() {
        mockServer.enqueue(new MockResponse().setBody("\"test\""));
        error_JsonValueResponse("deserialization failed: Content-Type is missing");
    }

    @Test
    public void error_JsonValueResponse_UnparsableContentType() {
        mockServer.enqueue(new MockResponse().setHeader("Content-Type", "a").setBody("\"test\""));
        error_JsonValueResponse("deserialization failed: Content-Type is not");
    }

    @Test
    public void error_JsonValueResponse_InvalidContentType() {
        mockServer.enqueue(
                new MockResponse().setHeader("Content-Type", "text/html").setBody("<p>test</p>"));
        error_JsonValueResponse("deserialization failed: Content-Type is not");
    }

    @Test
    public void error_JsonValueResponse_InvalidJson() {
        mockServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json").setBody("{"));
        error_JsonValueResponse("deserialization failed: JSON");
    }

    private void error_JsonValueResponse(String expectedMessagePrefix) {
        assertThatThrownBy(() -> JsonApiClient.requestBuilder(new TypeReference<String>() {})
                        .get(mockServer.rootUrl())
                        .build()
                        .execute())
                .isInstanceOf(JsonSerializationException.class)
                .hasMessageStartingWith(expectedMessagePrefix);
    }

    @Test
    public void error_StageCompleted() {
        ApiClient.UrlStageRequestBuilder<JsonApiClient.SendStage<Integer>> requestBuilder =
                JsonApiClient.requestBuilder();
        requestBuilder.get(mockServer.rootUrl());
        assertThatThrownBy(() -> requestBuilder.get(mockServer.rootUrl()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("stage already completed");
    }

    @Test
    public void error_SendRequestTwice() throws IOException {
        mockServer.enqueue(new MockResponse());
        JsonApiClient.SendStage<Integer> requestSender =
                JsonApiClient.requestBuilder().get(mockServer.rootUrl()).build();
        requestSender.execute();
        assertThatThrownBy(requestSender::execute)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("request was already sent");
    }

    private static RecordedRequest takeRecordedRequest() throws InterruptedException {
        RecordedRequest recordedRequest = mockServer.get().takeRequest(0, TimeUnit.MILLISECONDS);
        assertThat(recordedRequest).isNotNull();
        return recordedRequest;
    }
}
