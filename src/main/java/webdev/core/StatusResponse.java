package webdev.core;

import webdev.exceptions.InvalidIntegrationException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * This class is a wrapper around the response received from Paynow
 * after checking the current status of a transaction
 */
public class StatusResponse extends CanFail implements IResponse {

    /**
     * The key-value data response from Paynow.
     */
    private Map<String, String> data;

    /**
     * Whether the poll request was successful.
     */
    private boolean wasSuccessful;

    /**
     * The transaction’s reference on the merchant site, which should be unique to the transaction.
     */
    private String reference;

    /**
     * Amount of the transaction, in USD, to two decimal places.
     */
    private BigDecimal amount = new BigDecimal(0);

    /**
     * Whether the payment has been completed i.e. the amount has been paid.
     */
    private boolean wasPaid;

    /**
     * StatusResponse constructor.
     *
     * @param response Response data received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public StatusResponse(Map<String, String> response) {
        data = response;

        load();
    }

    public final Map<String, String> getData() {
        return data;
    }

    protected final boolean getWasSuccessful() {
        return wasSuccessful;
    }

    protected final void setWasSuccessful(boolean value) {
        wasSuccessful = value;
    }

    public final String getReference() {
        return reference;
    }

    public final void setReference(String value) {
        reference = value;
    }

    public final BigDecimal getAmount() {
        return amount;
    }

    public final void setAmount(BigDecimal value) {
        amount = value;
    }

    public final boolean getWasPaid() {
        return wasPaid;
    }

    public final void setWasPaid(boolean value) {
        wasPaid = value;
    }


    /**
     * Method to return whether the poll request was successful.
     *
     * @return Returns true if poll request was successful otherwise returns false.
     */
    public final boolean success() {
        return getWasSuccessful();
    }

    /**
     * Read through the raw response data received from Paynow, and set values in StatusResponse class
     */
    private void load() {
        if (!getData().containsKey("error")) {
            setWasSuccessful(true);
        }

        if (getData().containsKey("status")) {
            setWasPaid(getData().get("status").toLowerCase().equals(Constants.responsePaid));
        }

        if (getData().containsKey("amount")) {
            setAmount(new BigDecimal(getData().get("amount")));
        }

        if (getData().containsKey("reference")) {
            setReference(getData().get("reference"));
        }

        if (getWasSuccessful()) {
            return;
        }

        if (getData().containsKey("error")) {
            fail(getData().get("error"));
        }
    }

    /**
     * Method to return the poll url
     *
     * @return Returns the poll url
     */
    public final String pollUrl() {
        return getData().containsKey("pollurl") ? getData().get("pollurl") : "";
    }

    /**
     * Method to return whether the transaction was successfully complete .i.e. whether customer has paid.
     *
     * @return Returns true if amount has been paid, otherwise returns false.
     */
    public final boolean paid() {
        return getWasPaid();
    }

}