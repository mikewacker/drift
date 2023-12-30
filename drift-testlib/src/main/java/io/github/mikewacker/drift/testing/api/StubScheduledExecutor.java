package io.github.mikewacker.drift.testing.api;

import io.github.mikewacker.drift.api.ScheduledExecutor;
import java.time.Duration;

/** Stub {@link ScheduledExecutor} that no-ops. */
public final class StubScheduledExecutor implements ScheduledExecutor {

    private static final ScheduledExecutor instance = new StubScheduledExecutor();
    private static final Key keyInstance = () -> false;

    /**
     * Gets the stub executor.
     *
     * @return the stub {@code ScheduledExecutor}
     */
    public static ScheduledExecutor get() {
        return instance;
    }

    @Override
    public void execute(Runnable runnable) {}

    @Override
    public Key executeAfter(Runnable command, Duration delay) {
        return keyInstance;
    }

    private StubScheduledExecutor() {}
}
