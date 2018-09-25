package webdev.core;

import webdev.exceptions.InvalidIntegrationException;

import java.util.Map;


/**
 * This class is a wrapper around the response sent from Paynow
 * when initiating a transaction
 */
public class InitResponse extends CanFail {
    private Map<String, String> Data;
    private boolean WasSuccessful;
    private boolean HasRedirect;
    private String instructions;
    /**
     * InitResponse constructor.
     *
     * @param response Response data sent from Paynow
     * @throws InvalidIntegrationException If the error returned from paynow is
     */
    public InitResponse(Map<String, String> response) {
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

    protected final boolean getHasRedirect() {
        return HasRedirect;
    }

    protected final void setHasRedirect(boolean value) {
        HasRedirect = value;
    }

    /**
     * Reads through the response data sent from Paynow
     */
    private void load() {
        if (getData().containsKey("status")) {
            setWasSuccessful(getData().get("status").toLowerCase().equals(Constants.ResponseOk));
        }

        if (getData().containsKey("browserurl")) {
            setHasRedirect(true);
        }

        if (getData().containsKey("instructions")) {
            setInstructions(getData().get("instructions"));
        }

        if (getWasSuccessful()) {
            return;
        }

        if (getData().containsKey("error")) {
            fail(getData().get("error"));
        }


    }

    /**
     * Returns the poll URL sent from PaynowgetInstructions
     *
     * @return
     */
    public final String pollUrl() {
        return getData().containsKey("pollurl") ? getData().get("pollurl") : "";
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
     * Returns the url the user should be taken to so they can make a payment
     *
     * @return
     */
    public final String redirectLink() {
        return getHasRedirect() ? getData().get("browserurl") : "";
    }

    public final String instructions() {
        return getWasSuccessful() ? getInstructions() : "";
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}