package io.github.mikewacker.drift.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import java.util.List;

/**
 * An HTTP handler for the underlying server that invokes an API handler, using JSON as the wire format.
 *
 * @param <E> the type of the underlying HTTP exchange
 */
public interface AdaptedApiHandler<E> {

    /**
     * Gets the HTTP method for this API.
     *
     * @return an HTTP method
     */
    HttpMethod getMethod();

    /**
     * Gets the relative URL path for this API, split into segments using {@code '/'} as the delimiter.
     *
     * @return a list of path segments
     */
    List<String> getRelativePathSegments();

    /**
     * Handles the underlying HTTP request by invoking an API handler.
     *
     * @param httpExchange the underlying HTTP exchange
     * @throws Exception for any exception that this handler may throw
     */
    void handleRequest(E httpExchange) throws Exception;

    /**
     * Builder for an HTTP handler that can set the route: the HTTP method and the relative path.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     */
    interface RouteStageBuilder<E, EH extends AdaptedApiHandler<E>> {

        /**
         * Sets the route.
         *
         * @param method the HTTP method of the route
         * @param relativePath the relative URL path of the route
         * @return a builder at the response stage
         */
        ResponseStageBuilder<E, EH> route(HttpMethod method, String relativePath);
    }

    /**
     * Builder for an HTTP handler that can set the type of the response.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     */
    interface ResponseStageBuilder<E, EH extends AdaptedApiHandler<E>> {

        /**
         * Sets the type of the response to only an HTTP status code.
         *
         * @return a builder at the zero arguments stage
         */
        ZeroArgStageBuilder<E, EH, Sender.StatusCode> statusCodeResponse();

        /**
         * Sets the type of the response to an {@link HttpOptional} value that is deserialized from JSON.
         *
         * @param responseValueTypeRef a {@link TypeReference} for the response value
         * @return a builder at the zero arguments stage
         * @param <V> the type of the response value
         */
        <V> ZeroArgStageBuilder<E, EH, Sender.Value<V>> jsonResponse(TypeReference<V> responseValueTypeRef);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with zero arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     */
    interface ZeroArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender>
            extends ArgStageBuilder<E, EH, ApiHandler.ZeroArg<S>> {

        @Override
        default <A1> OneArgStageBuilder<E, EH, S, A1> arg(ArgExtractor<E, A1> arg1Extractor) {
            return arg(arg1Extractor.async());
        }

        @Override
        <A1> OneArgStageBuilder<E, EH, S, A1> arg(ArgExtractor.Async<E, A1> arg1Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with one argument or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     */
    interface OneArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1>
            extends ArgStageBuilder<E, EH, ApiHandler.OneArg<S, A1>> {

        @Override
        default <A2> TwoArgStageBuilder<E, EH, S, A1, A2> arg(ArgExtractor<E, A2> arg2Extractor) {
            return arg(arg2Extractor.async());
        }

        @Override
        <A2> TwoArgStageBuilder<E, EH, S, A1, A2> arg(ArgExtractor.Async<E, A2> arg2Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with two arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     * @param <A2> the type of the second argument for the API handler
     */
    interface TwoArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2>
            extends ArgStageBuilder<E, EH, ApiHandler.TwoArg<S, A1, A2>> {

        @Override
        default <A3> ThreeArgStageBuilder<E, EH, S, A1, A2, A3> arg(ArgExtractor<E, A3> arg3Extractor) {
            return arg(arg3Extractor.async());
        }

        @Override
        <A3> ThreeArgStageBuilder<E, EH, S, A1, A2, A3> arg(ArgExtractor.Async<E, A3> arg3Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with three arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     * @param <A2> the type of the second argument for the API handler
     * @param <A3> the type of the third argument for the API handler
     */
    interface ThreeArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3>
            extends ArgStageBuilder<E, EH, ApiHandler.ThreeArg<S, A1, A2, A3>> {

        @Override
        default <A4> FourArgStageBuilder<E, EH, S, A1, A2, A3, A4> arg(ArgExtractor<E, A4> arg4Extractor) {
            return arg(arg4Extractor.async());
        }

        @Override
        <A4> FourArgStageBuilder<E, EH, S, A1, A2, A3, A4> arg(ArgExtractor.Async<E, A4> arg4Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with four arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     * @param <A2> the type of the second argument for the API handler
     * @param <A3> the type of the third argument for the API handler
     * @param <A4> the type of the fourth argument for the API handler
     */
    interface FourArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4>
            extends ArgStageBuilder<E, EH, ApiHandler.FourArg<S, A1, A2, A3, A4>> {

        @Override
        default <A5> FiveArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5> arg(ArgExtractor<E, A5> arg5Extractor) {
            return arg(arg5Extractor.async());
        }

        @Override
        <A5> FiveArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5> arg(ArgExtractor.Async<E, A5> arg5Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with five arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     * @param <A2> the type of the second argument for the API handler
     * @param <A3> the type of the third argument for the API handler
     * @param <A4> the type of the fourth argument for the API handler
     * @param <A5> the type of the fifth argument for the API handler
     */
    interface FiveArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5>
            extends ArgStageBuilder<E, EH, ApiHandler.FiveArg<S, A1, A2, A3, A4, A5>> {

        @Override
        default <A6> SixArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6> arg(ArgExtractor<E, A6> arg6Extractor) {
            return arg(arg6Extractor.async());
        }

        @Override
        <A6> SixArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6> arg(ArgExtractor.Async<E, A6> arg6Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with six arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     * @param <A2> the type of the second argument for the API handler
     * @param <A3> the type of the third argument for the API handler
     * @param <A4> the type of the fourth argument for the API handler
     * @param <A5> the type of the fifth argument for the API handler
     * @param <A6> the type of the sixth argument for the API handler
     */
    interface SixArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6>
            extends ArgStageBuilder<E, EH, ApiHandler.SixArg<S, A1, A2, A3, A4, A5, A6>> {

        @Override
        default <A7> SevenArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7> arg(ArgExtractor<E, A7> arg7Extractor) {
            return arg(arg7Extractor.async());
        }

        @Override
        <A7> SevenArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7> arg(ArgExtractor.Async<E, A7> arg7Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with seven arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     * @param <A2> the type of the second argument for the API handler
     * @param <A3> the type of the third argument for the API handler
     * @param <A4> the type of the fourth argument for the API handler
     * @param <A5> the type of the fifth argument for the API handler
     * @param <A6> the type of the sixth argument for the API handler
     * @param <A7> the type of the seventh argument for the API handler
     */
    interface SevenArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6, A7>
            extends ArgStageBuilder<E, EH, ApiHandler.SevenArg<S, A1, A2, A3, A4, A5, A6, A7>> {

        @Override
        default <A8> EightArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7, A8> arg(
                ArgExtractor<E, A8> arg8Extractor) {
            return arg(arg8Extractor.async());
        }

        @Override
        <A8> EightArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7, A8> arg(
                ArgExtractor.Async<E, A8> arg8Extractor);
    }

    /**
     * Builder for an HTTP handler that can set the API handler with eight arguments or add an argument.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <S> the type of the response sender for the API handler
     * @param <A1> the type of the first argument for the API handler
     * @param <A2> the type of the second argument for the API handler
     * @param <A3> the type of the third argument for the API handler
     * @param <A4> the type of the fourth argument for the API handler
     * @param <A5> the type of the fifth argument for the API handler
     * @param <A6> the type of the sixth argument for the API handler
     * @param <A7> the type of the seventh argument for the API handler
     * @param <A8> the type of the eighth argument for the API handler
     */
    interface EightArgStageBuilder<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8>
            extends ApiHandlerStageBuilder<E, EH, ApiHandler.EightArg<S, A1, A2, A3, A4, A5, A6, A7, A8>> {}

    /**
     * Builder for an HTTP handler that can set the API handler.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <AH> the type of the API handler
     */
    interface ApiHandlerStageBuilder<E, EH extends AdaptedApiHandler<E>, AH extends ApiHandler> {

        /**
         * Sets the API handler.
         *
         * @param apiHandler the API handler
         * @return a builder at the final stage
         */
        FinalStageBuilder<E, EH> apiHandler(AH apiHandler);
    }

    /**
     * Builder for an HTTP handler that can set the API handler or add an argument.
     * <p>
     * Subtypes will override the return type, which is the next stage of the builder.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     * @param <AH> the type of the API handler
     */
    interface ArgStageBuilder<E, EH extends AdaptedApiHandler<E>, AH extends ApiHandler>
            extends ApiHandlerStageBuilder<E, EH, AH> {

        /**
         * Adds an argument to the API handler. The argument will be extracted from the underlying HTTP request.
         *
         * @param argExtractor the extractor that gets the argument from the underlying HTTP request
         * @return a builder at the stage with an additional argument
         * @param <A> the type of the argument for the API handler
         */
        <A> Object arg(ArgExtractor<E, A> argExtractor);

        /**
         * Adds an argument to the API handler. The argument will be extracted from the underlying HTTP request.
         *
         * @param argExtractor the extractor that gets the argument from the underlying HTTP request
         * @return a builder at the stage with an additional argument
         * @param <A> the type of the argument for the API handler
         */
        <A> Object arg(ArgExtractor.Async<E, A> argExtractor);
    }

    /**
     * Builder for an HTTP handler that can build the HTTP handler.
     *
     * @param <E> the type of the underlying HTTP exchange
     * @param <EH> the type of the HTTP handler for the underlying server
     */
    interface FinalStageBuilder<E, EH extends AdaptedApiHandler<E>> {

        /**
         * Builds the HTTP handler.
         *
         * @return the HTTP handler
         */
        EH build();
    }
}
