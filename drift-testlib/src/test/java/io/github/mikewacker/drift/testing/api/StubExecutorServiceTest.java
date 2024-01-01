package io.github.mikewacker.drift.testing.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class StubExecutorServiceTest {

    private static ExecutorService executor;

    private static Runnable command1;
    private static Callable<Void> command2;
    private static Collection<Callable<Void>> commands;

    @BeforeAll
    public static void createStubExecutorServiceEtAl() {
        executor = StubExecutorService.get();
        command1 = () -> {};
        command2 = () -> null;
        commands = List.of(command2);
    }

    @Test
    public void execute() {
        executor.execute(command1);
    }

    @Test
    public void submit() {
        Future<Void> future1 = executor.submit(command2);
        assertThat(future1).isNotNull();
        Future<?> future2 = executor.submit(command1);
        assertThat(future2).isNotNull();
        Future<Integer> future3 = executor.submit(command1, 0);
        assertThat(future3).isNotNull();
    }

    @Test
    public void future() {
        Future<Void> future = executor.submit(command2);
        assertThat(future.isDone()).isFalse();
        assertThat(future.isCancelled()).isFalse();
        unsupported(future::get);
        unsupported(() -> future.get(1, TimeUnit.MILLISECONDS));
        boolean wasCancelled = future.cancel(true);
        assertThat(wasCancelled).isFalse();
    }

    @Test
    public void invoke() {
        unsupported(() -> executor.invokeAll(commands));
        unsupported(() -> executor.invokeAll(commands, 1, TimeUnit.MILLISECONDS));
        unsupported(() -> executor.invokeAny(commands));
        unsupported(() -> executor.invokeAny(commands, 1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void isShutdown() {
        assertThat(executor.isShutdown()).isFalse();
        assertThat(executor.isTerminated()).isFalse();
    }

    @Test
    public void shutdown() {
        unsupported(executor::shutdown);
        unsupported(executor::shutdownNow);
        unsupported(() -> executor.awaitTermination(1, TimeUnit.MILLISECONDS));
    }

    private static void unsupported(ThrowableAssert.ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOf(UnsupportedOperationException.class);
    }
}
