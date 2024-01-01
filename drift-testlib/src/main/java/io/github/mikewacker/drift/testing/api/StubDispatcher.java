package io.github.mikewacker.drift.testing.api;

import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.ScheduledExecutor;
import io.github.mikewacker.drift.api.Sender;
import java.util.concurrent.ExecutorService;

/**
 * Stub {@link Dispatcher} that no-ops. It is always in the IO thread.
 * Operations that wait for a command to complete will throw an {@code UnsupportedOperationException}.
 */
public final class StubDispatcher implements Dispatcher {

    private static final Dispatcher instance = new StubDispatcher();

    /**
     * Gets the stub dispatcher
     *
     * @return the stub {@code Dispatcher}
     */
    public static Dispatcher get() {
        return instance;
    }

    @Override
    public boolean isInIoThread() {
        return true;
    }

    @Override
    public ScheduledExecutor getIoThread() {
        return StubScheduledExecutor.get();
    }

    @Override
    public ExecutorService getWorker() {
        return StubExecutorService.get();
    }

    @Override
    public <S extends Sender> void dispatch(S sender, ApiHandler.ZeroArg<S> handler) {}

    @Override
    public <S extends Sender, A> void dispatch(S sender, A arg, ApiHandler.OneArg<S, A> handler) {}

    @Override
    public <S extends Sender, A1, A2> void dispatch(S sender, A1 arg1, A2 arg2, ApiHandler.TwoArg<S, A1, A2> handler) {}

    @Override
    public <S extends Sender, A1, A2, A3> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, ApiHandler.ThreeArg<S, A1, A2, A3> handler) {}

    @Override
    public <S extends Sender, A1, A2, A3, A4> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, ApiHandler.FourArg<S, A1, A2, A3, A4> handler) {}

    @Override
    public void dispatched() {}

    @Override
    public void executeHandler(DispatchedHandler handler) {
        try {
            handler.handleRequest();
        } catch (Exception e) {
            throw new RuntimeException("handler threw an uncaught exception", e);
        }
    }

    private StubDispatcher() {}
}
