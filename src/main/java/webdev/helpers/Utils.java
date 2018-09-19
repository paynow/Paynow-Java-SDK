package webdev.helpers;

import java.io.UnsupportedEncodingException;
import webdev.payments.MobileMoneyMethod;
import webdev.core.Constants;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public final class Utils {
    /**
     * Url encode the given string
     *
     * @param s The string to url encode
     * @return The encoded string
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * Format a map as a query string
     *
     * @param map The map to format
     * @return Formatted query string
     */
    public static String urlEncode(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncode(entry.getKey().toString()),
                    urlEncode(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    public static String FlattenCollection(HashMap<String, BigDecimal> items) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, BigDecimal> pair : items.entrySet()) {
            sb.append(pair.getKey());
            sb.append(", ");
        }

        return sb.toString();
    }

    /**
     * Compute the total of the values in a Payment object
     *
     * @param items The collection of values
     * @return The total of the items
     */
    public static BigDecimal AddCollectionValues(HashMap<String, BigDecimal> items) {
        BigDecimal number = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> pair : items.entrySet()) {
            number = number.add(pair.getValue());
        }

        return number;
    }

    public static HashMap<String, String> ParseQueryString(String qs) {
        // TODO: Implement

        return new HashMap<String, String>();
    }

    public static String GetString(MobileMoneyMethod method) {
        switch (method) {
            case Ecocash:
                return Constants.MobileMoneyMethodEcocash;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}