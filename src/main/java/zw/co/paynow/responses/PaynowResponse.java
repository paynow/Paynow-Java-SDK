package zw.co.paynow.responses;

import zw.co.paynow.constants.TransactionStatus;
import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for all request responses from Paynow
 */
public abstract class PaynowResponse {

    /**
     * The key-value raw response content response from Paynow.
     */
    protected Map<String, String> rawResponseContent;

    /**
     * The status of the transaction
     */
    protected TransactionStatus status;

    /**
     * Whether a Paynow request was successful.
     */
    protected boolean requestSuccess;

    /**
     * List of errors in the request if any
     */
    private final ArrayList<String> errors = new ArrayList<>();

    /**
     * Throws an exception for critical errors and stores other non-critical errors
     *
     * @param error The message for the exception
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public void fail(String error) {
        if (error.equalsIgnoreCase(TransactionStatus.INVALID_ID.getResponseString())) {
            throw new InvalidIntegrationException();
        } else {
            errors.add(error);
        }
    }

    /**
     * Get the errors sent by Paynow
     *
     * @return The errors sent by paynow
     */
    public final String errors() {
        return errors(',');
    }

    /**
     * Get the errors sent by Paynow
     *
     * @param separator The character to separate the errors with
     * @return The errors from paynow
     */
    private String errors(char separator) {
        StringBuilder sb = new StringBuilder();
        for (String s : errors) {
            sb.append(s);
            sb.append(separator);
        }

        return sb.toString();
    }

    public void addError(String error) {
        errors.add(error);
    }

    //GETTER METHODS
    public boolean isRequestSuccess() {
        return requestSuccess;
    }

    public Map<String, String> getRawResponseContent() {
        return new HashMap<>(rawResponseContent);
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public ArrayList<String> getErrors() {
        return new ArrayList<>(errors);
    }
    //END OF GETTER METHODS

}