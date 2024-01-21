package io.github.mikewacker.drift.endpoint;

import static io.github.mikewacker.drift.testing.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

public final class GenericJsonApiHandlerTest {

    @Test
    public void handleHttpRequest_ZeroArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add0")
                .jsonResponse(new TypeReference<Integer>() {})
                .apiHandler(Adder::add0)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add0");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(0);
    }

    @Test
    public void handleHttpRequest_OneArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add1")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .apiHandler(Adder::add1)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add1", "1");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(1);
    }

    @Test
    public void handleHttpRequest_TwoArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add2")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .arg(StubArgs.intValue(1))
                .apiHandler(Adder::add2)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add2", "1", "2");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(3);
    }

    @Test
    public void handleHttpRequest_ThreeArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add3")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .arg(StubArgs.intValue(1))
                .arg(StubArgs.intValue(2))
                .apiHandler(Adder::add3)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add3", "1", "2", "3");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(6);
    }

    @Test
    public void handleHttpRequest_FourArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add4")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .arg(StubArgs.intValue(1))
                .arg(StubArgs.intValue(2))
                .arg(StubArgs.intValue(3))
                .apiHandler(Adder::add4)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add4", "1", "2", "3", "4");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(10);
    }

    @Test
    public void handleHttpRequest_FiveArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add5")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .arg(StubArgs.intValue(1))
                .arg(StubArgs.intValue(2))
                .arg(StubArgs.intValue(3))
                .arg(StubArgs.intValue(4))
                .apiHandler(Adder::add5)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add5", "1", "2", "3", "4", "5");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(15);
    }

    @Test
    public void handleHttpRequest_SixArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add6")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .arg(StubArgs.intValue(1))
                .arg(StubArgs.intValue(2))
                .arg(StubArgs.intValue(3))
                .arg(StubArgs.intValue(4))
                .arg(StubArgs.intValue(5))
                .apiHandler(Adder::add6)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add6", "1", "2", "3", "4", "5", "6");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(21);
    }

    @Test
    public void handleHttpRequest_SevenArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add7")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .arg(StubArgs.intValue(1))
                .arg(StubArgs.intValue(2))
                .arg(StubArgs.intValue(3))
                .arg(StubArgs.intValue(4))
                .arg(StubArgs.intValue(5))
                .arg(StubArgs.intValue(6))
                .apiHandler(Adder::add7)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add7", "1", "2", "3", "4", "5", "6", "7");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(28);
    }

    @Test
    public void handleHttpRequest_EightArgApiRequest() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add8")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .arg(StubArgs.intValue(1))
                .arg(StubArgs.intValue(2))
                .arg(StubArgs.intValue(3))
                .arg(StubArgs.intValue(4))
                .arg(StubArgs.intValue(5))
                .arg(StubArgs.intValue(6))
                .arg(StubArgs.intValue(7))
                .apiHandler(Adder::add8)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add8", "1", "2", "3", "4", "5", "6", "7", "8");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).hasValue(36);
    }

    @Test
    public void handleHttpRequest_ArgExtractorFails() throws Exception {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/add1")
                .jsonResponse(new TypeReference<Integer>() {})
                .arg(StubArgs.intValue(0))
                .apiHandler(Adder::add1)
                .build();
        StubHttpExchange httpExchange = StubHttpExchange.of("GET", "/add1", "a");
        httpHandler.handleRequest(httpExchange);
        assertThat(StubHttpHandler.getValueOrErrorCodeSent()).isEmptyWithErrorCode(400);
    }

    @Test
    public void getRoute() {
        StubHttpHandler httpHandler = StubHttpHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .jsonResponse(new TypeReference<Integer>() {})
                .apiHandler(Adder::add0)
                .build();
        assertThat(httpHandler.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpHandler.getRelativePathSegments()).containsExactly("some", "path");
    }
}
