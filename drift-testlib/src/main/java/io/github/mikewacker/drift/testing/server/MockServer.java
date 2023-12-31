package io.github.mikewacker.drift.testing.server;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * JUnit extension that starts a mock server on localhost. It is backed by a {@link MockWebServer}.
 * A fresh server is started for each test.
 * <p>
 * This extension should be a static field.
 */
public final class MockServer extends TestServer<MockWebServer> {

    /**
     * Creates and registers a mock server. The server can then be retrieved via {@link TestServer#get(String)}.
     *
     * @param name the name used to register the server
     * @return a {@code MockServer}
     */
    public static MockServer register(String name) {
        MockServer server = new MockServer();
        TestServer.register(name, server);
        return server;
    }

    /**
     * Wraps {@link MockWebServer#enqueue(MockResponse)}.
     *
     * @param response the mock response
     */
    public void enqueue(MockResponse response) {
        get().enqueue(response);
    }

    @Override
    protected void startUnderlyingServer(MockWebServer underlyingServer, int port) throws IOException {
        underlyingServer.start(port);
    }

    @Override
    protected void stopUnderlyingServer(MockWebServer underlyingServer) throws IOException {
        underlyingServer.shutdown();
    }

    private MockServer() {
        super(MockServer::create, true);
    }

    /** Creates the underlying server. */
    private static MockWebServer create(int port) {
        return new MockWebServer();
    }
}
