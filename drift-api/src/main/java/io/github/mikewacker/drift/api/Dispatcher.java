package io.github.mikewacker.drift.api;

import java.util.concurrent.ExecutorService;

/**
 * Dispatcher for an API request that can hand off or dispatch this request to a worker thread.
 * Requests are handled on an IO thread, so requests that block should be dispatched to a worker thread.
 * The dispatcher can also schedule tasks.
 * <p>
 * The request can be {@link #dispatch}'ed to an {@code ApiHandler} that will run on a worker thread,
 * or {@link #dispatched()} can be called if the request is manually dispatched.
 * The dispatched {@code ApiHandler} can have a different signature than the original {@code ApiHandler};
 * this may naturally occur when the request can be partially processed before it is dispatched.
 * <p>
 * The behavior is undefined if the response is not sent and the request is not dispatched.
 */
public interface Dispatcher {

    /**
     * Determines if the current thread is an IO thread.
     *
     * @return true if the current thread is an IO thread, otherwise false
     */
    boolean isInIoThread();

    /**
     * Gets the IO thread for this request, which can also schedule tasks.
     *
     * @return a {@link ScheduledExecutor} for the IO thread
     */
    ScheduledExecutor getIoThread();

    /**
     * Gets the worker thread pool.
     *
     * @return an {@link ExecutorService} for the worker thread pool
     */
    ExecutorService getWorker();

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     */
    <S extends Sender> void dispatch(S sender, ApiHandler.ZeroArg<S> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg the argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A> the type of the argument
     */
    <S extends Sender, A> void dispatch(S sender, A arg, ApiHandler.OneArg<S, A> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg1 the first argument to dispatch
     * @param arg2 the second argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     */
    <S extends Sender, A1, A2> void dispatch(S sender, A1 arg1, A2 arg2, ApiHandler.TwoArg<S, A1, A2> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg1 the first argument to dispatch
     * @param arg2 the second argument to dispatch
     * @param arg3 the third argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     */
    <S extends Sender, A1, A2, A3> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, ApiHandler.ThreeArg<S, A1, A2, A3> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg1 the first argument to dispatch
     * @param arg2 the second argument to dispatch
     * @param arg3 the third argument to dispatch
     * @param arg4 the fourth argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     */
    <S extends Sender, A1, A2, A3, A4> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, ApiHandler.FourArg<S, A1, A2, A3, A4> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg1 the first argument to dispatch
     * @param arg2 the second argument to dispatch
     * @param arg3 the third argument to dispatch
     * @param arg4 the fourth argument to dispatch
     * @param arg5 the fifth argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     * @param <A5> the type of the fifth argument
     */
    <S extends Sender, A1, A2, A3, A4, A5> void dispatch(
            S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, ApiHandler.FiveArg<S, A1, A2, A3, A4, A5> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg1 the first argument to dispatch
     * @param arg2 the second argument to dispatch
     * @param arg3 the third argument to dispatch
     * @param arg4 the fourth argument to dispatch
     * @param arg5 the fifth argument to dispatch
     * @param arg6 the sixth argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     * @param <A5> the type of the fifth argument
     * @param <A6> the type of the sixth argument
     */
    <S extends Sender, A1, A2, A3, A4, A5, A6> void dispatch(
            S sender,
            A1 arg1,
            A2 arg2,
            A3 arg3,
            A4 arg4,
            A5 arg5,
            A6 arg6,
            ApiHandler.SixArg<S, A1, A2, A3, A4, A5, A6> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg1 the first argument to dispatch
     * @param arg2 the second argument to dispatch
     * @param arg3 the third argument to dispatch
     * @param arg4 the fourth argument to dispatch
     * @param arg5 the fifth argument to dispatch
     * @param arg6 the sixth argument to dispatch
     * @param arg7 the seventh argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     * @param <A5> the type of the fifth argument
     * @param <A6> the type of the sixth argument
     * @param <A7> the type of the seventh argument
     */
    <S extends Sender, A1, A2, A3, A4, A5, A6, A7> void dispatch(
            S sender,
            A1 arg1,
            A2 arg2,
            A3 arg3,
            A4 arg4,
            A5 arg5,
            A6 arg6,
            A7 arg7,
            ApiHandler.SevenArg<S, A1, A2, A3, A4, A5, A6, A7> handler);

    /**
     * Dispatches this request to the worker thread pool.
     *
     * @param sender the response sender, for when the response can be sent
     * @param arg1 the first argument to dispatch
     * @param arg2 the second argument to dispatch
     * @param arg3 the third argument to dispatch
     * @param arg4 the fourth argument to dispatch
     * @param arg5 the fifth argument to dispatch
     * @param arg6 the sixth argument to dispatch
     * @param arg7 the seventh argument to dispatch
     * @param arg8 the eighth argument to dispatch
     * @param handler the API handler for the dispatched request
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     * @param <A5> the type of the fifth argument
     * @param <A6> the type of the sixth argument
     * @param <A7> the type of the seventh argument
     * @param <A8> the type of the eighth argument
     */
    <S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8> void dispatch(
            S sender,
            A1 arg1,
            A2 arg2,
            A3 arg3,
            A4 arg4,
            A5 arg5,
            A6 arg6,
            A7 arg7,
            A8 arg8,
            ApiHandler.EightArg<S, A1, A2, A3, A4, A5, A6, A7, A8> handler);

    /**
     * Called when this request is manually dispatched without calling {@code dispatch}.
     * <p>
     * The worker thread should immediately call {@link #executeHandler(DispatchedHandler)}.
     */
    void dispatched();

    /**
     * Called on the worker thread when the request is manually dispatched via {@code dispatched()}.
     *
     * @param handler the handler for the dispatched request
     */
    void executeHandler(DispatchedHandler handler);

    /** Handler for the worker thread when the request is manually dispatched via {@code dispatched()}. */
    @FunctionalInterface
    interface DispatchedHandler {

        /**
         * Handles the dispatched request.
         *
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest() throws Exception;
    }
}
