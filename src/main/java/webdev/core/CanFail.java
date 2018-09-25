package webdev.core;

import webdev.exceptions.InvalidIntegrationException;

import java.util.ArrayList;

public abstract class CanFail {

    /**
     * List of errors
     */
    private final ArrayList<String> _errors = new ArrayList<>();

    /**
     * Throws an exception for critical errors and stores other non-critical errors
     *
     * @param error The message for the exception
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public void fail(String error) {
        switch (error) {
            case Constants.ResponseInvalidId:
                throw new InvalidIntegrationException();
            default:
                _errors.add(error);
                break;
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
        for (String s : _errors) {
            sb.append(s);
            sb.append(separator);
        }

        return sb.toString();
    }
}