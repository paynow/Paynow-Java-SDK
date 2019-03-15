package zw.co.paynow.exceptions;

/**
 * Exception is thrown when an empty cart is attempted to be submitted to Paynow
 */
public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Please submit a cart with at one or more items.");
    }
}