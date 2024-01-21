package io.github.mikewacker.drift.endpoint;

/** Stub HTTP exchange. */
public record StubHttpExchange(String method, String relativePath, String... args) {

    /** Creates a stub HTTP exchange from the route and the arguments. */
    public static StubHttpExchange of(String method, String relativePath, String... args) {
        return new StubHttpExchange(method, relativePath, args);
    }
}
