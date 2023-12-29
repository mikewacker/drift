package io.github.mikewacker.drift.api;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * An optional value with an HTTP status code.
 * A present value has a 200 status code, while an empty value has an error status code.
 *
 * @param <V> the type of the value
 */
public final class HttpOptional<V> {

    private final V value; // nullable
    private final int statusCode;

    /**
     * Creates an optional value with a value and a 200 status code.
     *
     * @param value the non-null value
     * @return an {@code HttpOptional} with the value and a 200 status code
     * @param <V> the type of the value
     */
    public static <V> HttpOptional<V> of(V value) {
        Objects.requireNonNull(value);
        return new HttpOptional<>(value, 200);
    }

    /**
     * Creates an empty optional value with an error status code.
     *
     * @param errorCode an HTTP status code for the error
     * @return an empty {@code HttpOptional} with the error status code
     * @param <V> the type of the value
     */
    public static <V> HttpOptional<V> empty(int errorCode) {
        return new HttpOptional<>(null, errorCode);
    }

    /**
     * Creates an optional value from a nullable value and an error status code to use for a null value.
     *
     * @param value the nullable value
     * @param errorCode an HTTP status code for the error, if the value is null
     * @return an {@code HttpOptional} with the value and a 200 status code if the value is non-null,
     *     otherwise an empty {@code HttpOptional} with the error status code
     * @param <V> the type of the value
     */
    public static <V> HttpOptional<V> ofNullable(V value, int errorCode) {
        return (value != null) ? of(value) : empty(errorCode);
    }

    /**
     * Creates an optional value from an optional value without a status code
     * and an error status code to use for an empty value.
     *
     * @param maybeValue the {@link Optional} value
     * @param errorCode an HTTP status code for the error, if the value is empty
     * @return an {@code HttpOptional} value that corresponds to the {@code Optional} value
     * @param <V> the type of the value
     */
    public static <V> HttpOptional<V> fromOptional(Optional<V> maybeValue, int errorCode) {
        return maybeValue.isPresent() ? of(maybeValue.get()) : empty(errorCode);
    }

    /**
     * Determines if the value is empty.
     *
     * @return true if the value is empty, otherwise false
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * Determines if the value is present.
     *
     * @return true if the value is present, otherwise false
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public V get() {
        if (value == null) {
            throw new NoSuchElementException();
        }

        return value;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code
     */
    public int statusCode() {
        return statusCode;
    }

    /**
     * Converts this empty optional value to a different type.
     * <p>
     * Often used when errors need to be propagated.
     *
     * @return this empty {@code HttpOptional} as a different type
     * @param <U> the new type
     */
    @SuppressWarnings("unchecked")
    public <U> HttpOptional<U> convertEmpty() {
        if (value != null) {
            throw new IllegalStateException("value is present");
        }

        return (HttpOptional<U>) this;
    }

    /**
     * Converts this optional value to an optional value without a status code.
     *
     * @return an {@link Optional} value that corresponds to this {@code HttpOptional} value
     */
    public Optional<V> toOptional() {
        return isPresent() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        HttpOptional<?> other = (o instanceof HttpOptional) ? (HttpOptional<?>) o : null;
        if (other == null) {
            return false;
        }

        return Objects.equals(value, other.value) && (statusCode == other.statusCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, statusCode);
    }

    @Override
    public String toString() {
        return (value != null)
                ? String.format("HttpOptional[%s]", value)
                : String.format("HttpOptional.empty[%d]", statusCode);
    }

    private HttpOptional(V value, int statusCode) {
        this.value = value;
        this.statusCode = statusCode;
    }
}
