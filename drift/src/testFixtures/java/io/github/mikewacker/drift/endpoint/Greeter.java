package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.Sender;

/** Asynchronous API that sends a greeting. It also has a health check. */
final class Greeter {

    /** Sends a greeting to the recipient. */
    public static void sendGreeting(Sender.Value<String> sender, String recipient, Dispatcher dispatcher) {
        String greeting = String.format("Hello, %s!", recipient);
        sender.sendValue(greeting);
    }

    /** Sends a 200 status code as a health signal. */
    public static void healthCheck(Sender.StatusCode sender, Dispatcher dispatcher) {
        sender.sendOk();
    }

    // static class
    private Greeter() {}
}
