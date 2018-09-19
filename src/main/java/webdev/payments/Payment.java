package webdev.payments;

import webdev.helpers.Utils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Represents a single transaction to be sent to Paynow
 */
public class Payment {
    /**
     * Email to be sent to Paynow with the transaction
     */
    public String AuthEmail = "";
    /**
     * This is the reference for the transaction (like an id in the database)
     */
    private String Reference;
    /**
     * List of the items in the transaction
     */
    private HashMap<String, BigDecimal> Items;

    public Payment(String reference, String authEmail) {
        setReference(reference);
        Items = new HashMap<String, BigDecimal>();
        AuthEmail = authEmail;
    }

    public Payment(String reference, HashMap<String, BigDecimal> values, String authEmail) {
        setReference(reference);
        Items = values;
        AuthEmail = authEmail;
    }

    public final String getReference() {
        return Reference;
    }

    public final void setReference(String value) {
        Reference = value;
    }

    private HashMap<String, BigDecimal> getItems() {
        return Items;
    }

    /**
     * Get the total of the items in the transaction
     */
    public final BigDecimal getTotal() {
        return GetTotal();
    }

    /**
     * Add a new item to the transaction
     *
     * @param title  The name of the item
     * @param amount The cost of the item
     */
    public final Payment Add(String title, BigDecimal amount) {
        getItems().put(title, amount);

        return this;
    }

    /**
     * Remove an item from the transaction
     *
     * @param title
     */
    public final Payment Remove(String title) {
        HashMap<String, BigDecimal> items = getItems();

        if (items.containsKey(title)) {
            getItems().remove(title);
        }

        return this;
    }

    /**
     * Get the string representation of the items in the transaction
     */
    public final String ItemsDescription() {
        return Utils.FlattenCollection(getItems()).trim();
    }

    /**
     * Get the total cost of the items in the transaction
     */
    private BigDecimal GetTotal() {
        return Utils.AddCollectionValues(getItems());
    }

    /**
     * Get the items in the transaction as a dictionary
     *
     * @return
     */
    public final HashMap<String, String> ToDictionary() {
        HashMap<String, String> map = new HashMap<String, String>(
//        Map.ofEntries(
//                Map.entry("resulturl", ""),
//                Map.entry("returnurl", ""),
//                Map.entry("reference", getReference()),
//                Map.entry("amount", getTotal().toString()),
//                Map.entry("id", ""),
//                Map.entry("additionalinfo", ItemsDescription()),
//                Map.entry("authemail", AuthEmail),
//                Map.entry("status", "Message")
//        )
        );

        map.put("resulturl", "");
        map.put("returnurl", "");
        map.put("reference", getReference());
        map.put("amount", getTotal().toString());
        map.put("id", "");
        map.put("additionalinfo", ItemsDescription());
        map.put("authemail", AuthEmail);
        map.put("status", "Message");

        //TODO: Fix things

        return map;
    }
}