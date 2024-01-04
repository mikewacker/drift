package io.github.mikewacker.drift.endpoint;

/** Unchecked exception that tunnels a checked exception. */
final class TunneledException extends RuntimeException {

    private final Exception cause;

    /** Creates a tunneled exception from a checked exception. */
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
