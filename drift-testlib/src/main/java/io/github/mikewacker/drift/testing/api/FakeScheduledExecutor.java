package io.github.mikewacker.drift.testing.api;

import io.github.mikewacker.drift.api.ScheduledExecutor;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/** Fake, same-thread {@link ScheduledExecutor} where scheduled tasks must be run manually. */
public final class FakeScheduledExecutor implements ScheduledExecutor {

    private final AtomicReference<ScheduledTask> lastScheduledTask = new AtomicReference<>(null);

    /**
     * Creates a fake executor.
     *
     * @return a new {@code FakeScheduledExecutor}
     */
    public static FakeScheduledExecutor create() {
        return new FakeScheduledExecutor();
    }

    /**
     * Gets the last scheduled task.
     *
     * @return a {@link ScheduledTask}
     */
    public ScheduledTask getLastScheduledTask() {
        ScheduledTask scheduledTask = lastScheduledTask.get();
        if (scheduledTask == null) {
            throw new IllegalStateException("no tasks have been scheduled");
        }

        return scheduledTask;
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }

    @Override
    public Key executeAfter(Runnable command, Duration delay) {
        ScheduledTask scheduledTask = new ScheduledTask(command, delay);
        lastScheduledTask.set(scheduledTask);
        return scheduledTask;
    }

    private FakeScheduledExecutor() {}

    /** Scheduled task that must be run manually. */
    public static final class ScheduledTask implements Runnable, Key {

        private final Runnable command;
        private final Duration delay;

        private final AtomicBoolean canRun = new AtomicBoolean(true);

        /**
         * Gets the delay before the scheduled task would be executed in a real environment.
         *
         * @return the delay
         */
        public Duration getDelay() {
            return delay;
        }

        @Override
        public void run() {
            if (!canRun.getAndSet(false)) {
                return;
            }

            command.run();
        }

        @Override
        public boolean cancel() {
            return canRun.getAndSet(false);
        }

        private ScheduledTask(Runnable command, Duration delay) {
            this.command = command;
            this.delay = delay;
        }
    }
}
