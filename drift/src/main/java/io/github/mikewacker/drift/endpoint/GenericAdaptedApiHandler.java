package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import java.util.function.Consumer;

/**
 * Internal {@code AdaptedApiHandler} that implements the generic logic.
 * The implementation for a specific server will use this implementation as a delegate.
 */
final class GenericAdaptedApiHandler<E, S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8>
        implements AdaptedApiHandler<E> {

    private final ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequestFactory;
    private final SenderFactory<E, S> senderFactory;
    private final DispatcherFactory<E> dispatcherFactory;
    private final ArgExtractor.Async<E, A1> arg1Extractor;
    private final ArgExtractor.Async<E, A2> arg2Extractor;
    private final ArgExtractor.Async<E, A3> arg3Extractor;
    private final ArgExtractor.Async<E, A4> arg4Extractor;
    private final ArgExtractor.Async<E, A5> arg5Extractor;
    private final ArgExtractor.Async<E, A6> arg6Extractor;
    private final ArgExtractor.Async<E, A7> arg7Extractor;
    private final ArgExtractor.Async<E, A8> arg8Extractor;

    /** Creates a builder for an HTTP handler for the underlying server that invokes an API handler. */
    public static <E, EH extends AdaptedApiHandler<E>, S extends Sender> ZeroArgBuilder<E, EH, S> builder(
            SenderFactory<E, S> senderFactory,
            DispatcherFactory<E> dispatcherFactory,
            HttpHandlerFactory<E, EH> httpHandlerFactory) {
        return new ZeroArgBuilderImpl<>(senderFactory, dispatcherFactory, httpHandlerFactory);
    }

    @Override
    public void handleRequest(E exchange) throws Exception {
        S sender = senderFactory.create(exchange);
        Dispatcher dispatcher = dispatcherFactory.create(exchange);
        ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest =
                apiRequestFactory.createWithUnsetArgs(sender, dispatcher);
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg1Extractor, apiRequest::setArg1, this::onArg1Set);
    }

    /** Called when the first argument has been set for the API request. */
    private void onArg1Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg2Extractor, apiRequest::setArg2, this::onArg2Set);
    }

    /** Called when the second argument has been set for the API request. */
    private void onArg2Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg3Extractor, apiRequest::setArg3, this::onArg3Set);
    }

    /** Called when the third argument has been set for the API request. */
    private void onArg3Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg4Extractor, apiRequest::setArg4, this::onArg4Set);
    }

    /** Called when the fourth argument has been set for the API request. */
    private void onArg4Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg5Extractor, apiRequest::setArg5, this::onArg5Set);
    }

    /** Called when the fifth argument has been set for the API request. */
    private void onArg5Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg6Extractor, apiRequest::setArg6, this::onArg6Set);
    }

    /** Called when the sixth argument has been set for the API request. */
    private void onArg6Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg7Extractor, apiRequest::setArg7, this::onArg7Set);
    }

    /** Called when the seventh argument has been set for the API request. */
    private void onArg7Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        extractNextArgOrHandleApiRequest(exchange, apiRequest, arg8Extractor, apiRequest::setArg8, this::onArg8Set);
    }

    /** Called when the eight argument has been set for the API request. */
    private void onArg8Set(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception {
        apiRequest.handle();
    }

    /** Extracts the next argument, or handles the API request if all arguments have been extracted. */
    private <A> void extractNextArgOrHandleApiRequest(
            E exchange,
            ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest,
            ArgExtractor.Async<E, A> argExtractor,
            Consumer<A> argSetter,
            AdapterHandler<E, S, A1, A2, A3, A4, A5, A6, A7, A8> next)
            throws Exception {
        if (argExtractor == null) {
            apiRequest.handle();
            return;
        }

        ArgExtractor.Callback<A> callback = new AdaptedArgExtractorCallback<>(exchange, apiRequest, argSetter, next);
        argExtractor.tryExtract(exchange, callback);
    }

    private GenericAdaptedApiHandler(
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequestFactory,
            SenderFactory<E, S> senderFactory,
            DispatcherFactory<E> dispatcherFactory,
            ArgExtractor.Async<E, A1> arg1Extractor,
            ArgExtractor.Async<E, A2> arg2Extractor,
            ArgExtractor.Async<E, A3> arg3Extractor,
            ArgExtractor.Async<E, A4> arg4Extractor,
            ArgExtractor.Async<E, A5> arg5Extractor,
            ArgExtractor.Async<E, A6> arg6Extractor,
            ArgExtractor.Async<E, A7> arg7Extractor,
            ArgExtractor.Async<E, A8> arg8Extractor) {
        this.apiRequestFactory = apiRequestFactory;
        this.senderFactory = senderFactory;
        this.dispatcherFactory = dispatcherFactory;
        this.arg1Extractor = arg1Extractor;
        this.arg2Extractor = arg2Extractor;
        this.arg3Extractor = arg3Extractor;
        this.arg4Extractor = arg4Extractor;
        this.arg5Extractor = arg5Extractor;
        this.arg6Extractor = arg6Extractor;
        this.arg7Extractor = arg7Extractor;
        this.arg8Extractor = arg8Extractor;
    }

    /** Creates a {@code Sender} from the underlying HTTP exchange. */
    @FunctionalInterface
    public interface SenderFactory<E, S extends Sender> {

        S create(E exchange);
    }

    /** Creates a {@code Dispatcher} from the underlying HTTP exchange. */
    @FunctionalInterface
    public interface DispatcherFactory<E> {

        Dispatcher create(E exchange);
    }

    /** Creates an HTTP handler from an {@code AdaptedApiHandler} delegate. */
    public interface HttpHandlerFactory<E, EH extends AdaptedApiHandler<E>> {

        EH create(AdaptedApiHandler<E> delegate);
    }

    /** Internal handler that handles the underlying HTTP request by converting it to an {@code ApiRequest}. */
    @FunctionalInterface
    private interface AdapterHandler<E, S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8> {

        void handleRequest(E exchange, ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest) throws Exception;
    }

    /** Sets the argument that has been extracted, or sends an error status code if extraction fails. */
    private record AdaptedArgExtractorCallback<E, S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8, A>(
            E exchange,
            ApiRequest<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequest,
            Consumer<A> argSetter,
            AdapterHandler<E, S, A1, A2, A3, A4, A5, A6, A7, A8> next)
            implements ArgExtractor.Callback<A> {

        @Override
        public void onArgExtracted(HttpOptional<A> maybeArg) throws Exception {
            if (maybeArg.isEmpty()) {
                apiRequest.sendErrorCode(maybeArg);
                return;
            }
            A arg = maybeArg.get();

            argSetter.accept(arg);
            next.handleRequest(exchange, apiRequest);
        }
    }

    /** Internal {@code ZeroArgBuilder} implementation. */
    private record ZeroArgBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender>(
            SenderFactory<E, S> senderFactory,
            DispatcherFactory<E> dispatcherFactory,
            HttpHandlerFactory<E, EH> httpHandlerFactory)
            implements ZeroArgBuilder<E, EH, S> {

        @Override
        public EH build(ApiHandler.ZeroArg<S> apiHandler) {
            ApiRequest.Factory<S, Void, Void, Void, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.zeroArg(apiHandler);
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    senderFactory,
                    dispatcherFactory,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            return httpHandlerFactory.create(delegate);
        }

        @Override
        public <A1> OneArgBuilder<E, EH, S, A1> addArg(ArgExtractor.Async<E, A1> arg1Extractor) {
            return new OneArgBuilderImpl<>(this, arg1Extractor);
        }
    }

    /** Internal {@code OneArgBuilder} implementation. */
    private record OneArgBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1>(
            ZeroArgBuilderImpl<E, EH, S> builder0, ArgExtractor.Async<E, A1> arg1Extractor)
            implements OneArgBuilder<E, EH, S, A1> {

        @Override
        public EH build(ApiHandler.OneArg<S, A1> apiHandler) {
            ApiRequest.Factory<S, A1, Void, Void, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.oneArg(apiHandler);
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    arg1Extractor,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            return builder0.httpHandlerFactory.create(delegate);
        }

        @Override
        public <A2> TwoArgBuilder<E, EH, S, A1, A2> addArg(ArgExtractor.Async<E, A2> arg2Extractor) {
            return new TwoArgBuilderImpl<>(this, arg2Extractor);
        }
    }

    /** Internal {@code TwoArgBuilder} implementation. */
    private record TwoArgBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2>(
            OneArgBuilderImpl<E, EH, S, A1> builder1, ArgExtractor.Async<E, A2> arg2Extractor)
            implements TwoArgBuilder<E, EH, S, A1, A2> {

        @Override
        public EH build(ApiHandler.TwoArg<S, A1, A2> apiHandler) {
            ApiRequest.Factory<S, A1, A2, Void, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.twoArg(apiHandler);
            ZeroArgBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    builder1.arg1Extractor,
                    arg2Extractor,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            return builder0.httpHandlerFactory.create(delegate);
        }

        @Override
        public <A3> ThreeArgBuilder<E, EH, S, A1, A2, A3> addArg(ArgExtractor.Async<E, A3> arg3Extractor) {
            return new ThreeArgBuilderImpl<>(this, arg3Extractor);
        }
    }

    /** Internal {@code ThreeArgBuilder} implementation. */
    private record ThreeArgBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3>(
            TwoArgBuilderImpl<E, EH, S, A1, A2> builder2, ArgExtractor.Async<E, A3> arg3Extractor)
            implements ThreeArgBuilder<E, EH, S, A1, A2, A3> {

        @Override
        public EH build(ApiHandler.ThreeArg<S, A1, A2, A3> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.threeArg(apiHandler);
            OneArgBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    builder1.arg1Extractor,
                    builder2.arg2Extractor,
                    arg3Extractor,
                    null,
                    null,
                    null,
                    null,
                    null);
            return builder0.httpHandlerFactory.create(delegate);
        }

        @Override
        public <A4> FourArgBuilder<E, EH, S, A1, A2, A3, A4> addArg(ArgExtractor.Async<E, A4> arg4Extractor) {
            return new FourArgBuilderImpl<>(this, arg4Extractor);
        }
    }

    /** Internal {@code FourArgBuilder} implementation. */
    private record FourArgBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4>(
            ThreeArgBuilderImpl<E, EH, S, A1, A2, A3> builder3, ArgExtractor.Async<E, A4> arg4Extractor)
            implements FourArgBuilder<E, EH, S, A1, A2, A3, A4> {

        @Override
        public EH build(ApiHandler.FourArg<S, A1, A2, A3, A4> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.fourArg(apiHandler);
            TwoArgBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    builder1.arg1Extractor,
                    builder2.arg2Extractor,
                    builder3.arg3Extractor,
                    arg4Extractor,
                    null,
                    null,
                    null,
                    null);
            return builder0.httpHandlerFactory.create(delegate);
        }

        @Override
        public <A5> FiveArgBuilder<E, EH, S, A1, A2, A3, A4, A5> addArg(ArgExtractor.Async<E, A5> arg5Extractor) {
            return new FiveArgBuilderImpl<>(this, arg5Extractor);
        }
    }

    /** Internal {@code FiveArgBuilder} implementation. */
    private record FiveArgBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5>(
            FourArgBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4, ArgExtractor.Async<E, A5> arg5Extractor)
            implements FiveArgBuilder<E, EH, S, A1, A2, A3, A4, A5> {

        @Override
        public EH build(ApiHandler.FiveArg<S, A1, A2, A3, A4, A5> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.fiveArg(apiHandler);
            ThreeArgBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    builder1.arg1Extractor,
                    builder2.arg2Extractor,
                    builder3.arg3Extractor,
                    builder4.arg4Extractor,
                    arg5Extractor,
                    null,
                    null,
                    null);
            return builder0.httpHandlerFactory.create(delegate);
        }

        @Override
        public <A6> SixArgBuilder<E, EH, S, A1, A2, A3, A4, A5, A6> addArg(ArgExtractor.Async<E, A6> arg6Extractor) {
            return new SixArgBuilderImpl<>(this, arg6Extractor);
        }
    }

    /** Internal {@code SixArgBuilder} implementation. */
    private record SixArgBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6>(
            FiveArgBuilderImpl<E, EH, S, A1, A2, A3, A4, A5> builder5, ArgExtractor.Async<E, A6> arg6Extractor)
            implements SixArgBuilder<E, EH, S, A1, A2, A3, A4, A5, A6> {

        @Override
        public EH build(ApiHandler.SixArg<S, A1, A2, A3, A4, A5, A6> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.sixArg(apiHandler);
            FourArgBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4 = builder5.builder4;
            ThreeArgBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    builder1.arg1Extractor,
                    builder2.arg2Extractor,
                    builder3.arg3Extractor,
                    builder4.arg4Extractor,
                    builder5.arg5Extractor,
                    arg6Extractor,
                    null,
                    null);
            return builder0.httpHandlerFactory.create(delegate);
        }

        @Override
        public <A7> SevenArgBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7> addArg(
                ArgExtractor.Async<E, A7> arg7Extractor) {
            return new SevenArgBuilderImpl<>(this, arg7Extractor);
        }
    }

    /** Internal {@code SevenArgBuilder} implementation. */
    private record SevenArgBuilderImpl<
                    E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6, A7>(
            SixArgBuilderImpl<E, EH, S, A1, A2, A3, A4, A5, A6> builder6, ArgExtractor.Async<E, A7> arg7Extractor)
            implements SevenArgBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7> {

        @Override
        public EH build(ApiHandler.SevenArg<S, A1, A2, A3, A4, A5, A6, A7> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, A7, Void> apiRequestFactory =
                    ApiRequest.Factory.sevenArg(apiHandler);
            FiveArgBuilderImpl<E, EH, S, A1, A2, A3, A4, A5> builder5 = builder6.builder5;
            FourArgBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4 = builder5.builder4;
            ThreeArgBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    builder1.arg1Extractor,
                    builder2.arg2Extractor,
                    builder3.arg3Extractor,
                    builder4.arg4Extractor,
                    builder5.arg5Extractor,
                    builder6.arg6Extractor,
                    arg7Extractor,
                    null);
            return builder0.httpHandlerFactory.create(delegate);
        }

        @Override
        public <A8> EightArgBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7, A8> addArg(
                ArgExtractor.Async<E, A8> arg8Extractor) {
            return new EightArgBuilderImpl<>(this, arg8Extractor);
        }
    }

    /** Internal {@code EightArgBuilder} implementation. */
    private record EightArgBuilderImpl<
                    E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8>(
            SevenArgBuilderImpl<E, EH, S, A1, A2, A3, A4, A5, A6, A7> builder7, ArgExtractor.Async<E, A8> arg8Extractor)
            implements EightArgBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7, A8> {

        @Override
        public EH build(ApiHandler.EightArg<S, A1, A2, A3, A4, A5, A6, A7, A8> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequestFactory =
                    ApiRequest.Factory.eightArg(apiHandler);
            SixArgBuilderImpl<E, EH, S, A1, A2, A3, A4, A5, A6> builder6 = builder7.builder6;
            FiveArgBuilderImpl<E, EH, S, A1, A2, A3, A4, A5> builder5 = builder6.builder5;
            FourArgBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4 = builder5.builder4;
            ThreeArgBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    apiRequestFactory,
                    builder0.senderFactory,
                    builder0.dispatcherFactory,
                    builder1.arg1Extractor,
                    builder2.arg2Extractor,
                    builder3.arg3Extractor,
                    builder4.arg4Extractor,
                    builder5.arg5Extractor,
                    builder6.arg6Extractor,
                    builder7.arg7Extractor,
                    arg8Extractor);
            return builder0.httpHandlerFactory.create(delegate);
        }
    }
}
