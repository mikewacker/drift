package io.github.mikewacker.drift.endpoint;

/** Unchecked exception that tunnels exceptions, including checked exceptions. */
final class TunneledException extends RuntimeException {

    private final Exception cause;

    /** Creates a tunneled exception. */
    public static TunneledException tunnel(Exception cause) {
        return new TunneledException(cause);
    }

    @Override
    public Exception getCause() {
        return cause;
    }

    private TunneledException(Exception cause) {
        this.cause = cause;
    }
}
