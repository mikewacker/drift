# Drift

"I just want to prototype an HTTP server for a JSON API that can also make backend requests."

Drift makes this task easy. To use Drift, all you need to know&mdash;beyond general knowledge of Java and HTTP&mdash;is
the "Hello, world!" example for Undertow, an HTTP server. Or, you only need surface-level knowledge of Undertow;
you can Drift on the surface instead of diving into the Undertow.

## Example

Here is that "Hello, world!" example from [Undertow's website][undertow]:

```java
public class HelloWorldServer {

    public static void main(final String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Hello World");
                    }
                }).build();
        server.start();
    }
}
```

In this example, we'll build an `HttpHandler` for a more complex "Hello, world!" example, using a JSON API:

- The recipient (`world`) is a JSON string in the body of the HTTP request.
- The salutation (`Hello`) is retrieved from a backend server; the backend server also uses a JSON API.
- We'll also add a health check: a simple API that sends back a 200 status code as a health signal.

We can build this `HttpHandler` in three simple steps, and we'll also add tests.
The code for this example can also be found in the [example][example] folder.

### Step 1: Define the API

```java
/** Asynchronous API for greetings. */
public interface GreetingApi {

    /** Sends a greeting to the recipient. */
    void sendGreeting(Sender.Value<String> sender, String recipient, Dispatcher dispatcher);

    /** Sends a 200 status code as a health signal. */
    void healthCheck(Sender.StatusCode sender, Dispatcher dispatcher);
}
```

The only requirement is that the API methods
must match the signature of a `@FunctionalInterface` in [`ApiHandler`][api-handler].

### Step 2: Implement the API

```java
/** Service for {@code GreetingApi}. */
public final class GreetingService implements GreetingApi {

    private final Supplier<String> salutationUrlProvider;
    private final BackendDispatcher backendDispatcher = BackendDispatcher.create();

    /** Creates a greeting service. */
    public static GreetingApi create(Supplier<String> salutationUrlProvider) {
        return new GreetingService(salutationUrlProvider);
    }

    @Override
    public void sendGreeting(Sender.Value<String> sender, String recipient, Dispatcher dispatcher) {
        backendDispatcher
                .requestBuilder(new TypeReference<String>() {})
                .get(salutationUrlProvider.get())
                .build()
                .dispatch(sender, recipient, dispatcher, this::onSalutationReceived);
    }

    @Override
    public void healthCheck(Sender.StatusCode sender, Dispatcher dispatcher) {
        sender.sendOk();
    }

    /** Called when a salutation is received from the backend. */
    private void onSalutationReceived(
            Sender.Value<String> sender,
            String recipient,
            HttpOptional<String> maybeSalutation,
            Dispatcher dispatcher) {
        if (maybeSalutation.isEmpty()) {
            sender.sendErrorCode(500);
            return;
        }
        String salutation = maybeSalutation.get();

        String greeting = String.format("%s, %s!", salutation, recipient);
        sender.sendValue(greeting);
    }

    private GreetingService(Supplier<String> salutationUrlProvider) {
        this.salutationUrlProvider = salutationUrlProvider;
    }
}
```

Behind the scenes, `BackendDispatcher` deserializes the HTTP response body from JSON;
the `TypeReference<>` in `requestBuilder()` lets it know the type of the object that it is deserializing.

(For more information on JSON serialization and deserialization, see [`JsonValues`][json-values].)

### Step 3: Create an Endpoint

This is where we create the `HttpHandler`:

```java
/** HTTP handler for {@code GreetingApi}. */
public final class GreetingEndpoint {

    /** Creates an endpoint. */
    public static HttpHandler create(Supplier<String> salutationUrlProvider) {
        GreetingApi greetingApi = GreetingService.create(salutationUrlProvider);

        HttpHandler greetingHandler = UndertowJsonApiHandler.builder(new TypeReference<String>() {})
                .addArg(UndertowArgs.body(new TypeReference<String>() {}))
                .build(greetingApi::sendGreeting);
        HttpHandler healthHandler = UndertowJsonApiHandler.builder().build(greetingApi::healthCheck);

        return UndertowRouter.builder()
                .addRoute(HttpMethod.POST, "/greeting", greetingHandler)
                .addRoute(HttpMethod.GET, "/health", healthHandler)
                .build();
    }

    // static class
    private GreetingEndpoint() {}
}
```

`UndertowJsonApiHandler` is type-safe; a compiler error will occur if the types for the `addArg()` calls
do not align with the signature of the `ApiHandler` used in `build()`.

### What About Tests?

```java
public final class GreetingTest {

    @RegisterExtension
    private static final TestServer<?> greetingServer =
            TestUndertowServer.register("greeting", GreetingTest::createGreetingHandler);

    @RegisterExtension
    private static final MockServer salutationServer = MockServer.register("salutation");

    @Test
    public void helloWorld() throws IOException {
        salutationServer.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json").setBody("\"Hello\""));
        HttpOptional<String> maybeGreeting = JsonApiClient.requestBuilder(new TypeReference<String>() {})
                .post(greetingServer.url("/greeting"))
                .body("world")
                .build()
                .execute();
        assertThat(maybeGreeting).hasValue("Hello, world!");
    }

    @Test
    public void healthCheck() throws IOException {
        int statusCode = JsonApiClient.requestBuilder()
                .get(greetingServer.url("/health"))
                .build()
                .execute();
        assertThat(statusCode).isEqualTo(200);
    }

    private static HttpHandler createGreetingHandler() {
        // Could also call "salutationServer.rootUrl()", but this works outside this class as well.
        Supplier<String> salutationUrlProvider =
                () -> TestServer.get("salutation").rootUrl();
        return GreetingEndpoint.create(salutationUrlProvider);
    }
}
```

(`MockServer` is backed by [OkHttp's `MockWebServer`][mock-server].)

Similar to `BackendDispatcher`, `JsonApiClient` serializes the argument for `body(Object)` to JSON,
and it also deserializes the HTTP response body from JSON.

---

While not shown here, `drift-testlib` also provides some fakes and stubs for unit-testing.
For more complex APIs, I recommend extracting a synchronous helper class for an `ApiHandler` and unit-testing that.

### What About a Real-World Example?

https://github.com/mikewacker/age-verification

## Release Notes

- The API is incubating in 0.x versions of Drift.
- Drift is based on a real-world project; the other side of that coin is that said project heavily dictates
  which features are (and are not) included in the 0.1 release.

[undertow]: https://undertow.io/
[example]: /example
[api-handler]: /drift-api/src/main/java/io/github/mikewacker/drift/api/ApiHandler.java
[json-values]: /drift-api/src/main/java/io/github/mikewacker/drift/json/JsonValues.java
[mock-server]: https://square.github.io/okhttp/#mockwebserver
