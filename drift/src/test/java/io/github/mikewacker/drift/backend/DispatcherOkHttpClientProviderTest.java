package io.github.mikewacker.drift.backend;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.testing.api.StubDispatcher;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

public final class DispatcherOkHttpClientProviderTest {

    @Test
    public void get() {
        DispatcherOkHttpClientProvider clientProvider = DispatcherOkHttpClientProvider.create();
        Dispatcher dispatcher = StubDispatcher.get();
        OkHttpClient client1 = clientProvider.get(dispatcher);
        OkHttpClient client2 = clientProvider.get(dispatcher);
        assertThat(client1).isSameAs(client2);
    }
}
