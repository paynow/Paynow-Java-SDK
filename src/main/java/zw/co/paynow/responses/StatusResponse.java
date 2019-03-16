package zw.co.paynow.responses;

import zw.co.paynow.constants.TransactionStatus;
import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

/**
 * This class is a wrapper around the status response received from Paynow
 * after checking the current status of a transaction
 */
public class StatusResponse extends PaynowResponse {

    /**
     * The transaction’s reference on the merchant site, which should be unique to the transaction.
     */
    private final String merchantReference;

    /**
     * The transaction’s reference from Paynow, which is unique to the transaction at Paynow.
     */
    private final String paynowReference;

    /**
     * Amount of the transaction, in RTGS$/USD
     */
    private final BigDecimal amount;

    /**
     * Whether the payment has been completed i.e. the amount has been paid.
     */
    private final boolean paid;

    /**
     * StatusResponse constructor.
     * <br>
     * Read through the raw response content received from Paynow, and set response values
     *
     * @param response Raw response content received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public StatusResponse(Map<String, String> response) throws InvalidIntegrationException {

        rawResponseContent = response;

        if (!rawResponseContent.containsKey("error")) {
            requestSuccess = true;
        } else {
            requestSuccess = false;
        }

        if (rawResponseContent.containsKey("status")) {
            String rawStatus = rawResponseContent.get("status");
            status = TransactionStatus.getTransactionStatus(rawStatus);

            paid = rawResponseContent.get("status").equalsIgnoreCase(TransactionStatus.PAID.getResponseString());
        } else {
            paid = false;

            status = TransactionStatus.UNDEFINED;
        }

        if (rawResponseContent.containsKey("amount")) {
            amount = new BigDecimal(rawResponseContent.get("amount"));
        } else {
            //set a default value of zero
            amount = new BigDecimal(0);
        }

        if (rawResponseContent.containsKey("reference")) {
            merchantReference = rawResponseContent.get("reference");
        } else {
            merchantReference = "";
        }

        if (rawResponseContent.containsKey("paynowreference")) {
            paynowReference = rawResponseContent.get("paynowreference");
        } else {
            paynowReference = "";
        }

        if (requestSuccess) {
            return;
        }

        if (rawResponseContent.containsKey("error")) {
            fail(rawResponseContent.get("error"));
        }

    }

    /**
     * Method to return the poll url
     *
     * @return Returns the poll url
     */
    public final String pollUrl() {
        return rawResponseContent.containsKey("pollurl") ? rawResponseContent.get("pollurl") : "";
    }

    /**
     * The hash value generated
     *
     * @return Returns the hash
     */
    public final String hash() {
        return rawResponseContent.containsKey("hash") ? rawResponseContent.get("hash") : "";
    }

    //GETTER METHODS
    public String getMerchantReference() {
        return merchantReference;
    }

    public String getPaynowReference() {
        return paynowReference;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amount.doubleValue()).round(new MathContext(2, RoundingMode.HALF_UP));
    }

    public boolean paid() {
        return isPaid();
    }

    public boolean isPaid() {
        return paid;
    }
    //END OF GETTER METHODS

}