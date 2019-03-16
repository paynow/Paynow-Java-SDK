package zw.co.paynow.responses;

import zw.co.paynow.constants.TransactionStatus;
import zw.co.paynow.constants.TransactionType;
import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

/**
 * This class is a wrapper around the response received from Paynow
 * after initiating a transaction
 */
public abstract class InitResponse extends PaynowResponse {

    /**
     * The poll url to check the status of a transaction.
     */
    protected final String pollUrl;

    /**
     * The hash value generated
     */
    protected final String hash;

    /**
     * InitResponse constructor.
     * <br>
     * Read through the raw response content received from Paynow, and set response values
     *
     * @param response Raw response content received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public InitResponse(Map<String, String> response) throws InvalidIntegrationException {

        rawResponseContent = response;

        if (rawResponseContent.containsKey("status")) {
            requestSuccess = (rawResponseContent.get("status").equalsIgnoreCase(TransactionStatus.OK.getResponseString()));
        } else {
            requestSuccess = false;
        }

        if (rawResponseContent.containsKey("status")) {
            String rawStatus = rawResponseContent.get("status");
            status = TransactionStatus.getTransactionStatus(rawStatus);
        } else {
            status = TransactionStatus.UNDEFINED;
        }

        if (rawResponseContent.containsKey("hash")) {
            hash = rawResponseContent.get("hash");
        } else {
            hash = "";
        }

        if (rawResponseContent.containsKey("pollurl")) {
            pollUrl = rawResponseContent.get("pollurl");
        } else {
            pollUrl = "";
        }

        if (requestSuccess) {
            return;
        }

        if (rawResponseContent.containsKey("error")) {
            fail(rawResponseContent.get("error"));
        }

    }

    public String getHash() {
        return hash;
    }

    public String getPollUrl() {
        return pollUrl;
    }

    public String pollUrl() {
        return getPollUrl();
    }

}