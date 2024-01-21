package io.github.mikewacker.drift.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.Sender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class GenericJsonApiRouterTest {

    private static StubJsonApiRouter router;

    @BeforeAll
    public static void createStubJsonApiRouter() {
        router = StubJsonApiRouter.of(StubJsonApiHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .statusCodeResponse()
                .apiHandler(GenericJsonApiRouterTest::sendOk)
                .build());
    }

    @Test
    public void route() throws Exception {
        StubHttpExchange exchange = new StubHttpExchange("GET", "/some/path");
        router.handleRequest(exchange);
        assertThat(StubJsonApiRouter.tryGetErrorCodeSent()).isEmpty();
        assertThat(StubJsonApiHandler.getStatusCodeSent()).isEqualTo(200);
    }

    @Test
    public void notFound() throws Exception {
        StubHttpExchange exchange = new StubHttpExchange("GET", "/other/path");
        router.handleRequest(exchange);
        assertThat(StubJsonApiRouter.tryGetErrorCodeSent()).hasValue(404);
    }

    @Test
    public void methodNotAllowed() throws Exception {
        StubHttpExchange exchange = new StubHttpExchange("PUT", "/some/path");
        router.handleRequest(exchange);
        assertThat(StubJsonApiRouter.tryGetErrorCodeSent()).hasValue(405);
    }

    @Test
    public void badMethod() throws Exception {
        StubHttpExchange exchange = new StubHttpExchange("TRACE", "/some/path");
        router.handleRequest(exchange);
        assertThat(StubJsonApiRouter.tryGetErrorCodeSent()).hasValue(400);
    }

    @Test
    public void error_RouteConflict() {
        StubJsonApiHandler httpHandler = StubJsonApiHandler.builder()
                .route(HttpMethod.GET, "/some/path")
                .statusCodeResponse()
                .apiHandler(GenericJsonApiRouterTest::sendOk)
                .build();
        assertThatThrownBy(() -> StubJsonApiRouter.of(httpHandler, httpHandler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("multiple HTTP handlers have the same route");
    }

    private static void sendOk(Sender.StatusCode sender, Dispatcher dispatcher) {
        sender.sendOk();
    }
}
