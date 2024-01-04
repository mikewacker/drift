package io.github.mikewacker.drift.example;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.Sender;

/** Asynchronous API for greetings. */
public interface GreetingApi {

    /** Sends a greeting to the recipient. */
    void sendGreeting(Sender.Value<String> sender, String recipient, Dispatcher dispatcher);

    /** Sends a 200 status code as a health signal. */
    void healthCheck(Sender.StatusCode sender, Dispatcher dispatcher);
}
