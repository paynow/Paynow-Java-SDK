package zw.co.paynow.exceptions;

/**
 * Exception is thrown if Paynow reports that merchant used an invalid integration
 */
public class InvalidIntegrationException extends RuntimeException {
    public InvalidIntegrationException() {
        super("Please use a valid integration with Paynow. If you do not have one already, please follow the instruction in the documentation.");
    }
}