package zw.co.paynow.constants;

/**
 * Enumeration for status type of the transaction
 * <p>
 * See documentation for more information on the different types of status.
 */
public enum TransactionStatus {

    OK("Ok", "Transaction initiation request is successful."),
    PAID("Paid", "Transaction paid successfully, the merchant will receive the funds at next settlement."),
    CANCELLED("Cancelled", "The transaction has been cancelled in Paynow and may not be resumed and needs to be recreated."),
    SENT("Sent", "Transaction has been created in Paynow and an up stream system, the customer has been referred to that upstream system but has not yet made payment."),
    DELIVERY("Awaiting Delivery", ""),
    DISPUTED("Disputed", "Transaction has been disputed by the Customer and funds are being held in suspense until the dispute has been resolved."),
    REFUNDED("Refunded", "Funds were refunded back to the customer."),
    DELIVERED("Delivered", "The user or merchant has acknowledged delivery of the goods but the funds are still sitting in suspense awaiting the 24 hour confirmation window to close."),
    CREATED("Created", "Transaction has been created in Paynow, but has not yet been paid by the customer."),
    ERROR("Error", "Transaction initiation request failed."),
    INVALID_ID("Invalid id.", "Transaction not initiated. Merchant supplied an invalid integration."),

    //this is a fallback value for a transaction status that is now found when calling getTransactionStatus
    UNDEFINED("unknown", "Transaction status is not defined in this SDK or the transaction status does not exist. See raw response for actual status.");

    private String responseString;
    private String description;

    TransactionStatus(String responseString, String description) {
        this.responseString = responseString;
        this.description = description;
    }

    public static TransactionStatus getTransactionStatus(String string) {
        for (TransactionStatus transactionStatus : TransactionStatus.values()) {
            if (transactionStatus.getResponseString().equalsIgnoreCase(string)) {
                return transactionStatus;
            }
        }
        return UNDEFINED;
    }

    public String getResponseString() {
        return responseString;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return responseString + ". " + description;
    }

}
