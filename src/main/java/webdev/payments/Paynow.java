package webdev.payments;

import webdev.core.Constants;
import webdev.core.InitResponse;
import webdev.core.StatusResponse;
import webdev.exceptions.ConnectionException;
import webdev.exceptions.EmptyCartException;
import webdev.exceptions.HashMismatchException;
import webdev.exceptions.InvalidReferenceException;
import webdev.helpers.Hash;
import webdev.helpers.Utils;
import webdev.http.Client;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;


public class Paynow {
    /**
     * Merchant's return url
     */
    private String ResultUrl = "http://localhost";
    /**
     * Merchant's result url
     */
    private String ReturnUrl = "http://localhost";
    /**
     * Merchant's integration id
     */
    private String IntegrationKey;
    /**
     * Client for making http requests
     */
    private Client Client;
    /**
     * Merchant's integration key
     */
    private String IntegrationId;

    /**
     * Paynow constructor
     *
     * @param integrationId
     * @param integrationKey
     * @throws IllegalArgumentException
     */

    public Paynow(String integrationId, String integrationKey) {
        this(integrationId, integrationKey, null);
    }

    public Paynow(String integrationId, String integrationKey, String resultUrl) {
        if (integrationId.isEmpty()) {
            throw new IllegalArgumentException("Integration id cannot be empty");
        }
        if (integrationKey.isEmpty()) {
            throw new IllegalArgumentException("Integration key cannot be empty");
        }


        setIntegrationId(integrationId);
        setIntegrationKey(integrationKey);

        if (resultUrl != null) {
            setResultUrl(resultUrl);
        }


        setClient(new Client());
    }

    public final String getResultUrl() {
        return ResultUrl;
    }

    public final void setResultUrl(String value) {
        ResultUrl = value;
    }

    public final String getReturnUrl() {
        return ReturnUrl;
    }

    public final void setReturnUrl(String value) {
        ReturnUrl = value;
    }

    public final String getIntegrationKey() {
        return IntegrationKey;
    }

    public final void setIntegrationKey(String value) {
        IntegrationKey = value;
    }

    public final Client getClient() {
        return Client;
    }

    public final void setClient(Client value) {
        Client = value;
    }

    public final String getIntegrationId() {
        return IntegrationId;
    }

    public final void setIntegrationId(String value) {
        IntegrationId = value;
    }

    /**
     * Creates a new transaction
     *
     * @param reference
     * @param values
     * @return
     */

    public final Payment CreatePayment(String reference, java.util.HashMap<String, java.math.BigDecimal> values) {
        return CreatePayment(reference, values, "");
    }

    public final Payment CreatePayment(String reference) {
        return CreatePayment(reference, null, "");
    }

    public final Payment CreatePayment(String reference, HashMap<String, BigDecimal> values, String authEmail) {
        return values != null ? new Payment(reference, values, authEmail) : new Payment(reference, authEmail);
    }

    /**
     * Sends a payment to paynow
     *
     * @param payment
     * @return
     * @throws InvalidReferenceException
     * @throws EmptyCartException
     */
    public final InitResponse Send(Payment payment) {
        if (payment.getReference().isEmpty()) {
            throw new InvalidReferenceException();
        }

        if (payment.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new EmptyCartException();
        }

        return Init(payment);
    }

    /**
     * Polls the given poll url for a status update
     *
     * @param url The poll url to hit
     *
     * @throws HashMismatchException Thrown when hashes do not match
     * @throws ConnectionException Thrown when http request fails to go through
     *
     * @return The response from Paynow
     */
    public final StatusResponse PollTransaction(String url) throws HashMismatchException, ConnectionException {
       try  {
           String response = getClient().PostAsync(url, null);
           HashMap<String, String> data = Utils.ParseQueryString(response);

           if (!data.containsKey("hash") || Hash.Verify(data, getIntegrationKey())) {
               throw new HashMismatchException();
           }

           return new StatusResponse(data);
       } catch (IOException ex) {
           throw new ConnectionException(ex.getMessage());
       }
    }

    /**
     * Process a status update from Paynow
     *
     * @param response Raw POST string sent from Paynow
     * @return
     * @throws HashMismatchException
     */
    public final StatusResponse ProcessStatusUpdate(String response) {
        HashMap<String, String> data = Utils.ParseQueryString(response);

        if (!data.containsKey("hash") || Hash.Verify(data, getIntegrationKey())) {
            throw new HashMismatchException();
        }

        return new StatusResponse(data);
    }


    /**
     * Process a status update from Paynow
     *
     * @param response Key-value pairs of data sent from Paynow
     *
     * @throws HashMismatchException Thrown when hashes do not match
     *
     * @return The status response from Paynow
     */
    public final StatusResponse ProcessStatusUpdate(HashMap<String, String> response) {
        if (!response.containsKey("hash") || Hash.Verify(response, getIntegrationKey())) {
            throw new HashMismatchException();
        }

        return new StatusResponse(response);
    }

    public final InitResponse SendMobile(Payment payment, String phone, String method) {
        if (payment.getReference().isEmpty()) {
            throw new InvalidReferenceException();
        }

        if (payment.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new EmptyCartException();
        }

        if (!phone.matches("07([7,8])((\\1=7)[1-9]|[2-5])\\d{6}")) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        return InitMobile(payment, phone, method);
    }

    /**
     * Initiate a new Paynow mobile transaction
     *
     * @param payment The payment to send to Paynow
     * @param phone Payer's phone number
     * @param method The mobile money method to use
     *
     * @return The response from Paynow
     */
    private InitResponse InitMobile(Payment payment, String phone, String method) throws ConnectionException, HashMismatchException {
        try {
            HashMap<String, String> data = FormatMobileInitRequest(payment, phone, method);

            String response = getClient().PostAsync(Constants.UrlInitiateTransaction, data);

            if (!data.containsKey("hash") || Hash.Verify(data, getIntegrationKey())) {
                throw new HashMismatchException();
            }

            return new InitResponse(Utils.ParseQueryString(response));
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
    private InitResponse Init(Payment payment) throws ConnectionException, HashMismatchException {
        try {
            HashMap<String, String> data = FormatInitRequest(payment);

            String response = getClient().PostAsync(Constants.UrlInitiateTransaction, data);

            if (!data.containsKey("hash") || !Hash.Verify(data, getIntegrationKey())) {
                throw new HashMismatchException();
            }

            return new InitResponse(Utils.ParseQueryString(response));
        } catch (IOException ex) {
            throw new ConnectionException(ex.getMessage());
        }
    }

    /**
     * Formats an init request before its sent to Paynow
     *
     * @param payment
     * @return
     */
    private HashMap<String, String> FormatInitRequest(Payment payment) {
        HashMap<String, String> items = payment.ToDictionary();

        items.put("returnurl", getReturnUrl().trim());
        items.put("resulturl", getResultUrl().trim());
        items.put("id", getIntegrationId());

        items.put("hash", Hash.make(items, getIntegrationKey()));

        return items;
    }

    /**
     * Initiate a new Paynow transaction
     * <p>
     * <p>
     * Currently, only eccocash is supported
     *
     * @param payment The transaction to be sent to Paynow
     * @param phone   The user's phone number
     * @param method  The mobile transaction method i.e ecocash, telecash
     * @return
     */
    private HashMap<String, String> FormatMobileInitRequest(Payment payment, String phone, String method) {
        HashMap<String, String> items = payment.ToDictionary();

        items.put("returnurl", getReturnUrl().trim());
        items.put("resulturl", getResultUrl().trim());
        items.put("id", getIntegrationId());
        items.put("phone", phone);
        items.put("method", method);

        items.put("hash", Hash.make(items, getIntegrationKey()));

        return items;
    }
}