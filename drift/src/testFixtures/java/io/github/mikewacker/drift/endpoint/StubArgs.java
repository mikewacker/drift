package io.github.mikewacker.drift.endpoint;

import io.github.mikewacker.drift.api.HttpOptional;

/** Repository of extractors that get arguments for the API request from the stub HTTP exchange. */
final class StubArgs {

    /** Returns an extractor that gets an integer value from an argument, or extracts a 400 error. */
    public static ArgExtractor<StubHttpExchange, Integer> intValue(int index) {
        return new IntArgExtractor(index);
    }

    // static class
    private StubArgs() {}

    /** Extractor that gets an integer value from an argument. */
    private record IntArgExtractor(int index) implements ArgExtractor<StubHttpExchange, Integer> {

        @Override
        public HttpOptional<Integer> tryExtract(StubHttpExchange httpExchange) {
            String arg = httpExchange.args()[index];
            try {
                int i = Integer.parseInt(arg);
                return HttpOptional.of(i);
            } catch (NumberFormatException e) {
                return HttpOptional.empty(400);
            }
        }
    }
}
