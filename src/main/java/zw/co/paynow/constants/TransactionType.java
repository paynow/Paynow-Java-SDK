package zw.co.paynow.constants;

/**
 * Enumeration for the mobile money method to use
 * <p>
 * Currently only ECOCASH supported
 */
public enum TransactionType {
    WEB,
    MOBILE,
    UNDEFINED //Transaction type could not be parsed
}