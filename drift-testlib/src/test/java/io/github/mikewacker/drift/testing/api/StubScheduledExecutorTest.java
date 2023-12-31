package io.github.mikewacker.drift.testing.api;

import io.github.mikewacker.drift.api.ScheduledExecutor;
import java.time.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class StubScheduledExecutorTest {

    private static ScheduledExecutor executor;

    private static Runnable command;

    @BeforeAll
    public static void createStubScheduledExecutorEtAl() {
        executor = StubScheduledExecutor.get();
        command = () -> {};
    }

    @Test
    public void execute() {
        executor.execute(command);
    }

    @Test
    public void executeAfter() {
        ScheduledExecutor.Key key = executor.executeAfter(command, Duration.ofSeconds(1));
        key.cancel();
    }
}
