package io.github.mikewacker.drift.api;

import java.time.Duration;
import java.util.concurrent.Executor;

/** {@link Executor} that can also schedule cancellable tasks. */
public interface ScheduledExecutor extends Executor {

    /**
     * Executes the command after a delay, returning a task key that can cancel the scheduled task.
     *
     * @param command the command to execute
     * @param delay the delay before the command is executed
     * @return a task key that can cancel the scheduled task
     */
    Key executeAfter(Runnable command, Duration delay);

    /** Task key that can cancel a scheduled task. */
    @FunctionalInterface
    interface Key {

        /**
         * Tries to cancel the scheduled task.
         *
         * @return true if the task was cancelled, or false is the task has already run or was already cancelled
         */
        boolean cancel();
    }
}
