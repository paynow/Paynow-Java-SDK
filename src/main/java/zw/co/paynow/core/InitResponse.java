package zw.co.paynow.core;

import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.util.Map;

/**
 * This class is a wrapper around the response received from Paynow
 * after initiating a transaction
 */
public class InitResponse extends CanFail {

    /**
     * The key-value data response from Paynow.
     */
    private Map<String, String> data;

    /**
     * Whether the transaction initiation was successful.
     */
    private boolean wasSuccessful;

    /**
     * The error message if an error has occurred
     */
    private String error = "";

    /**
     * Whether a redirect link was returned by Paynow. Redirect link is returned for web based transactions.
     */
    private boolean hasRedirect;

    /**
     * Instructions on how to complete mobile based transaction using specified mobile money method. Instructions are returned for mobile based transactions.
     */
    private String instructions;

    /**
     * InitResponse constructor.
     *
     * @param response Response data received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public InitResponse(Map<String, String> response) {
        data = response;

        load();
    }

    public final Map<String, String> getData() {
        return data;
    }

    public boolean isWasSuccessful() {
        return wasSuccessful;
    }

    public void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }

    protected final boolean getHasRedirect() {
        return hasRedirect;
    }

    protected final void setHasRedirect(boolean value) {
        hasRedirect = value;
    }

    /**
     * Read through the raw response data received from Paynow, and set values in InitResponse class
     */
    private void load() {
        if (getData().containsKey("status")) {
            setWasSuccessful(getData().get("status").toLowerCase().equals(Constants.responseOk));
        }

        if (getData().containsKey("browserurl")) {
            setHasRedirect(true);
        }

        if (getData().containsKey("instructions")) {
            setInstructions(getData().get("instructions"));
        }

        if (isWasSuccessful()) {
            return;
        }

        if (getData().containsKey("error")) {
            setError(getData().get("error"));
            fail(getData().get("error"));
        }


    }

    /**
     * Method to return the poll url if it is a successful web based transaction.
     *
     * @return Returns the poll url
     */
    public final String pollUrl() {
        return getData().containsKey("pollurl") ? getData().get("pollurl") : "";
    }


    /**
     * Method to return whether the transaction initiation was successful.
     *
     * @return Returns true if transaction initiation was successful otherwise returns false.
     */
    public final boolean success() {
        return isWasSuccessful();
    }

    /**
     * For a web based transaction. Returns the redirect link i.e. The URL to the Paynow website that the merchant site will redirect the customer’s browser to in order to make the payment.
     *
     * @return Returns the redirect link
     */
    public final String redirectLink() {
        return getHasRedirect() ? getData().get("browserurl") : "";
    }

    /**
     * For a mobile based transaction. Returns the instructions to show to the customer how to complete the payment using mobile money.
     *
     * @return Returns the mobile money payment instructions
     */
    public final String instructions() {
        return isWasSuccessful() ? getInstructions() : "";
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "InitResponse{" +
                "data=" + data +
                ", wasSuccessful=" + wasSuccessful +
                ", error='" + error + '\'' +
                ", hasRedirect=" + hasRedirect +
                ", instructions='" + instructions + '\'' +
                '}';
    }

}