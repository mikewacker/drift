package io.github.mikewacker.drift.backend;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.Sender;

/** Asynchronous API that proxies a response from a backend server. */
interface ProxyApi {

    /** Proxies a status code that is received from a backend server. */
    void proxyStatusCode(Sender.StatusCode sender, Dispatcher dispatcher);

    /** Proxies text that is received from a backend server. */
    void proxyText(Sender.Value<String> sender, Dispatcher dispatcher);
}
