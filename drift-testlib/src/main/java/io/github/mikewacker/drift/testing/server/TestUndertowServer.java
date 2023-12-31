package io.github.mikewacker.drift.testing.server;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;

/**
 * JUnit extension that starts an Undertow server on localhost.
 * It can also build the underlying {@link Undertow} server from an {@link HttpHandler}.
 * One server is started for all tests.
 * <p>
 * This extension should be a static field.
 */
public final class TestUndertowServer extends TestServer<Undertow> {

    /**
     * Creates and registers an Undertow server. The server can then be retrieved via {@link TestServer#get(String)}.
     *
     * @param name the name used to register the server
     * @param underlyingServerFactory the factory that creates the underlying {@link Undertow} server
     * @return a {@code TestUndertowServer}
     */
    public static TestUndertowServer register(String name, UnderlyingFactory<Undertow> underlyingServerFactory) {
        TestUndertowServer server = new TestUndertowServer(underlyingServerFactory);
        TestServer.register(name, server);
        return server;
    }

    /**
     * Creates and registers an Undertow server that is built from an HTTP handler.
     * The server can then be retrieved via {@link TestServer#get(String)}.
     *
     * @param name the name used to register the server
     * @param rootHandlerFactory the factory that creates the root {@link HttpHandler}
     * @return a {@code TestUndertowServer}
     */
    public static TestUndertowServer register(String name, HandlerFactory rootHandlerFactory) {
        UnderlyingFactory<Undertow> underlyingServerFactory = new RootHandlerAdapter(rootHandlerFactory);
        return register(name, underlyingServerFactory);
    }

    /**
     * Creates and registers an Undertow server that is built from an HTTP handler that listens on the specified path.
     * The server can then be retrieved via {@link TestServer#get(String)}.
     * <p>
     * Requests to a different path will result in a 404 error.
     *
     * @param name the name used to register the server
     * @param prefixPath the prefix path that the HTTP handler listens on
     * @param prefixHandlerFactory  the factory that creates the {@link HttpHandler} that listens on that path
     * @return a {@code TestUndertowServer}
     */
    public static TestUndertowServer register(String name, String prefixPath, HandlerFactory prefixHandlerFactory) {
        HandlerFactory rootHandlerFactory = new PrefixHandlerAdapter(prefixPath, prefixHandlerFactory);
        return register(name, rootHandlerFactory);
    }

    @Override
    protected void startUnderlyingServer(Undertow underlyingServer, int port) {
        underlyingServer.start();
    }

    @Override
    protected void stopUnderlyingServer(Undertow underlyingServer) {
        underlyingServer.stop();
    }

    private TestUndertowServer(UnderlyingFactory<Undertow> underlyingServerFactory) {
        super(underlyingServerFactory, false);
    }

    /** Factory that creates an Undertow HTTP handler. */
    public interface HandlerFactory {

        /**
         * Creates an HTTP handler.
         *
         * @return an {@code HttpHandler}
         */
        HttpHandler createHandler();
    }

    /** {@code UnderlyingFactory} that is built from a root {@code HandlerFactory}. */
    private record RootHandlerAdapter(HandlerFactory rootHandlerFactory)
            implements TestServer.UnderlyingFactory<Undertow> {

        @Override
        public Undertow createUnderlyingServer(int port) {
            HttpHandler rootHandler = rootHandlerFactory.createHandler();
            return Undertow.builder()
                    .addHttpListener(port, "localhost")
                    .setHandler(rootHandler)
                    .build();
        }
    }

    /** Root {@code HandlerFactory} that is built from a prefix {@code HandlerFactory}. */
    private record PrefixHandlerAdapter(String prefixPath, HandlerFactory prefixHandlerFactory)
            implements HandlerFactory {

        @Override
        public HttpHandler createHandler() {
            HttpHandler prefixHandler = prefixHandlerFactory.createHandler();
            PathHandler rootHandler = new PathHandler();
            rootHandler.addPrefixPath(prefixPath, prefixHandler);
            return rootHandler;
        }
    }
}
