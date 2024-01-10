package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.ScheduledExecutor;
import io.github.mikewacker.drift.api.Sender;
import io.undertow.server.Connectors;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.SameThreadExecutor;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.xnio.XnioExecutor;

/** {@code Dispatcher} that is backed by an Undertow {@code HttpServerExchange}. */
final class UndertowDispatcher implements Dispatcher {

    private final HttpServerExchange httpExchange;
    private final ScheduledExecutor ioThread;
    private final ExecutorService worker;

    /** Creates the dispatcher from the HTTP exchange. */
    public static Dispatcher create(HttpServerExchange httpExchange) {
        return new UndertowDispatcher(httpExchange);
    }

    @Override
    public boolean isInIoThread() {
        return httpExchange.isInIoThread();
    }

    @Override
    public ScheduledExecutor getIoThread() {
        return ioThread;
    }

    @Override
    public ExecutorService getWorker() {
        return worker;
    }

    @Override
    public <S extends Sender> void dispatch(S sender, ApiHandler.ZeroArg<S> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, this));
    }

    @Override
    public <S extends Sender, A> void dispatch(S sender, A arg, ApiHandler.OneArg<S, A> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, arg, this));
    }

    @Override
    public <S extends Sender, A1, A2> void dispatch(S sender, A1 arg1, A2 arg2, ApiHandler.TwoArg<S, A1, A2> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, arg1, arg2, this));
    }

    @Override
    public <S extends Sender, A1, A2, A3> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, ApiHandler.ThreeArg<S, A1, A2, A3> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, arg1, arg2, arg3, this));
    }

    @Override
    public <S extends Sender, A1, A2, A3, A4> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, ApiHandler.FourArg<S, A1, A2, A3, A4> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, arg1, arg2, arg3, arg4, this));
    }

    @Override
    public <S extends Sender, A1, A2, A3, A4, A5> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, ApiHandler.FiveArg<S, A1, A2, A3, A4, A5> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, arg1, arg2, arg3, arg4, arg5, this));
    }

    @Override
    public <S extends Sender, A1, A2, A3, A4, A5, A6> void dispatch(
            S sender,
            A1 arg1,
            A2 arg2,
            A3 arg3,
            A4 arg4,
            A5 arg5,
            A6 arg6,
            ApiHandler.SixArg<S, A1, A2, A3, A4, A5, A6> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, arg1, arg2, arg3, arg4, arg5, arg6, this));
    }

    @Override
    public <S extends Sender, A1, A2, A3, A4, A5, A6, A7> void dispatch(
            S sender,
            A1 arg1,
            A2 arg2,
            A3 arg3,
            A4 arg4,
            A5 arg5,
            A6 arg6,
            A7 arg7,
            ApiHandler.SevenArg<S, A1, A2, A3, A4, A5, A6, A7> handler) {
        httpExchange.dispatch(he -> handler.handleRequest(sender, arg1, arg2, arg3, arg4, arg5, arg6, arg7, this));
    }

    @Override
    public <S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8> void dispatch(
            S sender,
            A1 arg1,
            A2 arg2,
            A3 arg3,
            A4 arg4,
            A5 arg5,
            A6 arg6,
            A7 arg7,
            A8 arg8,
            ApiHandler.EightArg<S, A1, A2, A3, A4, A5, A6, A7, A8> handler) {
        httpExchange.dispatch(
                he -> handler.handleRequest(sender, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, this));
    }

    @Override
    public void dispatched() {
        httpExchange.dispatch(SameThreadExecutor.INSTANCE, () -> {});
    }

    @Override
    public void executeHandler(DispatchedHandler handler) {
        Connectors.executeRootHandler(he -> handler.handleRequest(), httpExchange);
    }

    private UndertowDispatcher(HttpServerExchange httpExchange) {
        this.httpExchange = httpExchange;
        ioThread = new XnioScheduledExecutor(httpExchange.getIoThread());
        worker = httpExchange.getConnection().getWorker();
    }

    /** {@code ScheduledExecutor} that is backed by a {@code XnioExecutor}. */
    private record XnioScheduledExecutor(XnioExecutor executor) implements ScheduledExecutor {

        @Override
        public void execute(Runnable command) {
            executor.execute(command);
        }

        @Override
        public Key executeAfter(Runnable command, Duration delay) {
            XnioExecutor.Key key = executor.executeAfter(command, delay.toMillis(), TimeUnit.MILLISECONDS);
            return new XnioKey(key);
        }

        /** {@code ScheduledExecutor.Key} that is backed by a {@code XnioExecutor.Key}. */
        private record XnioKey(XnioExecutor.Key key) implements Key {

            @Override
            public boolean cancel() {
                return key.remove();
            }
        }
    }
}
