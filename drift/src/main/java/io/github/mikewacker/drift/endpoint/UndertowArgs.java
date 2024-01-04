package io.github.mikewacker.drift.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.json.JsonValues;
import io.undertow.io.Receiver;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.util.ArrayDeque;
import java.util.Deque;

/** Repository of extractors for Undertow that get arguments for the API request from the underlying HTTP request. */
public final class UndertowArgs {

    /**
     * Returns an extractor that gets an argument for the API request from the HTTP request body.
     * A 400 error occurs if the argument cannot be deserialized from JSON.
     *
     * @param argTypeRef a {@link TypeReference} for the argument
     * @return an argument extractor for the HTTP request body
     * @param <A> the type of the argument
     */
    public static <A> ArgExtractor.Async<HttpServerExchange, A> body(TypeReference<A> argTypeRef) {
        return new BodyExtractor<>(argTypeRef);
    }

    /**
     * Returns an extractor that gets a text argument for the API request from an HTTP query parameter.
     * A 400 error occurs if the query parameter has zero values or multiple values.
     *
     * @param name the name of the HTTP query parameter
     * @return an argument extractor for the HTTP query parameter
     */
    public static ArgExtractor<HttpServerExchange, String> queryParam(String name) {
        return new QueryParamTextExtractor(name);
    }

    /**
     * Returns an extractor that gets an argument for the API request from an HTTP query parameter.
     * A 400 error occurs if the query parameter has zero values or multiple values,
     * or if the argument cannot be deserialized from JSON.
     *
     * @param name the name of the HTTP query parameter
     * @param argTypeRef a {@link TypeReference} for the argument
     * @return an argument extractor for the HTTP query parameter
     * @param <A> the type of the argument
     */
    public static <A> ArgExtractor<HttpServerExchange, A> queryParam(String name, TypeReference<A> argTypeRef) {
        return new QueryParamJsonExtractor<>(name, argTypeRef);
    }

    // static class
    private UndertowArgs() {}

    /** Extractor that reads and deserializes the HTTP request body. */
    private record BodyExtractor<A>(TypeReference<A> argTypeRef) implements ArgExtractor.Async<HttpServerExchange, A> {

        @Override
        public void tryExtract(HttpServerExchange httpExchange, ArgExtractor.Callback<A> callback) {
            Receiver.FullBytesCallback bodyCallback = new BodyCallback<>(argTypeRef, callback);
            httpExchange.getRequestReceiver().receiveFullBytes(bodyCallback);
        }
    }

    /** Callback for when the HTTP request body has been read. */
    private record BodyCallback<A>(TypeReference<A> argTypeRef, ArgExtractor.Callback<A> callback)
            implements Receiver.FullBytesCallback {

        @Override
        public void handle(HttpServerExchange httpExchange, byte[] rawArg) {
            HttpOptional<A> maybeArg = JsonValues.tryDeserialize(rawArg, argTypeRef, StatusCodes.BAD_REQUEST);

            // FullBytesCallback does not throw checked exceptions, so we must tunnel checked exceptions.
            try {
                callback.onArgExtracted(maybeArg);
            } catch (Exception e) {
                throw TunneledException.tunnel(e);
            }
        }
    }

    /** Extractor that gets the text value of an HTTP query parameter. */
    private record QueryParamTextExtractor(String name) implements ArgExtractor<HttpServerExchange, String> {

        private static final Deque<String> EMPTY_VALUES = new ArrayDeque<>();

        @Override
        public HttpOptional<String> tryExtract(HttpServerExchange httpExchange) {
            Deque<String> values = httpExchange.getQueryParameters().getOrDefault(name, EMPTY_VALUES);
            if (values.size() != 1) {
                return HttpOptional.empty(StatusCodes.BAD_REQUEST);
            }
            String value = values.getFirst();

            return HttpOptional.of(value);
        }
    }

    /** Extractor that gets and deserializes the value of an HTTP query parameter. */
    private record QueryParamJsonExtractor<A>(String name, TypeReference<A> argTypeRef)
            implements ArgExtractor<HttpServerExchange, A> {

        @Override
        public HttpOptional<A> tryExtract(HttpServerExchange httpExchange) {
            ArgExtractor<HttpServerExchange, String> textArgExtractor = new QueryParamTextExtractor(name);
            HttpOptional<String> maybeTextArg = textArgExtractor.tryExtract(httpExchange);
            if (maybeTextArg.isEmpty()) {
                return maybeTextArg.convertEmpty();
            }
            String textArg = maybeTextArg.get();

            byte[] rawArg = JsonValues.serialize(textArg);
            return JsonValues.tryDeserialize(rawArg, argTypeRef, StatusCodes.BAD_REQUEST);
        }
    }
}
