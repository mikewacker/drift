package io.github.mikewacker.drift.backend;

import io.github.mikewacker.drift.api.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * Provides a shared client that uses the worker of the underlying server.
 * It is lazily initialized on the first request.
 */
final class DispatcherOkHttpClientProvider {

    private volatile OkHttpClient client;
    private final Object lock = new Object();

    /** Creates a provider for the shared client. */
    public static DispatcherOkHttpClientProvider create() {
        return new DispatcherOkHttpClientProvider();
    }

    /** Gets the shared client. */
    public OkHttpClient get(Dispatcher dispatcher) {
        // Use double-checked locking.
        OkHttpClient localClient = client;
        if (localClient == null) {
            synchronized (lock) {
                localClient = client;
                if (localClient == null) {
                    localClient = createClient(dispatcher);
                    client = localClient;
                }
            }
        }
        return localClient;
    }

    /** Creates the shared client using the worker of the underlying server. */
    private static OkHttpClient createClient(Dispatcher dispatcher) {
        okhttp3.Dispatcher clientDispatcher = new okhttp3.Dispatcher(dispatcher.getWorker());
        return new OkHttpClient.Builder().dispatcher(clientDispatcher).build();
    }

    private DispatcherOkHttpClientProvider() {}
}
