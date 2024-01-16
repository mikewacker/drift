package io.github.mikewacker.drift.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.ApiHandler;
import io.github.mikewacker.drift.api.Dispatcher;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import java.util.List;
import java.util.function.Consumer;

/**
 * Internal {@code AdaptedApiHandler} that implements the generic logic.
 * <p>
 * An implementation for a specific server will...
 * <ul>
 *     <li>provide a private constructor that accepts an {@code AdaptedApiHandler} delegate.
 *     <li>implement {@code PreArgStageBuilder}.
 *     <li>provide a static {@code builder()} method that returns a {@code RouteStageBuilder},
 *         using the {@code PreArgStageBuilder} implementation as the {@code RouteStageBuilder} implementation.
 * </ul>
 */
final class GenericAdaptedApiHandler<E, S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8>
        implements AdaptedApiHandler<E> {

    private final HttpMethod method;
    private List<String> relativePathSegments;

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

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public List<String> getRelativePathSegments() {
        return relativePathSegments;
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
            HttpMethod method,
            List<String> relativePathSegments,
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
        this.method = method;
        this.relativePathSegments = relativePathSegments;
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

    /** Abstract {@code RouteStageBuilder} and {@code ResponseStageBuilder} implementation. */
    public abstract static class PreArgStageBuilder<E, EH extends AdaptedApiHandler<E>>
            implements RouteStageBuilder<E, EH>, ResponseStageBuilder<E, EH> {

        private HttpMethod method = null;
        private List<String> relativePathSegments = null;

        @Override
        public final PreArgStageBuilder<E, EH> route(HttpMethod method, String relativePath) {
            this.method = method;
            relativePathSegments = splitRelativePath(relativePath);
            return this;
        }

        @Override
        public final ZeroArgStageBuilder<E, EH, Sender.StatusCode> statusCodeResponse() {
            SenderFactory<E, Sender.StatusCode> senderFactory = getStatusCodeSenderFactory();
            DispatcherFactory<E> dispatcherFactory = getDispatcherFactory();
            HttpHandlerFactory<E, EH> httpHandlerFactory = getHttpHandlerFactory();
            return new ZeroArgStageBuilderImpl<>(
                    method, relativePathSegments, senderFactory, dispatcherFactory, httpHandlerFactory);
        }

        @Override
        public final <V> ZeroArgStageBuilder<E, EH, Sender.Value<V>> jsonResponse(
                TypeReference<V> responseValueTypeRef) {
            SenderFactory<E, Sender.Value<V>> senderFactory = getJsonValueSenderFactory();
            DispatcherFactory<E> dispatcherFactory = getDispatcherFactory();
            HttpHandlerFactory<E, EH> httpHandlerFactory = getHttpHandlerFactory();
            return new ZeroArgStageBuilderImpl<>(
                    method, relativePathSegments, senderFactory, dispatcherFactory, httpHandlerFactory);
        }

        /** Gets the factory that creates a {@code Sender.StatusCode} from the underlying HTTP exchange. */
        protected abstract SenderFactory<E, Sender.StatusCode> getStatusCodeSenderFactory();

        /** Gets the factory that creates a {@code Sender.Value} from the underlying HTTP exchange. */
        protected abstract <V> SenderFactory<E, Sender.Value<V>> getJsonValueSenderFactory();

        /** Gets the factory that creates a {@code Dispatcher} from the underlying HTTP exchange. */
        protected abstract DispatcherFactory<E> getDispatcherFactory();

        /** Gets the factory that creates an HTTP handler from the {@code AdaptedApiHandler} delegate. */
        protected abstract HttpHandlerFactory<E, EH> getHttpHandlerFactory();

        protected PreArgStageBuilder() {}

        /** Splits the relative URL path into segments. */
        private static List<String> splitRelativePath(String relativePath) {
            relativePath = relativePath.replaceFirst("^/", "");
            return List.of(relativePath.split("/"));
        }
    }

    /** Internal {@code ZeroArgStageBuilder} implementation. */
    private record ZeroArgStageBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender>(
            HttpMethod method,
            List<String> relativePathSegments,
            SenderFactory<E, S> senderFactory,
            DispatcherFactory<E> dispatcherFactory,
            HttpHandlerFactory<E, EH> httpHandlerFactory)
            implements ZeroArgStageBuilder<E, EH, S> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.ZeroArg<S> apiHandler) {
            ApiRequest.Factory<S, Void, Void, Void, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.zeroArg(apiHandler);
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    method,
                    relativePathSegments,
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
            return new FinalStageBuilderImpl<>(httpHandlerFactory, delegate);
        }

        @Override
        public <A1> OneArgStageBuilder<E, EH, S, A1> arg(ArgExtractor.Async<E, A1> arg1Extractor) {
            return new OneArgStageBuilderImpl<>(this, arg1Extractor);
        }
    }

    /** Internal {@code OneArgStageBuilder} implementation. */
    private record OneArgStageBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1>(
            ZeroArgStageBuilderImpl<E, EH, S> builder0, ArgExtractor.Async<E, A1> arg1Extractor)
            implements OneArgStageBuilder<E, EH, S, A1> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.OneArg<S, A1> apiHandler) {
            ApiRequest.Factory<S, A1, Void, Void, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.oneArg(apiHandler);
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }

        @Override
        public <A2> TwoArgStageBuilder<E, EH, S, A1, A2> arg(ArgExtractor.Async<E, A2> arg2Extractor) {
            return new TwoArgStageBuilderImpl<>(this, arg2Extractor);
        }
    }

    /** Internal {@code TwoArgStageBuilder} implementation. */
    private record TwoArgStageBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2>(
            OneArgStageBuilderImpl<E, EH, S, A1> builder1, ArgExtractor.Async<E, A2> arg2Extractor)
            implements TwoArgStageBuilder<E, EH, S, A1, A2> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.TwoArg<S, A1, A2> apiHandler) {
            ApiRequest.Factory<S, A1, A2, Void, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.twoArg(apiHandler);
            ZeroArgStageBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }

        @Override
        public <A3> ThreeArgStageBuilder<E, EH, S, A1, A2, A3> arg(ArgExtractor.Async<E, A3> arg3Extractor) {
            return new ThreeArgStageBuilderImpl<>(this, arg3Extractor);
        }
    }

    /** Internal {@code ThreeArgStageBuilder} implementation. */
    private record ThreeArgStageBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3>(
            TwoArgStageBuilderImpl<E, EH, S, A1, A2> builder2, ArgExtractor.Async<E, A3> arg3Extractor)
            implements ThreeArgStageBuilder<E, EH, S, A1, A2, A3> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.ThreeArg<S, A1, A2, A3> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, Void, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.threeArg(apiHandler);
            OneArgStageBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgStageBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }

        @Override
        public <A4> FourArgStageBuilder<E, EH, S, A1, A2, A3, A4> arg(ArgExtractor.Async<E, A4> arg4Extractor) {
            return new FourArgStageBuilderImpl<>(this, arg4Extractor);
        }
    }

    /** Internal {@code FourArgStageBuilder} implementation. */
    private record FourArgStageBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4>(
            ThreeArgStageBuilderImpl<E, EH, S, A1, A2, A3> builder3, ArgExtractor.Async<E, A4> arg4Extractor)
            implements FourArgStageBuilder<E, EH, S, A1, A2, A3, A4> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.FourArg<S, A1, A2, A3, A4> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, Void, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.fourArg(apiHandler);
            TwoArgStageBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgStageBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgStageBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }

        @Override
        public <A5> FiveArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5> arg(ArgExtractor.Async<E, A5> arg5Extractor) {
            return new FiveArgStageBuilderImpl<>(this, arg5Extractor);
        }
    }

    /** Internal {@code FiveArgStageBuilder} implementation. */
    private record FiveArgStageBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5>(
            FourArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4, ArgExtractor.Async<E, A5> arg5Extractor)
            implements FiveArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.FiveArg<S, A1, A2, A3, A4, A5> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, Void, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.fiveArg(apiHandler);
            ThreeArgStageBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgStageBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgStageBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgStageBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }

        @Override
        public <A6> SixArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6> arg(ArgExtractor.Async<E, A6> arg6Extractor) {
            return new SixArgStageBuilderImpl<>(this, arg6Extractor);
        }
    }

    /** Internal {@code SixArgStageBuilder} implementation. */
    private record SixArgStageBuilderImpl<E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6>(
            FiveArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4, A5> builder5, ArgExtractor.Async<E, A6> arg6Extractor)
            implements SixArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.SixArg<S, A1, A2, A3, A4, A5, A6> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, Void, Void> apiRequestFactory =
                    ApiRequest.Factory.sixArg(apiHandler);
            FourArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4 = builder5.builder4;
            ThreeArgStageBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgStageBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgStageBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgStageBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }

        @Override
        public <A7> SevenArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7> arg(
                ArgExtractor.Async<E, A7> arg7Extractor) {
            return new SevenArgStageBuilderImpl<>(this, arg7Extractor);
        }
    }

    /** Internal {@code SevenArgStageBuilder} implementation. */
    private record SevenArgStageBuilderImpl<
                    E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6, A7>(
            SixArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4, A5, A6> builder6, ArgExtractor.Async<E, A7> arg7Extractor)
            implements SevenArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.SevenArg<S, A1, A2, A3, A4, A5, A6, A7> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, A7, Void> apiRequestFactory =
                    ApiRequest.Factory.sevenArg(apiHandler);
            FiveArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4, A5> builder5 = builder6.builder5;
            FourArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4 = builder5.builder4;
            ThreeArgStageBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgStageBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgStageBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgStageBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }

        @Override
        public <A8> EightArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7, A8> arg(
                ArgExtractor.Async<E, A8> arg8Extractor) {
            return new EightArgStageBuilderImpl<>(this, arg8Extractor);
        }
    }

    /** Internal {@code EightArgStageBuilder} implementation. */
    private record EightArgStageBuilderImpl<
                    E, EH extends AdaptedApiHandler<E>, S extends Sender, A1, A2, A3, A4, A5, A6, A7, A8>(
            SevenArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4, A5, A6, A7> builder7,
            ArgExtractor.Async<E, A8> arg8Extractor)
            implements EightArgStageBuilder<E, EH, S, A1, A2, A3, A4, A5, A6, A7, A8> {

        @Override
        public FinalStageBuilder<E, EH> apiHandler(ApiHandler.EightArg<S, A1, A2, A3, A4, A5, A6, A7, A8> apiHandler) {
            ApiRequest.Factory<S, A1, A2, A3, A4, A5, A6, A7, A8> apiRequestFactory =
                    ApiRequest.Factory.eightArg(apiHandler);
            SixArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4, A5, A6> builder6 = builder7.builder6;
            FiveArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4, A5> builder5 = builder6.builder5;
            FourArgStageBuilderImpl<E, EH, S, A1, A2, A3, A4> builder4 = builder5.builder4;
            ThreeArgStageBuilderImpl<E, EH, S, A1, A2, A3> builder3 = builder4.builder3;
            TwoArgStageBuilderImpl<E, EH, S, A1, A2> builder2 = builder3.builder2;
            OneArgStageBuilderImpl<E, EH, S, A1> builder1 = builder2.builder1;
            ZeroArgStageBuilderImpl<E, EH, S> builder0 = builder1.builder0;
            AdaptedApiHandler<E> delegate = new GenericAdaptedApiHandler<>(
                    builder0.method,
                    builder0.relativePathSegments,
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
            return new FinalStageBuilderImpl<>(builder0.httpHandlerFactory, delegate);
        }
    }

    /** Internal {@code FinalStageBuilder} implementation. */
    private record FinalStageBuilderImpl<E, EH extends AdaptedApiHandler<E>>(
            HttpHandlerFactory<E, EH> httpHandlerFactory, AdaptedApiHandler<E> delegate)
            implements FinalStageBuilder<E, EH> {

        @Override
        public EH build() {
            return httpHandlerFactory.create(delegate);
        }
    }
}
