package webdev.core;

import webdev.exceptions.InvalidIntegrationException;

import java.math.BigDecimal;
import java.util.Map;

public class StatusResponse extends CanFail implements IResponse {
    private Map<String, String> Data;
    private boolean WasSuccessful;
    private String Reference;
    private BigDecimal Amount = new BigDecimal(0);
    private boolean WasPaid;

    /**
     * InitResponse constructor.
     *
     * @param response Response data sent from Paynow
     * @throws InvalidIntegrationException If the error returned from paynow is
     */
    public StatusResponse(Map<String, String> response) {
        Data = response;

        load();
    }

    public final Map<String, String> getData() {
        return Data;
    }

    protected final boolean getWasSuccessful() {
        return WasSuccessful;
    }

    protected final void setWasSuccessful(boolean value) {
        WasSuccessful = value;
    }

    public final String getReference() {
        return Reference;
    }

    public final void setReference(String value) {
        Reference = value;
    }

    public final BigDecimal getAmount() {
        return Amount;
    }

    public final void setAmount(BigDecimal value) {
        Amount = value;
    }

    public final boolean getWasPaid() {
        return WasPaid;
    }

    public final void setWasPaid(boolean value) {
        WasPaid = value;
    }


    /**
     * Gets a boolean indicating whether a request succeeded or failed
     *
     * @return
     */
    public final boolean success() {
        return getWasSuccessful();
    }

    /**
     * Reads through the response data sent from Paynow
     */
    private void load() {
        if (!getData().containsKey("error")) {
            setWasSuccessful(true);
        }

        if (getData().containsKey("status")) {
            setWasPaid(getData().get("status").toLowerCase().equals(Constants.ResponsePaid));
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
     * Returns the poll URL sent from Paynow
     *
     * @return
     */
    public final String pollUrl() {
        return getData().containsKey("pollurl") ? getData().get("pollurl") : "";
    }

    public final boolean paid() {
        return getWasPaid();
    }

}