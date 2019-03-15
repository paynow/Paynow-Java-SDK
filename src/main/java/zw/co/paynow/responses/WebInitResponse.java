package zw.co.paynow.responses;

import zw.co.paynow.constants.TransactionType;
import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.util.Map;

public class WebInitResponse extends InitResponse {

    /**
     * The redirect URL i.e. The URL to the Paynow website that the merchant site will redirect the customer’s browser to in order to make the payment.
     */
    protected final String redirectURL;

    /**
     * WebInitResponse constructor.
     * <br>
     * Read through the raw response content received from Paynow, and set response values
     *
     * @param response Raw response content received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public WebInitResponse(Map<String, String> response) throws InvalidIntegrationException {
        super(response);

        if (rawResponseContent.containsKey("browserurl")) {
            redirectURL = rawResponseContent.get("browserurl");
        } else {
            redirectURL = "";
        }
    }

    public String getRedirectURL() {
        return redirectURL;
    }

}
