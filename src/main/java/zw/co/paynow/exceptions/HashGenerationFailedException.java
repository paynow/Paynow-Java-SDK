package zw.co.paynow.exceptions;

/**
 * Exception is thrown when the hash generation fails
 */
public class HashGenerationFailedException extends RuntimeException {
    public HashGenerationFailedException() {
        super("An error occurred while generating the hash");
    }
}
