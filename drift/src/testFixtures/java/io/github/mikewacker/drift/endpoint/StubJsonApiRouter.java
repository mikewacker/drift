package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Sender;
import java.util.Optional;
import java.util.OptionalInt;

/** Stub router. */
public class StubJsonApiRouter extends GenericJsonApiRouter<StubHttpExchange> {

    private static OptionalInt maybeErrorCode = OptionalInt.empty();

    /** Gets the error status code if it was sent. */
    public static OptionalInt tryGetErrorCodeSent() {
        return maybeErrorCode;
    }

    /** Creates a router. */
    public static StubJsonApiRouter of(StubJsonApiHandler... httpHandlers) {
        StubJsonApiRouter router = new StubJsonApiRouter();
        for (StubJsonApiHandler httpHandler : httpHandlers) {
            router.addHttpHandler(httpHandler);
        }
        return router;
    }

    @Override
    protected Sender createErrorCodeSender(StubHttpExchange httpExchange) {
        maybeErrorCode = OptionalInt.empty();
        return errorCode -> maybeErrorCode = OptionalInt.of(errorCode);
    }

    @Override
    protected Optional<HttpMethod> tryGetMethod(StubHttpExchange httpExchange) {
        try {
            HttpMethod method = HttpMethod.valueOf(httpExchange.method());
            return Optional.of(method);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    protected String getRelativePath(StubHttpExchange httpExchange) {
        return httpExchange.relativePath();
    }

    private StubJsonApiRouter() {}
}
