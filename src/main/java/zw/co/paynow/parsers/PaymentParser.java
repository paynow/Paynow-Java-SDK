package zw.co.paynow.parsers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to parse various 'Payment' related fields
 */
public class PaymentParser {

    public static String flattenCollection(HashMap<String, BigDecimal> items) {
        StringBuilder sb = new StringBuilder();

        int i = 1;
        for (Map.Entry<String, BigDecimal> pair : items.entrySet()) {
            sb.append(pair.getKey());

            //Don't put comma at last item in cart
            if (!(i == items.size())) {
                sb.append(", ");
            }
            i++;
        }

        return sb.toString();
    }

    /**
     * Compute the total of the values in a Payment object
     *
     * @param items The collection of values
     * @return The total of the items
     */
    public static BigDecimal addCollectionValues(HashMap<String, BigDecimal> items) {
        BigDecimal number = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> pair : items.entrySet()) {
            number = number.add(pair.getValue());
        }

        return number;
    }

}
