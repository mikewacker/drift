package io.github.mikewacker.drift.testing.server;

import com.google.errorprone.annotations.FormatMethod;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit extension that starts a server on localhost.
 * Servers are registered with a name; a {@code TestServer} can be retrieved by name via {@link #get(String)}.
 * <p>
 * This extension should be a static field, though some implementations may start a fresh server for each test.
 * <p>
 * Implementations will provide static {@code register()} method(s) that both create and register a {@code TestServer}.
 *
 * @param <S> the type of the underlying server
 */
public abstract class TestServer<S>
        implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    // AfterAll clears this, but that obviously may not work if multiple test classes run in parallel.
    private static final Map<String, TestServer<?>> servers = new ConcurrentHashMap<>();

    private final UnderlyingFactory<S> underlyingServerFactory;
    private final boolean createForEachTest;

    private S underlyingServer;
    private int port;
    private String rootUrl;

    /**
     * Gets a registered server.
     *
     * @param name the name used to register the server
     * @return a {@code TestServer}
     */
    public static TestServer<?> get(String name) {
        TestServer<?> server = servers.get(name);
        if (server == null) {
            throw new NoSuchElementException(name);
        }

        return server;
    }

    /**
     * Gets the host of the server.
     *
     * @return {@code localhost}
     */
    public final String host() {
        checkServerStarted();
        return "localhost";
    }

    /**
     * Gets the port of the server.
     *
     * @return a port
     */
    public final int port() {
        checkServerStarted();
        return port;
    }

    /**
     * Gets the root URL of the server.
     *
     * @return the root URL
     */
    public final String rootUrl() {
        checkServerStarted();
        return rootUrl;
    }

    /**
     * Gets the URL at the specified path.
     *
     * @param path the URL path
     * @return a URL
     */
    public final String url(String path) {
        path = path.replaceFirst("^/", "");
        return String.format("%s/%s", rootUrl(), path);
    }

    /** Gets the URL at the specified path.
     *
     * @param pathFormat the format string for the URL path
     * @param args the format arguments for the URL path
     * @return a URL
     */
    @FormatMethod
    public final String url(String pathFormat, Object... args) {
        String path = String.format(pathFormat, args);
        return url(path);
    }

    /**
     * Gets the underlying server.
     *
     * @return the underlying server
     */
    public final S get() {
        checkServerStarted();
        return underlyingServer;
    }

    @Override
    public final void beforeAll(ExtensionContext context) throws Exception {
        if (!createForEachTest) {
            createAndStart();
        }
    }

    @Override
    public final void beforeEach(ExtensionContext context) throws Exception {
        if (createForEachTest) {
            createAndStart();
        }
    }

    @Override
    public final void afterEach(ExtensionContext context) throws Exception {
        if (createForEachTest) {
            stop();
        }
    }

    @Override
    public final void afterAll(ExtensionContext context) throws Exception {
        try {
            if (!createForEachTest) {
                stop();
            }
        } finally {
            servers.clear();
        }
    }

    /**
     * Creates a server, specifying whether a fresh underlying server is created for each test.
     *
     * @param underlyingServerFactory the factory that creates the underlying server
     * @param createForEachTest true to create a fresh underlying server for each test,
     *     or false to create one underlying server for all tests
     */
    protected TestServer(UnderlyingFactory<S> underlyingServerFactory, boolean createForEachTest) {
        this.underlyingServerFactory = underlyingServerFactory;
        this.createForEachTest = createForEachTest;
        clear();
    }

    /**
     * Registers a server.
     *
     * @param name the name used to register the server
     * @param server the server to register
     */
    protected static void register(String name, TestServer<?> server) {
        TestServer<?> conflictingServer = servers.putIfAbsent(name, server);
        if (conflictingServer != null) {
            String message = String.format("server already registered: %s", name);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Starts the underlying server on localhost.
     *
     * @param underlyingServer the underlying server
     * @param port the port that the underlying server will listen on
     * @throws Exception for any exceptions that may be thrown
     */
    protected abstract void startUnderlyingServer(S underlyingServer, int port) throws Exception;

    /**
     * Stops the underlying server.
     *
     * @param underlyingServer the underlying server
     * @throws Exception for any exceptions that may be thrown
     */
    protected abstract void stopUnderlyingServer(S underlyingServer) throws Exception;

    /** Creates and starts a fresh underlying server. */
    private void createAndStart() throws Exception {
        try {
            port = findAvailablePort();
            rootUrl = String.format("http://localhost:%d", port);
            underlyingServer = underlyingServerFactory.createUnderlyingServer(port);
            startUnderlyingServer(underlyingServer, port);
        } catch (Exception e) {
            clear();
            throw e;
        }
    }

    /** Stops the current underlying server. */
    private void stop() throws Exception {
        try {
            stopUnderlyingServer(underlyingServer);
        } finally {
            clear();
        }
    }

    /** Finds an available port. */
    private static int findAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    /** Clears the fields for the server. */
    private void clear() {
        underlyingServer = null;
        port = 0;
        rootUrl = "";
    }

    /** Checks that the underlying server has started. */
    private void checkServerStarted() {
        if (underlyingServer == null) {
            throw new IllegalStateException("server has not started");
        }
    }

    /**
     * Factory that creates an underlying server on localhost.
     *
     * @param <S> the type of the underlying server
     */
    @FunctionalInterface
    public interface UnderlyingFactory<S> {

        /**
         * Creates an underlying server on localhost that listens on the specified port.
         *
         * @param port the port to listen on
         * @return the underlying server
         */
        S createUnderlyingServer(int port);
    }
}
