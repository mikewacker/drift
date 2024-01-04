package io.github.mikewacker.drift.backend;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.Sender;

/** API methods for testing. */
interface ProxyApi {

    void proxyStatusCode(Sender.StatusCode sender, Dispatcher dispatcher);

    void proxyText(Sender.Value<String> sender, Dispatcher dispatcher);
}
