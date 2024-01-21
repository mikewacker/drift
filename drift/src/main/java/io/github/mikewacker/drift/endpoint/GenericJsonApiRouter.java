package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Sender;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Internal {@code JsonApiRouter} that implements the generic logic.
 * <p>
 * An implementation for a specific server will...
 * <ul>
 *     <li>provide a static factory method, {@code of()}; the method accepts a varargs list of HTTP handlers
 *         whose type is a {@code JsonApiHandler} implementation for the server.
 *         The protected {@code addHttpHandler()} method can be called to add these HTTP handlers to the router.
 *     <li>implement the protected abstract methods, which contain server-specific logic.
 * </ul>
 */
abstract class GenericJsonApiRouter<E> implements JsonApiRouter<E> {

    private final Node<E> root = Node.createRoot();

    @Override
    public final void handleRequest(E httpExchange) throws Exception {
        Sender sender = createErrorCodeSender(httpExchange);

        Optional<HttpMethod> maybeMethod = tryGetMethod(httpExchange);
        if (maybeMethod.isEmpty()) {
            sender.sendErrorCode(400);
            return;
        }
        HttpMethod method = maybeMethod.get();

        String relativePath = getRelativePath(httpExchange);
        String[] relativePathSegments = splitPath(relativePath);

        Node<E> node = root;
        for (String pathSegment : relativePathSegments) {
            Optional<Node<E>> maybeNode = node.tryGetChild(pathSegment);
            if (maybeNode.isEmpty()) {
                sender.sendErrorCode(404);
                return;
            }

            node = maybeNode.get();
        }

        Optional<JsonApiHandler<E>> maybeHttpHandler = node.tryGetHandler(method);
        if (maybeHttpHandler.isEmpty()) {
            sender.sendErrorCode(405);
            return;
        }
        JsonApiHandler<E> httpHandler = maybeHttpHandler.get();

        httpHandler.handleRequest(httpExchange);
    }

    /**
     * Adds an HTTP handler for an API handler to this router.
     *
     * @param httpHandler an HTTP handler for an API handler
     */
    protected final void addHttpHandler(JsonApiHandler<E> httpHandler) {
        Node<E> node = root;
        for (String pathSegment : httpHandler.getRelativePathSegments()) {
            node = node.getOrCreateChild(pathSegment);
        }
        node.setHandler(httpHandler.getMethod(), httpHandler);
    }

    /**
     * Creates a {@code Sender} for sending an error status code.
     *
     * @param httpExchange the underlying HTTP exchange
     * @return a {@code Sender} for sending an error status code
     */
    protected abstract Sender createErrorCodeSender(E httpExchange);

    /**
     * Gets the HTTP method, or returns empty.
     *
     * @param httpExchange the underlying HTTP exchange
     * @return the HTTP method, or empty if the HTTP method is invalid
     */
    protected abstract Optional<HttpMethod> tryGetMethod(E httpExchange);

    /**
     * Gets the relative URL path.
     *
     * @param httpExchange the underlying HTTP exchange
     * @return the relative URL path
     */
    protected abstract String getRelativePath(E httpExchange);

    protected GenericJsonApiRouter() {}

    /** Splits the URL path into segments. */
    private static String[] splitPath(String path) {
        path = path.replaceFirst("^/", "");
        return path.split("/");
    }

    /** Trie node for the router. */
    private static final class Node<E> {

        private Map<HttpMethod, JsonApiHandler<E>> httpHandlers = new HashMap<>();
        private Map<String, Node<E>> children = new HashMap<>();

        /** Creates the root node. */
        public static <E> Node<E> createRoot() {
            return new Node<>();
        }

        /** Gets the HTTP handler for an HTTP method, or returns empty. */
        public Optional<JsonApiHandler<E>> tryGetHandler(HttpMethod method) {
            return Optional.ofNullable(httpHandlers.get(method));
        }

        /** Gets the child node for the path segment, or returns empty. */
        public Optional<Node<E>> tryGetChild(String pathSegment) {
            return Optional.ofNullable(children.get(pathSegment));
        }

        /** Sets the HTTP handler for an HTTP method. */
        public void setHandler(HttpMethod method, JsonApiHandler<E> httpHandler) {
            Optional<JsonApiHandler<E>> maybeConflictingHttpHandler =
                    Optional.ofNullable(httpHandlers.putIfAbsent(method, httpHandler));
            if (maybeConflictingHttpHandler.isPresent()) {
                throw new IllegalArgumentException("multiple HTTP handlers have the same route");
            }
        }

        /** Gets or creates the child node for the path segment. */
        public Node<E> getOrCreateChild(String pathSegment) {
            return children.computeIfAbsent(pathSegment, ps -> new Node<>());
        }

        private Node() {}
    }
}
