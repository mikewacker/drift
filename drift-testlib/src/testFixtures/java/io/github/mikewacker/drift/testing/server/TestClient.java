package io.github.mikewacker.drift.testing.server;

import okhttp3.OkHttpClient;

/** Shared client for testing. */
final class TestClient {

    private static final OkHttpClient client = new OkHttpClient();

    /** Gets the shared client. */
    public static OkHttpClient get() {
        return client;
    }

    // static class
    private TestClient() {}
}
