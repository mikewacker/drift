package io.github.mikewacker.drift.endpoint;

/**
 * An HTTP handler for the underlying server that routes each HTTP request to an HTTP handler for an API handler.
 * The HTTP handler for an API handler is a {@link JsonApiHandler}.
 * <p>
 * An implementation for a specific server will provide a static factory method, {@code of()}; the method accepts
 * a varargs list of HTTP handlers whose type is a {@code JsonApiHandler} implementation for the server.
 *
 * @param <E> the type of the underlying HTTP exchange
 */
public interface JsonApiRouter<E> {

    /**
     * Handles an HTTP request by routing it to an HTTP handler for an API handler, or sending an error status code.
     *
     * @param httpExchange the underlying HTTP exchange
     * @throws Exception for any exception that the {@code JsonApiHandler} may throw
     */
    void handleRequest(E httpExchange) throws Exception;
}
