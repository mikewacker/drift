package io.github.mikewacker.drift.testing.api;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Stub {@link ExecutorService} that no-ops. Operations that wait for a command to complete
 * or shutdown the executor will throw an {@code UnsupportedOperationException}.
 */
public final class StubExecutorService implements ExecutorService {

    private static final ExecutorService instance = new StubExecutorService();

    /**
     * Gets the stub executor.
     *
     * @return the stub {@code ExecutorService}
     */
    public static ExecutorService get() {
        return instance;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public void execute(Runnable command) {}

    @SuppressWarnings("unchecked")
    @Override
    public <T> Future<T> submit(Callable<T> command) {
        return (Future<T>) StubFuture.getInstance();
    }

    @Override
    public Future<?> submit(Runnable command) {
        return StubFuture.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Future<T> submit(Runnable command, T result) {
        return (Future<T>) StubFuture.getInstance();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> commands) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> commands, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> commands) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> commands, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    private StubExecutorService() {}

    /** Stub {@code Future} that no-ops. Getting the result will throw an {@code UnsupportedOperationException}. */
    private static final class StubFuture implements Future<Object> {

        private static final Future<Object> instance = new StubFuture();

        /** Gets the stub {@code Future}. */
        public static Future<Object> getInstance() {
            return instance;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public Object get() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(long timeout, TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }
    }
}
