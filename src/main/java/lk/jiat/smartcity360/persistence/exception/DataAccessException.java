package lk.jiat.smartcity360.persistence.exception;

/**
 * Runtime exception thrown when persistence operations encounter errors.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
