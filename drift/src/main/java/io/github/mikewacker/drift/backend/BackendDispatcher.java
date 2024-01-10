package io.github.mikewacker.drift.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.client.ApiClient;

/**
 * A dispatcher for a frontend request that can asynchronously send backend requests to a backend server.
 * <p>
 * Failures for a backend request are not handled by a callback; the frontend server will send a 502 error.
 * <p>
 * The frontend server should create and share a single {@code BackendDispatcher}; each instance creates a new client.
 */
public interface BackendDispatcher extends ApiClient {

    /**
     * Creates a dispatcher.
     *
     * @return a new {@code BackendDispatcher}
     */
    static BackendDispatcher create() {
        return BackendDispatcherImpl.create();
    }

    /**
     * Creates a staged builder for a backend JSON API request whose response is only an HTTP status code.
     *
     * @return a backend request builder at the initial stage
     */
    UrlStageRequestBuilder<DispatchStage<Integer>> requestBuilder();

    /**
     * Creates a staged builder for a backend JSON API request whose response is an {@link HttpOptional} value.
     *
     * @param responseValueTypeRef a {@link TypeReference} for the backend response value
     * @return a backend request builder at the initial stage
     * @param <V> the type of the backend response value
     */
    <V> UrlStageRequestBuilder<DispatchStage<HttpOptional<V>>> requestBuilder(TypeReference<V> responseValueTypeRef);

    /**
     * Post-build stage that can asynchronously send this backend request.
     *
     * @param <R> the type of the backend response, either an HTTP {@code Integer} status code
     *     or an {@link HttpOptional} value
     */
    interface DispatchStage<R> {

        /**
         * Sends this backend request asynchronously.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         */
        <S extends Sender> void dispatch(S sender, Dispatcher dispatcher, ApiHandler.OneArg<S, R> callback);

        /**
         * Sends this backend request asynchronously, passing one additional argument along to the callback.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param arg the argument to dispatch
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         * @param <A> the type of the argument
         */
        default <S extends Sender, A> void dispatch(
                S sender, A arg, Dispatcher dispatcher, ApiHandler.TwoArg<S, A, R> callback) {
            dispatch(sender, dispatcher, (s, response, d) -> callback.handleRequest(s, arg, response, d));
        }

        /**
         * Sends this backend request asynchronously, passing two additional arguments along to the callback.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param arg1 the first argument to dispatch
         * @param arg2 the second argument to dispatch
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         * @param <A1> the type of the first argument
         * @param <A2> the type of the second argument
         */
        default <S extends Sender, A1, A2> void dispatch(
                S sender, A1 arg1, A2 arg2, Dispatcher dispatcher, ApiHandler.ThreeArg<S, A1, A2, R> callback) {
            dispatch(sender, dispatcher, (s, response, d) -> callback.handleRequest(s, arg1, arg2, response, d));
        }

        /**
         * Sends this backend request asynchronously, passing three additional arguments along to the callback.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param arg1 the first argument to dispatch
         * @param arg2 the second argument to dispatch
         * @param arg3 the third argument to dispatch
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         * @param <A1> the type of the first argument
         * @param <A2> the type of the second argument
         * @param <A3> the type of the third argument
         */
        default <S extends Sender, A1, A2, A3> void dispatch(
                S sender,
                A1 arg1,
                A2 arg2,
                A3 arg3,
                Dispatcher dispatcher,
                ApiHandler.FourArg<S, A1, A2, A3, R> callback) {
            dispatch(sender, dispatcher, (s, response, d) -> callback.handleRequest(s, arg1, arg2, arg3, response, d));
        }

        /**
         * Sends this backend request asynchronously, passing four additional arguments along to the callback.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param arg1 the first argument to dispatch
         * @param arg2 the second argument to dispatch
         * @param arg3 the third argument to dispatch
         * @param arg4 the fourth argument to dispatch
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         * @param <A1> the type of the first argument
         * @param <A2> the type of the second argument
         * @param <A3> the type of the third argument
         * @param <A4> the type of the fourth argument
         */
        default <S extends Sender, A1, A2, A3, A4> void dispatch(
                S sender,
                A1 arg1,
                A2 arg2,
                A3 arg3,
                A4 arg4,
                Dispatcher dispatcher,
                ApiHandler.FiveArg<S, A1, A2, A3, A4, R> callback) {
            dispatch(
                    sender,
                    dispatcher,
                    (s, response, d) -> callback.handleRequest(s, arg1, arg2, arg3, arg4, response, d));
        }

        /**
         * Sends this backend request asynchronously, passing five additional arguments along to the callback.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param arg1 the first argument to dispatch
         * @param arg2 the second argument to dispatch
         * @param arg3 the third argument to dispatch
         * @param arg4 the fourth argument to dispatch
         * @param arg5 the fifth argument to dispatch
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         * @param <A1> the type of the first argument
         * @param <A2> the type of the second argument
         * @param <A3> the type of the third argument
         * @param <A4> the type of the fourth argument
         * @param <A5> the type of the fifth argument
         */
        default <S extends Sender, A1, A2, A3, A4, A5> void dispatch(
                S sender,
                A1 arg1,
                A2 arg2,
                A3 arg3,
                A4 arg4,
                A5 arg5,
                Dispatcher dispatcher,
                ApiHandler.SixArg<S, A1, A2, A3, A4, A5, R> callback) {
            dispatch(
                    sender,
                    dispatcher,
                    (s, response, d) -> callback.handleRequest(s, arg1, arg2, arg3, arg4, arg5, response, d));
        }

        /**
         * Sends this backend request asynchronously, passing six additional arguments along to the callback.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param arg1 the first argument to dispatch
         * @param arg2 the second argument to dispatch
         * @param arg3 the third argument to dispatch
         * @param arg4 the fourth argument to dispatch
         * @param arg5 the fifth argument to dispatch
         * @param arg6 the sixth argument to dispatch
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         * @param <A1> the type of the first argument
         * @param <A2> the type of the second argument
         * @param <A3> the type of the third argument
         * @param <A4> the type of the fourth argument
         * @param <A5> the type of the fifth argument
         * @param <A6> the type of the sixth argument
         */
        default <S extends Sender, A1, A2, A3, A4, A5, A6> void dispatch(
                S sender,
                A1 arg1,
                A2 arg2,
                A3 arg3,
                A4 arg4,
                A5 arg5,
                A6 arg6,
                Dispatcher dispatcher,
                ApiHandler.SevenArg<S, A1, A2, A3, A4, A5, A6, R> callback) {
            dispatch(
                    sender,
                    dispatcher,
                    (s, response, d) -> callback.handleRequest(s, arg1, arg2, arg3, arg4, arg5, arg6, response, d));
        }

        /**
         * Sends this backend request asynchronously, passing seven additional arguments along to the callback.
         *
         * @param sender the response sender for the frontend request, for when the response can be sent
         * @param arg1 the first argument to dispatch
         * @param arg2 the second argument to dispatch
         * @param arg3 the third argument to dispatch
         * @param arg4 the fourth argument to dispatch
         * @param arg5 the fifth argument to dispatch
         * @param arg6 the sixth argument to dispatch
         * @param arg7 the seventh argument to dispatch
         * @param dispatcher the {@link Dispatcher} for the frontend request
         * @param callback an {@link ApiHandler} that will process the backend response value
         * @param <S> the interface for the response sender for the frontend request
         * @param <A1> the type of the first argument
         * @param <A2> the type of the second argument
         * @param <A3> the type of the third argument
         * @param <A4> the type of the fourth argument
         * @param <A5> the type of the fifth argument
         * @param <A6> the type of the sixth argument
         * @param <A7> the type of the seventh argument
         */
        default <S extends Sender, A1, A2, A3, A4, A5, A6, A7> void dispatch(
                S sender,
                A1 arg1,
                A2 arg2,
                A3 arg3,
                A4 arg4,
                A5 arg5,
                A6 arg6,
                A7 arg7,
                Dispatcher dispatcher,
                ApiHandler.EightArg<S, A1, A2, A3, A4, A5, A6, A7, R> callback) {
            dispatch(
                    sender,
                    dispatcher,
                    (s, response, d) ->
                            callback.handleRequest(s, arg1, arg2, arg3, arg4, arg5, arg6, arg7, response, d));
        }
    }
}
