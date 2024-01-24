package io.github.mikewacker.drift.example;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.backend.BackendDispatcher;
import java.util.function.Supplier;

/** Service for {@code GreetingApi}. */
public final class GreetingService implements GreetingApi {

    private final Supplier<String> salutationUrlProvider;
    private final BackendDispatcher backendDispatcher = BackendDispatcher.create();

    /** Creates a greeting service. */
    public static GreetingApi create(Supplier<String> salutationUrlProvider) {
        return new GreetingService(salutationUrlProvider);
    }

    @Override
    public void sendGreeting(Sender.Value<String> sender, String recipient, Dispatcher dispatcher) {
        backendDispatcher
                .requestBuilder()
                .jsonResponse(new TypeReference<String>() {})
                .get(salutationUrlProvider.get())
                .build()
                .dispatch(sender, recipient, dispatcher, this::onSalutationReceived);
    }

    @Override
    public void healthCheck(Sender.StatusCode sender, Dispatcher dispatcher) {
        sender.sendOk();
    }

    /** Called when a salutation is received from the backend. */
    private void onSalutationReceived(
            Sender.Value<String> sender,
            String recipient,
            HttpOptional<String> maybeSalutation,
            Dispatcher dispatcher) {
        if (maybeSalutation.isEmpty()) {
            sender.sendErrorCode(500);
            return;
        }
        String salutation = maybeSalutation.get();

        String greeting = String.format("%s, %s!", salutation, recipient);
        sender.sendValue(greeting);
    }

    private GreetingService(Supplier<String> salutationUrlProvider) {
        this.salutationUrlProvider = salutationUrlProvider;
    }
}
