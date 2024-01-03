package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Sender;

/**
 * An HTTP handler for the underlying server that invokes an API handler.
 *
 * @param <E> the type of the underlying HTTP exchange
 */
public interface AdaptedApiHandler<E> {

    /**
     * Handles the underlying HTTP request by invoking an API handler.
     *
     * @param exchange the underlying HTTP exchange
     * @throws Exception for any exception that this handler may throw
     */
    void handleRequest(E exchange) throws Exception;

    /**
     * Builder for an HTTP handler for the underlying server that invokes an API handler.
     * The API request contains zero or more arguments.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API request
     */
    interface ZeroArgBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender>
            extends ArgBuilder<E, EH, ApiHandler.ZeroArg<S>> {

        @Override
        default <A1> OneArgBuilder<E, EH, S, A1> addArg(ArgExtractor<E, A1> argExtractor) {
            return addArg(argExtractor.async());
        }

        @Override
        <A1> OneArgBuilder<E, EH, S, A1> addArg(ArgExtractor.Async<E, A1> argExtractor);
    }

    /**
     * Builder for an HTTP handler for the underlying server that invokes an API handler.
     * The API request contains one or more arguments.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API request
     * @param <A1> the type of the first argument for the API request
     */
    interface OneArgBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1>
            extends ArgBuilder<E, EH, ApiHandler.OneArg<S, A1>> {

        @Override
        default <A2> TwoArgBuilder<E, EH, S, A1, A2> addArg(ArgExtractor<E, A2> argExtractor) {
            return addArg(argExtractor.async());
        }

        @Override
        <A2> TwoArgBuilder<E, EH, S, A1, A2> addArg(ArgExtractor.Async<E, A2> argExtractor);
    }

    /**
     * Builder for an HTTP handler for the underlying server that invokes an API handler.
     * The API request contains two or more arguments.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API request
     * @param <A1> the type of the first argument for the API request
     * @param <A2> the type of the second argument for the API request
     */
    interface TwoArgBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2>
            extends ArgBuilder<E, EH, ApiHandler.TwoArg<S, A1, A2>> {

        @Override
        default <A3> ThreeArgBuilder<E, EH, S, A1, A2, A3> addArg(ArgExtractor<E, A3> argExtractor) {
            return addArg(argExtractor.async());
        }

        @Override
        <A3> ThreeArgBuilder<E, EH, S, A1, A2, A3> addArg(ArgExtractor.Async<E, A3> argExtractor);
    }

    /**
     * Builder for an HTTP handler for the underlying server that invokes an API handler.
     * The API request contains three or more arguments.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API request
     * @param <A1> the type of the first argument for the API request
     * @param <A2> the type of the second argument for the API request
     * @param <A3> the type of the third argument for the API request
     */
    interface ThreeArgBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3>
            extends ArgBuilder<E, EH, ApiHandler.ThreeArg<S, A1, A2, A3>> {

        @Override
        default <A4> FourArgBuilder<E, EH, S, A1, A2, A3, A4> addArg(ArgExtractor<E, A4> argExtractor) {
            return addArg(argExtractor.async());
        }

        @Override
        <A4> FourArgBuilder<E, EH, S, A1, A2, A3, A4> addArg(ArgExtractor.Async<E, A4> argExtractor);
    }

    /**
     * Builder for an HTTP handler for the underlying server that invokes an API handler.
     * The API request contains four arguments.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API request
     * @param <A1> the type of the first argument for the API request
     * @param <A2> the type of the second argument for the API request
     * @param <A3> the type of the third argument for the API request
     * @param <A4> the type of the fourth argument for the API request
     */
    interface FourArgBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4>
            extends Builder<E, EH, ApiHandler.FourArg<S, A1, A2, A3, A4>> {}

    /**
     * Builder for an HTTP handler for the underlying server that invokes an API handler.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <AH> the type of the API handler
     */
    interface Builder<E, EH extends AdaptedApiHandler<E>, AH extends ApiHandler> {

        /**
         * Builds an HTTP handler for the underlying server that invokes an API handler.
         *
         * @param apiHandler the API handler
         * @return an HTTP handler for the underlying server that invokes the API handler
         */
        EH build(AH apiHandler);
    }

    /**
     * Builder for an HTTP handler for the underlying server that invokes an API handler.
     * It can add an argument to the API request.
     * <p>
     * Subtypes will override the return type.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <AH> the type of the API handler
     */
    interface ArgBuilder<E, EH extends AdaptedApiHandler<E>, AH extends ApiHandler> extends Builder<E, EH, AH> {

        /**
         * Adds an argument to the API request, extracting that argument from the underlying HTTP request.
         *
         * @param argExtractor the extractor that gets the argument from the underlying HTTP request
         * @return a builder with an additional argument for the API request
         * @param <A> the type of the argument for the API request
         */
        <A> Object addArg(ArgExtractor<E, A> argExtractor);

        /**
         * Adds an argument to the API request, extracting that argument from the underlying HTTP request.
         *
         * @param argExtractor the extractor that gets the argument from the underlying HTTP request
         * @return a builder with an additional argument for the API request
         * @param <A> the type of the argument for the API request
         */
        <A> Object addArg(ArgExtractor.Async<E, A> argExtractor);
    }
}
