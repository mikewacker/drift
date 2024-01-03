package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;

/**
 * Internal data structure for an API request that has an API handler attached to it.
 * Some type parameters may be {@code Void}, depending on how many arguments the API request has.
 */
final class ApiRequest<S extends Sender, A1, A2, A3, A4> {

    private final Handler<S, A1, A2, A3, A4> handler;
    private final S sender;
    private final Dispatcher dispatcher;
    private A1 arg1 = null;
    private A2 arg2 = null;
    private A3 arg3 = null;
    private A4 arg4 = null;

    /** Sets the first argument. */
    public void setArg1(A1 arg1) {
        this.arg1 = arg1;
    }

    /** Sets the second argument. */
    public void setArg2(A2 arg2) {
        this.arg2 = arg2;
    }

    /** Sets the third argument. */
    public void setArg3(A3 arg3) {
        this.arg3 = arg3;
    }

    /** Sets the fourth argument. */
    public void setArg4(A4 arg4) {
        this.arg4 = arg4;
    }

    /** Handles this API request. */
    public void handle() throws Exception {
        handler.handleRequest(this);
    }

    /** Sends an error status code when an argument cannot be set. */
    public <A> void sendErrorCode(HttpOptional<A> emptyArg) {
        sender.sendErrorCode(emptyArg);
    }

    private ApiRequest(Handler<S, A1, A2, A3, A4> handler, S sender, Dispatcher dispatcher) {
        this.handler = handler;
        this.sender = sender;
        this.dispatcher = dispatcher;
    }

    /** Factory for API requests. */
    @FunctionalInterface
    public interface Factory<S extends Sender, A1, A2, A3, A4> {

        /** Creates a factory for API requests with zero arguments. */
        static <S extends Sender> Factory<S, Void, Void, Void, Void> zeroArg(ApiHandler.ZeroArg<S> handler) {
            return new FactoryImpl<>(new ZeroArgHandler<>(handler));
        }

        /** Creates a factory for API requests with one argument. */
        static <S extends Sender, A1> Factory<S, A1, Void, Void, Void> oneArg(ApiHandler.OneArg<S, A1> handler) {
            return new FactoryImpl<>(new OneArgHandler<>(handler));
        }

        /** Creates a factory for API requests with two arguments. */
        static <S extends Sender, A1, A2> Factory<S, A1, A2, Void, Void> twoArg(ApiHandler.TwoArg<S, A1, A2> handler) {
            return new FactoryImpl<>(new TwoArgHandler<>(handler));
        }

        /** Creates a factory for API requests with three arguments. */
        static <S extends Sender, A1, A2, A3> Factory<S, A1, A2, A3, Void> threeArg(
                ApiHandler.ThreeArg<S, A1, A2, A3> handler) {
            return new FactoryImpl<>(new ThreeArgHandler<>(handler));
        }

        /** Creates a factory for API requests with four arguments. */
        static <S extends Sender, A1, A2, A3, A4> Factory<S, A1, A2, A3, A4> fourArg(
                ApiHandler.FourArg<S, A1, A2, A3, A4> handler) {
            return new FactoryImpl<>(new FourArgHandler<>(handler));
        }

        /** Creates an API request with none of the arguments set. */
        ApiRequest<S, A1, A2, A3, A4> createWithUnsetArgs(S sender, Dispatcher dispatcher);
    }

    /** Internal {@code Factory} implementation. */
    private record FactoryImpl<S extends Sender, A1, A2, A3, A4>(Handler<S, A1, A2, A3, A4> handler)
            implements Factory<S, A1, A2, A3, A4> {

        @Override
        public ApiRequest<S, A1, A2, A3, A4> createWithUnsetArgs(S sender, Dispatcher dispatcher) {
            return new ApiRequest<>(handler, sender, dispatcher);
        }
    }

    /** Internal handler for the API request. */
    @FunctionalInterface
    private interface Handler<S extends Sender, A1, A2, A3, A4> {

        void handleRequest(ApiRequest<S, A1, A2, A3, A4> request) throws Exception;
    }

    /** Internal {@code Handler} implementation for an API request with zero arguments. */
    private record ZeroArgHandler<S extends Sender>(ApiHandler.ZeroArg<S> delegate)
            implements Handler<S, Void, Void, Void, Void> {

        @Override
        public void handleRequest(ApiRequest<S, Void, Void, Void, Void> request) throws Exception {
            delegate.handleRequest(request.sender, request.dispatcher);
        }
    }

    /** Internal {@code Handler} implementation for an API request with one argument. */
    private record OneArgHandler<S extends Sender, A>(ApiHandler.OneArg<S, A> delegate)
            implements Handler<S, A, Void, Void, Void> {

        @Override
        public void handleRequest(ApiRequest<S, A, Void, Void, Void> request) throws Exception {
            delegate.handleRequest(request.sender, request.arg1, request.dispatcher);
        }
    }

    /** Internal {@code Handler} implementation for an API request with two arguments. */
    private record TwoArgHandler<S extends Sender, A1, A2>(ApiHandler.TwoArg<S, A1, A2> delegate)
            implements Handler<S, A1, A2, Void, Void> {

        @Override
        public void handleRequest(ApiRequest<S, A1, A2, Void, Void> request) throws Exception {
            delegate.handleRequest(request.sender, request.arg1, request.arg2, request.dispatcher);
        }
    }

    /** Internal {@code Handler} implementation for an API request with three arguments. */
    private record ThreeArgHandler<S extends Sender, A1, A2, A3>(ApiHandler.ThreeArg<S, A1, A2, A3> delegate)
            implements Handler<S, A1, A2, A3, Void> {

        @Override
        public void handleRequest(ApiRequest<S, A1, A2, A3, Void> request) throws Exception {
            delegate.handleRequest(request.sender, request.arg1, request.arg2, request.arg3, request.dispatcher);
        }
    }

    /** Internal {@code Handler} implementation for an API request with four arguments. */
    private record FourArgHandler<S extends Sender, A1, A2, A3, A4>(ApiHandler.FourArg<S, A1, A2, A3, A4> delegate)
            implements Handler<S, A1, A2, A3, A4> {

        @Override
        public void handleRequest(ApiRequest<S, A1, A2, A3, A4> request) throws Exception {
            delegate.handleRequest(
                    request.sender, request.arg1, request.arg2, request.arg3, request.arg4, request.dispatcher);
        }
    }
}
