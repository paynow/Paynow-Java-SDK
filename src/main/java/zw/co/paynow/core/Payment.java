package zw.co.paynow.core;

import zw.co.paynow.parsers.PaymentParser;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Represents a single transaction related to a merchantReference on the merchant site.
 */
public class Payment {

    /**
     * Boolean used to determine if the merchant has supplied a description of the transaction.
     * e.g. If a description is not supplied, this value will remain as false and the description will be generated in the SDK
     */
    private boolean overrideDescription = false;

    /**
     * The description of the transaction as set by the merchant
     */
    public String cartDescription = "";

    /**
     * (optional) If the field is present and set to an email address Paynow will attempt to auto
     * login the customers email address as an anonymous user. If the email address has a registered account
     * the user will be prompted to login to that account.
     */
    public String authEmail = "";

    /**
     * The transaction’s reference on the merchant site, this should be unique to the transaction.
     */
    private String merchantReference;

    /**
     * List of the items in the cart to send as a transaction, i.e. the item description and amount
     */
    private HashMap<String, BigDecimal> cart;

    /**
     * Constructor for new Payment object
     *
     * @param merchantReference Unique transaction’s merchantReference on the merchant site
     * @param authEmail         E-mail address of the user making the payment. Will be used to automatically login if possible
     */
    public Payment(String merchantReference, String authEmail) {
        this.merchantReference = merchantReference;
        cart = new HashMap<>();
        this.authEmail = authEmail;
    }

    /**
     * Constructor for new Payment object
     *
     * @param merchantReference Unique transaction’s merchantReference on the merchant site
     * @param cart              List of items in the cart
     * @param authEmail         E-mail address of the user making the payment. Will be used to automatically login if possible
     */
    public Payment(String merchantReference, HashMap<String, BigDecimal> cart, String authEmail) {
        this.merchantReference = merchantReference;
        this.cart = cart;
        this.authEmail = authEmail;
    }

    /**
     * Adds a new item to the transaction
     *
     * @param title  The name of the item
     * @param amount The cost of the item
     */
    public final Payment add(String title, double amount) {
        cart.put(title, new BigDecimal(amount));
        return this;
    }

    /**
     * Adds a new item to the cart for the transaction
     *
     * @param title  The name of the item
     * @param amount The cost of the item
     */
    public final Payment add(String title, int amount) {
        cart.put(title, new BigDecimal(amount));

        return this;
    }

    /**
     * Adds a new item to the cart for the transaction
     *
     * @param title  The name of the item
     * @param amount The cost of the item
     */
    public final Payment add(String title, BigDecimal amount) {
        cart.put(title, amount);

        return this;
    }

    /**
     * Removes an item from the transaction
     *
     * @param title The name of the item
     */
    public final Payment remove(String title) {
        HashMap<String, BigDecimal> items = cart;

        if (items.containsKey(title)) {
            cart.remove(title);
        }

        return this;
    }

    /**
     * Calculate the total amount of the cart in the cart for the transaction
     */
    private BigDecimal calculateTotal() {
        return PaymentParser.addCollectionValues(cart).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Sets the description of the transaction cart that will appear as 'Additional Payee Info' on Paynow
     *
     * @param description The description of the transaction cart
     */
    public void setCartDescription(String description) {
        this.cartDescription = description;
        this.overrideDescription = true;
    }

    /**
     * Get the cart in the transaction cart as a dictionary
     *
     * @return HashGenerator map of cart in the cart as a dictionary
     */
    public final HashMap<String, String> toDictionary() {
        HashMap<String, String> map = new HashMap<>();

        map.put("resulturl", "");
        map.put("returnurl", "");
        map.put("reference", merchantReference);
        map.put("amount", getTotal().toString());
        map.put("id", "");
        map.put("additionalinfo", getCartDescription());
        map.put("authemail", authEmail);
        map.put("status", "Message");

        return map;
    }

    /**
     * Get the total amount of the items in the cart for the transaction
     */
    public final BigDecimal getTotal() {
        return calculateTotal();
    }

    //GETTER AND SETTER METHODS
    public boolean isOverrideDescription() {
        return overrideDescription;
    }

    public String getAuthEmail() {
        return authEmail;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public HashMap<String, BigDecimal> getCart() {
        return new HashMap<>(cart);
    }

    public String getCartDescription() {
        if (this.overrideDescription) {
            return PaymentParser.flattenCollection(cart).trim();
        } else {
            return this.cartDescription;
        }
    }
    //END OF GETTER AND SETTER METHODS

}
