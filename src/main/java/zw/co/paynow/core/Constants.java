package zw.co.paynow.core;

/**
 * Contains a list of re-used constants
 */
public final class Constants {

    //Response constants
    public static final String responseOk = "ok";
    public static final String responsePaid = "paid";
    public static final String responseError = "error";
    public static final String responseInvalidId = "invalid id.";

    //URL for initiating a transaction
    public static final String urlInitiateTransaction = "https://www.paynow.co.zw/interface/initiatetransaction";

    //URL for initiating a mobile transaction
    public static final String urlInitiateMobileTransaction = "https://www.paynow.co.zw/interface/remotetransaction";

    //Mobile money method constants
    public static final String mobileMoneyMethodEcocash = "ecocash";
}