package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.HttpOptional;
import io.github.mikewacker.drift.api.Sender;
import io.github.mikewacker.drift.json.JsonValues;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.nio.ByteBuffer;
import org.xnio.IoUtils;

/** {@code Sender} that is backed by an Undertow {@code HttpServerExchange}. */
interface UndertowSender {

    /** Checks that the response has not started, safely closing the connection if it has started. */
    private static boolean safeCheckResponseNotStarted(HttpServerExchange httpExchange) {
        if (httpExchange.isResponseStarted()) {
            IoUtils.safeClose(httpExchange.getConnection());
            return false;
        }

        return true;
    }

    /** {@code Sender.StatusCode} that is backed by an Undertow {@code HttpServerExchange}. */
    final class StatusCode implements Sender.StatusCode {

        private final HttpServerExchange httpExchange;

        /** Creates the response sender from the HTTP exchange. */
        public static Sender.StatusCode create(HttpServerExchange httpExchange) {
            return new UndertowSender.StatusCode(httpExchange);
        }

        @Override
        public void send(int statusCode) {
            if (!safeCheckResponseNotStarted(httpExchange)) {
                return;
            }

            httpExchange.setStatusCode(statusCode);
            httpExchange.endExchange();
        }

        private StatusCode(HttpServerExchange httpExchange) {
            this.httpExchange = httpExchange;
        }
    }

    /** {@code Sender.Value} that is backed by an Undertow {@code HttpServerExchange}, serializing values as JSON. */
    final class JsonValue<V> implements Sender.Value<V> {

        private final HttpServerExchange httpExchange;

        /** Creates the response sender from the HTTP exchange. */
        public static <V> Sender.Value<V> create(HttpServerExchange httpExchange) {
            return new JsonValue<>(httpExchange);
        }

        @Override
        public void send(HttpOptional<V> maybeValue) {
            if (!safeCheckResponseNotStarted(httpExchange)) {
                return;
            }

            if (maybeValue.isEmpty()) {
                httpExchange.setStatusCode(maybeValue.statusCode());
                httpExchange.endExchange();
                return;
            }
            V value = maybeValue.get();

            byte[] rawValue = JsonValues.serialize(value);
            httpExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            httpExchange.getResponseSender().send(ByteBuffer.wrap(rawValue));
        }

        private JsonValue(HttpServerExchange httpExchange) {
            this.httpExchange = httpExchange;
        }
    }
}
