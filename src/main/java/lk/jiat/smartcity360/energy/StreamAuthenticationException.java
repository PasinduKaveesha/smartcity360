package lk.jiat.smartcity360.energy;

/**
 * Exception thrown when stream payload authentication or HMAC validation fails.
 */
public class StreamAuthenticationException extends RuntimeException {

    public StreamAuthenticationException(String message) {
        super(message);
    }

    public StreamAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
