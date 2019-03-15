package zw.co.paynow.exceptions;

/**
 * Exception is thrown when user attempts sending an empty transaction reference value to Paynow
 */
public class InvalidReferenceException extends RuntimeException {
    public InvalidReferenceException() {
        super("Please specify a transaction reference that is unique at the merchant.");
    }
}