package zw.co.paynow.exceptions;

import java.lang.RuntimeException;

/**
 * This exception is thrown when the application fails to send HTTP requests
 */
public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }
}
