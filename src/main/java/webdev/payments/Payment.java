package webdev.payments;

import webdev.helpers.Utils;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Represents a single transaction related to a reference on the merchant site.
 */
public class Payment {

    /**
     * Boolean used to determine the value of the description of the transaction.
     * e.g. If a description is not supplied, this value will remain as false and the description will be generated
     */
    private boolean override = false;

    /**
     * The default valeu of the description of the transaction.
     */
    public String defaultDescription = "";

    /**
     * (optional) If the field is present and set to an email address Paynow will attempt to auto
     * login the customers email address as an anonymous user. If the email address has a registered account
     * the user will be prompted to login to that account.
     */
    public String authEmail = "";

    /**
     * The transaction’s reference on the merchant site, this should be unique to the transaction.
     */
    private String reference;

    /**
     * List of the items in the cart to send as a transaction, i.e. the item description and amount
     */
    private HashMap<String, BigDecimal> items;

    /**
     * Constructor for new Payment object
     *
     * @param reference Unique transaction’s reference on the merchant site
     * @param authEmail E-mail address of the user making the payment. Will be used to automatically login if possible
     */
    public Payment(String reference, String authEmail) {
        setReference(reference);
        items = new HashMap<String, BigDecimal>();
        this.authEmail = authEmail;
    }

    /**
     * Constructor for new Payment object
     *
     * @param reference Unique transaction’s reference on the merchant site
     * @param values    List of items in the cart
     * @param authEmail E-mail address of the user making the payment. Will be used to automatically login if possible
     */
    public Payment(String reference, HashMap<String, BigDecimal> values, String authEmail) {
        setReference(reference);
        items = values;
        this.authEmail = authEmail;
    }

    public final String getReference() {
        return reference;
    }

    public final void setReference(String value) {
        reference = value;
    }

    private HashMap<String, BigDecimal> getItems() {
        return items;
    }

    /**
     * Get the total amount of the items in the cart for the transaction
     */
    public final BigDecimal getTotal() {
        return calculateTotal();
    }

    /**
     * Adds a new item to the transaction
     *
     * @param title  The name of the item
     * @param amount The cost of the item
     */
    public final Payment add(String title, double amount) {
        getItems().put(title, new BigDecimal(amount));

        return this;
    }

    /**
     * Adds a new item to the cart for the transaction
     *
     * @param title  The name of the item
     * @param amount The cost of the item
     */
    public final Payment add(String title, int amount) {
        getItems().put(title, new BigDecimal(amount));

        return this;
    }

    /**
     * Adds a new item to the cart for the transaction
     *
     * @param title  The name of the item
     * @param amount The cost of the item
     */
    public final Payment add(String title, BigDecimal amount) {
        getItems().put(title, amount);

        return this;
    }

    /**
     * Removes an item from the transaction
     *
     * @param title The name of the item
     */
    public final Payment remove(String title) {
        HashMap<String, BigDecimal> items = getItems();

        if (items.containsKey(title)) {
            getItems().remove(title);
        }

        return this;
    }

    /**
     * Get the string representation of the items in the cart for the transaction
     */
    public final String itemsDescription() {
        if(this.override) {
            return this.defaultDescription;
        }

        return Utils.flattenCollection(getItems()).trim();
    }

    /**
     * Calculate the total amount of the items in the cart for the transaction
     */
    private BigDecimal calculateTotal() {
        return Utils.addCollectionValues(getItems());
    }

    /**
     * Sets the default description of the transaction cart
     *
     * @param description The description of the transaction cart
     */
    public void setDefaultDescription(String description)
    {
        this.defaultDescription = description;
        this.override = true;
    }

    /**
     * Get the items in the transaction cart as a dictionary
     *
     * @return Hash map of items in the cart as a dictionary
     */
    public final HashMap<String, String> toDictionary() {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("resulturl", "");
        map.put("returnurl", "");
        map.put("reference", getReference());
        map.put("amount", getTotal().toString());
        map.put("id", "");
        map.put("additionalinfo", itemsDescription());
        map.put("authemail", authEmail);
        map.put("status", "Message");

        return map;
    }
}
