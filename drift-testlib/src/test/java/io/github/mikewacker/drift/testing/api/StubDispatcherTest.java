package io.github.mikewacker.drift.testing.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.ScheduledExecutor;
import io.github.mikewacker.drift.api.Sender;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public final class StubDispatcherTest {

    private static Dispatcher dispatcher;

    private static Runnable command;

    @BeforeAll
    public static void createStubDispatcherEtAl() {
        dispatcher = StubDispatcher.get();
        command = () -> {};
    }

    @Test
    public void isInIoThread() {
        assertThat(dispatcher.isInIoThread()).isTrue();
    }

    @Test
    public void getIoThread() {
        ScheduledExecutor ioThread = dispatcher.getIoThread();
        ScheduledExecutor.Key key = ioThread.executeAfter(command, Duration.ofMinutes(1));
        key.cancel();
    }

    @Test
    public void getWorker() {
        ExecutorService worker = dispatcher.getWorker();
        Future<Integer> future = worker.submit(command, 0);
        future.cancel(true);
    }

    @Test
    public void dispatch() {
        Sender.StatusCode sender = FakeSender.StatusCode.create();
        dispatcher.dispatch(sender, (s, d) -> {});
        dispatcher.dispatch(sender, 0, (s, arg, d) -> {});
        dispatcher.dispatch(sender, 0, 0, (s, arg1, arg2, d) -> {});
        dispatcher.dispatch(sender, 0, 0, 0, (s, arg1, arg2, arg3, d) -> {});
        dispatcher.dispatch(sender, 0, 0, 0, 0, (s, arg1, arg2, arg3, arg4, d) -> {});
    }

    @Test
    public void dispatched() {
        dispatcher.dispatched();
    }

    @Test
    public void executeHandler() {
        AtomicBoolean wasRun = new AtomicBoolean(false);
        dispatcher.executeHandler(() -> wasRun.set(true));
        assertThat(wasRun.get()).isTrue();
    }
}
