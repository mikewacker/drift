package io.github.mikewacker.drift.api;

/** Marker interface for an asynchronous API handler. */
public interface ApiHandler {

    /**
     * Asynchronous API handler for a request with zero arguments.
     *
     * @param <S> the interface for the response sender
     */
    @FunctionalInterface
    interface ZeroArg<S extends Sender> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(S sender, Dispatcher dispatcher) throws Exception;
    }

    /**
     * Asynchronous API handler for a request with one argument.
     *
     * @param <S> the interface for the response sender
     * @param <A> the type of the argument
     */
    @FunctionalInterface
    interface OneArg<S extends Sender, A> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg the argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(S sender, A arg, Dispatcher dispatcher) throws Exception;
    }

    /**
     * Asynchronous API handler for a request with two arguments.
     *
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     */
    @FunctionalInterface
    interface TwoArg<S extends Sender, A1, A2> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg1 the first argument for the API request
         * @param arg2 the second argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(S sender, A1 arg1, A2 arg2, Dispatcher dispatcher) throws Exception;
    }

    /**
     * Asynchronous API handler for a request with three arguments.
     *
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     */
    @FunctionalInterface
    interface ThreeArg<S extends Sender, A1, A2, A3> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg1 the first argument for the API request
         * @param arg2 the second argument for the API request
         * @param arg3 the third argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(S sender, A1 arg1, A2 arg2, A3 arg3, Dispatcher dispatcher) throws Exception;
    }

    /**
     * Asynchronous API handler for a request with four arguments.
     *
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     */
    @FunctionalInterface
    interface FourArg<S extends Sender, A1, A2, A3, A4> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg1 the first argument for the API request
         * @param arg2 the second argument for the API request
         * @param arg3 the third argument for the API request
         * @param arg4 the fourth argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, Dispatcher dispatcher) throws Exception;
    }

    /**
     * Asynchronous API handler for a request with five arguments.
     *
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     * @param <A5> the type of the fifth argument
     */
    @FunctionalInterface
    interface FiveArg<S extends Sender, A1, A2, A3, A4, A5> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg1 the first argument for the API request
         * @param arg2 the second argument for the API request
         * @param arg3 the third argument for the API request
         * @param arg4 the fourth argument for the API request
         * @param arg5 the fifth argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, Dispatcher dispatcher)
                throws Exception;
    }

    /**
     * Asynchronous API handler for a request with six arguments.
     *
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     * @param <A5> the type of the fifth argument
     * @param <A6> the type of the sixth argument
     */
    @FunctionalInterface
    interface SixArg<S extends Sender, A1, A2, A3, A4, A5, A6> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg1 the first argument for the API request
         * @param arg2 the second argument for the API request
         * @param arg3 the third argument for the API request
         * @param arg4 the fourth argument for the API request
         * @param arg5 the fifth argument for the API request
         * @param arg6 the sixth argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6, Dispatcher dispatcher)
                throws Exception;
    }

    /**
     * Asynchronous API handler for a request with seven arguments.
     *
     * @param <S> the interface for the response sender
     * @param <A1> the type of the first argument
     * @param <A2> the type of the second argument
     * @param <A3> the type of the third argument
     * @param <A4> the type of the fourth argument
     * @param <A5> the type of the fifth argument
     * @param <A6> the type of the sixth argument
     * @param <A7> the type of the seventh argument
     */
    @FunctionalInterface
    interface SevenArg<S extends Sender, A1, A2, A3, A4, A5, A6, A7> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg1 the first argument for the API request
         * @param arg2 the second argument for the API request
         * @param arg3 the third argument for the API request
         * @param arg4 the fourth argument for the API request
         * @param arg5 the fifth argument for the API request
         * @param arg6 the sixth argument for the API request
         * @param arg7 the seventh argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(
                S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6, A7 arg7, Dispatcher dispatcher)
                throws Exception;
    }

    /**
     * Asynchronous API handler for a request with eight arguments.
     *
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
    @FunctionalInterface
    interface EightArg<S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8> extends ApiHandler {

        /**
         * Handles an API request, sending the response or dispatching the request.
         *
         * @param sender the response sender, for when the response can be sent
         * @param arg1 the first argument for the API request
         * @param arg2 the second argument for the API request
         * @param arg3 the third argument for the API request
         * @param arg4 the fourth argument for the API request
         * @param arg5 the fifth argument for the API request
         * @param arg6 the sixth argument for the API request
         * @param arg7 the seventh argument for the API request
         * @param arg8 the eighth argument for the API request
         * @param dispatcher the request dispatcher, for when the response cannot be sent yet
         * @throws Exception for any exception that this handler may throw
         */
        void handleRequest(
                S sender, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6, A7 arg7, A8 arg8, Dispatcher dispatcher)
                throws Exception;
    }
}
