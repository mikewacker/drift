package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.Sender;

/** API methods for testing. */
final class Greeter {

    public static void sendGreeting(Sender.Value<String> sender, String recipient, Dispatcher dispatcher) {
        String greeting = String.format("Hello, %s!", recipient);
        sender.sendValue(greeting);
    }

    public static void healthCheck(Sender.StatusCode sender, Dispatcher dispatcher) {
        sender.sendOk();
    }

    // static class
    private Greeter() {}
}
