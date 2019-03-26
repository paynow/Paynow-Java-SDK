package zw.co.paynow.core;

import zw.co.paynow.constants.MobileMoneyMethod;
import zw.co.paynow.constants.PaynowUrls;
import zw.co.paynow.exceptions.ConnectionException;
import zw.co.paynow.exceptions.EmptyCartException;
import zw.co.paynow.exceptions.HashMismatchException;
import zw.co.paynow.exceptions.InvalidReferenceException;
import zw.co.paynow.http.HttpClient;
import zw.co.paynow.parsers.UrlParser;
import zw.co.paynow.responses.MobileInitResponse;
import zw.co.paynow.responses.StatusResponse;
import zw.co.paynow.responses.WebInitResponse;
import zw.co.paynow.validators.EmailValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Represents a Paynow integration related to a integration ID and key.
 * Object created using this class can be used to carry out multiple transactions.
 */
public class Paynow {

    /**
     * The URL on the merchant website Paynow will post transaction results to. It is recommended this
     * URL contains enough information for the merchant site to identify the transaction.
     */
    private String resultUrl = "http://localhost";

    /**
     * The URL on the merchant website the customer will be redirected to after the transaction
     * has been processed. It is recommended this URL contains enough information for the merchant
     * site to identify the transaction.
     */
    private String returnUrl = "http://localhost";

    /**
     * Integration ID shown to the merchant in the “3rd Party Site or Link Profile” area of “Receive Payment Links”
     * section of “Sell or Receive” on Paynow.
     */
    private String integrationId;

    /**
     * Integration key sent to the merchant via email after requesting it in the “3rd Party Site or Link Profile” area of
     * “Receive Payment Links” section of “Sell or Receive” on Paynow.
     */
    private String integrationKey;

    /**
     * Http httpHttpClient for making http requests
     */
    private HttpClient httpHttpClient;

    /**
     * Constructor for new Paynow object
     *
     * @param integrationId  ID shown to the merchant in the Paynow website
     * @param integrationKey key sent to the merchant via email from Paynow
     */
    public Paynow(String integrationId, String integrationKey) {
        this(integrationId, integrationKey, null);
    }

    /**
     * Constructor for new Paynow object
     *
     * @param integrationId  ID shown to the merchant in the Paynow website
     * @param integrationKey Key sent to the merchant via email from Paynow
     * @param resultUrl      URL on the merchant website Paynow will post transaction results to
     * @throws IllegalArgumentException Thrown if empty argument is passed as parameter
     */
    public Paynow(String integrationId, String integrationKey, String resultUrl) {

        if (integrationId.isEmpty()) {
            throw new IllegalArgumentException("Integration id cannot be empty");
        }
        if (integrationKey.isEmpty()) {
            throw new IllegalArgumentException("Integration key cannot be empty");
        }

        this.integrationId = integrationId;
        this.integrationKey = integrationKey;

        if (resultUrl != null) {
            this.resultUrl = resultUrl;
        }

        httpHttpClient = new HttpClient();
    }

    /**
     * Creates a new 'Payment' object to be used to initiate a transaction using the 'Paynow' class.
     * Requires reference and cart values. Cannot be used for mobile transactions.
     *
     * @param merchantReference Unique transaction’s reference on the merchant site
     * @param cart              List of items in the cart
     * @return New 'Payment' instance associated with reference
     */
    public final Payment createPayment(String merchantReference, java.util.HashMap<String, java.math.BigDecimal> cart) {
        return createPayment(merchantReference, cart, "");
    }

    /**
     * Creates a new 'Payment' object to be used to initiate a transaction using the 'Paynow' class.
     * Requires reference and email. Can be used for mobile transactions.
     *
     * @param merchantReference Unique transaction’s reference on the merchant site
     * @param email             E-mail address of the user making the payment
     * @return New 'Payment' instance associated with reference
     */
    public final Payment createPayment(String merchantReference, String email) {
        return createPayment(merchantReference, null, email);
    }

    /**
     * Creates a new 'Payment' object to be used to initiate a transaction using the 'Paynow' class.
     * Requires reference. Cannot be used for mobile transactions.
     *
     * @param merchantReference Unique transaction’s reference on the merchant site
     * @return New 'Payment' instance associated with reference
     */
    public final Payment createPayment(String merchantReference) {
        return createPayment(merchantReference, null, "");
    }

    /**
     * Creates a new 'Payment' object to be used to initiate a transaction using the 'Paynow' class.
     * Requires reference, cart, and authEmail. Can be used for mobile transactions.
     *
     * @param merchantReference Unique transaction’s reference on the merchant site
     * @param cart
     * @param authEmail
     * @return New 'Payment' instance associated with reference
     */
    public final Payment createPayment(String merchantReference, HashMap<String, BigDecimal> cart, String authEmail) {
        return cart != null ? new Payment(merchantReference, cart, authEmail) : new Payment(merchantReference, authEmail);
    }

    /**
     * Sends a request to Paynow so that a web transaction can be initialised. When the transaction is successfully
     * initialised, the customer can now make the payment, and the transaction can be polled for its status.
     *
     * @param payment 'Payment' instance associated with a reference used to initialise a transaction
     * @return Response object containing information about the result of the request
     * @throws InvalidReferenceException Thrown if reference is empty
     * @throws EmptyCartException        Thrown if cart cart total is less than or equal to zero
     */
    public final WebInitResponse send(Payment payment) {
        validatePayment(payment);

        return initWebTransaction(payment);
    }

    /**
     * Ensure payment field values are valid
     *
     * @param payment 'Payment' to validate
     */
    private void validatePayment(Payment payment) {
        if (payment.getMerchantReference().isEmpty()) {
            throw new InvalidReferenceException();
        }

        if (payment.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new EmptyCartException();
        }
    }

    /**
     * Verify map has valid hash
     *
     * @param data Hashmap to validate
     */
    private void verifyHash(HashMap<String, String> data) {
        if (!data.containsKey("hash") || !HashGenerator.verify(data, integrationKey)) {
            throw new HashMismatchException(data.get("Error"));
        }
    }

    /**
     * Polls the given poll url for a status update
     *
     * @param url The poll url to hit
     * @return The response from Paynow
     * @throws HashMismatchException Thrown when hashes do not match
     * @throws ConnectionException   Thrown when http request fails to go through
     */
    public final StatusResponse pollTransaction(String url) throws HashMismatchException, ConnectionException {
        try {
            String response = httpHttpClient.postAsync(url, null);
            return parseStatus(response);
        } catch (IOException ex) {
            throw new ConnectionException(ex.getMessage());
        }
    }

    /**
     * Process a status update from Paynow
     *
     * @param response Raw POST string sent from Paynow
     * @return StatusResponse Return response from Paynow
     * @throws HashMismatchException Thrown when hashes do not match
     */
    protected StatusResponse parseStatus(String response) throws HashMismatchException {
        HashMap<String, String> data = UrlParser.parseMapFromQueryString(response);
        verifyHash( data);

        return new StatusResponse(data);
    }

    /**
     * Process a status update from Paynow
     *
     * @param response Key-value pairs of data sent from Paynow
     * @return The status response from Paynow
     * @throws HashMismatchException Thrown when hashes do not match
     */
    protected final StatusResponse parseStatus(HashMap<String, String> response) {
        verifyHash( response);
        return new StatusResponse(response);
    }

    public final MobileInitResponse sendMobile(Payment payment, String phone, MobileMoneyMethod mMoneyMethod) {
        validatePayment(payment);

        return initMobileTransaction(payment, phone, mMoneyMethod);
    }

    /**
     * Initiate a new Paynow mobile transaction
     *
     * @param payment      The payment to send to Paynow
     * @param phone        Payer's phone number
     * @param mMoneyMethod The mobile money method to use
     * @return The response from Paynow
     */
    private MobileInitResponse initMobileTransaction(Payment payment, String phone, MobileMoneyMethod mMoneyMethod) throws ConnectionException, HashMismatchException {
        try {
            HashMap<String, String> data = formatInitMobileTransactionRequest(payment, phone, mMoneyMethod);

            String email = data.get("authemail");

            if (email == null || email.isEmpty() || !EmailValidator.validateEmail(email)) {
                throw new IllegalArgumentException("Auth email is required for mobile transactions. Please pass a valid email address to the createPayment method");
            }

            HashMap<String, String> response = UrlParser.parseMapFromQueryString(
                    httpHttpClient.postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data)
            );

            if (!response.get("status").equalsIgnoreCase("error") && (!response.containsKey("hash") || !HashGenerator.verify(response, integrationKey))) {
                throw new HashMismatchException(response.get("Error"));
            }

            return new MobileInitResponse(response);
        } catch (IOException ex) {
            throw new ConnectionException(ex.getMessage());
        }
    }


    /**
     * Initiate a new Paynow transaction
     *
     * @param payment The payment to send to Paynow
     * @return The response from Paynow
     */
    private WebInitResponse initWebTransaction(Payment payment) throws ConnectionException, HashMismatchException {
        try {
            HashMap<String, String> data = formatInitWebTransactionRequest(payment);

            HashMap<String, String> response = UrlParser.parseMapFromQueryString(
                    httpHttpClient.postAsync(PaynowUrls.INITIATE_TRANSACTION, data)
            );

            if (!response.get("status").equalsIgnoreCase("error") && (!response.containsKey("hash") || !HashGenerator.verify(response, integrationKey))) {
                throw new HashMismatchException(response.get("Error"));
            }

            return new WebInitResponse(response);
        } catch (IOException ex) {
            throw new ConnectionException(ex.getMessage());
        }
    }

    /**
     * Method used to format a web transaction initialisation request. Creates a map of parameters
     * to be sent and hashes the result using the integration key.
     *
     * @param payment 'Payment' instance associated with a reference used to initialise a transaction
     * @return Formatted hash map of string values to be sent in request
     */
    private HashMap<String, String> formatInitWebTransactionRequest(Payment payment) {
        HashMap<String, String> items = payment.toDictionary();

        items.put("returnurl", returnUrl.trim());
        items.put("resulturl", resultUrl.trim());
        items.put("id", integrationId);

        items.put("hash", HashGenerator.make(items, integrationKey));

        return items;
    }

    /**
     * Method used to format a mobile transaction initialisation request. Creates a map of parameters
     * to be sent and hashes the result using the integration key.
     *
     * @param payment      'Payment' instance associated with a reference used to initialise a transaction
     * @param phone        The customer phone number to be used to make the payment
     * @param mMoneyMethod The mobile money method to be used to complete the transaction e.g. Ecocash
     * @return Formatted hash map of string values to be sent in request
     */
    private HashMap<String, String> formatInitMobileTransactionRequest(Payment payment, String phone, MobileMoneyMethod mMoneyMethod) {
        HashMap<String, String> items = payment.toDictionary();

        items.put("returnurl", returnUrl.trim());
        items.put("resulturl", resultUrl.trim());
        items.put("id", integrationId);
        items.put("phone", phone);
        items.put("method", mMoneyMethod.toString());

        items.put("hash", HashGenerator.make(items, integrationKey));

        return items;
    }

    //GETTER AND SETTER METHODS
    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public String getIntegrationKey() {
        return integrationKey;
    }

    protected HttpClient getHttpHttpClient() {
        return httpHttpClient;
    }

    public void setHttpHttpClient(HttpClient httpHttpClient) {
        this.httpHttpClient = httpHttpClient;
    }
    //END OF GETTER AND SETTER METHODS

}
