package zw.co.paynow.helpers;

import java.io.UnsupportedEncodingException;

import zw.co.paynow.payments.MobileMoneyMethod;
import zw.co.paynow.core.Constants;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class Utils {

    public static BigDecimal b(double value) {
        return new BigDecimal(value);
    }

    public static BigDecimal b(int value) {
        return new BigDecimal(value);
    }

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


    public static String flattenCollection(HashMap<String, BigDecimal> items) {
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
    public static BigDecimal addCollectionValues(HashMap<String, BigDecimal> items) {
        BigDecimal number = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> pair : items.entrySet()) {
            number = number.add(pair.getValue());
        }

        return number;
    }

    /**
     * Url decode the given string
     *
     * @param s The string to url decode
     * @return The decoded string
     */
    public static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    /**
     * Validate an email string
     *
     * @param email The string to validate as an email
     * @return Whether the string is a valid email i.e. true if valid
     */
    public static boolean validateEmail(String email) {
        return Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        )
                .matcher(email).matches();
    }


    /**
     * Parse a query string from a URL
     *
     * @param qs Query string to parse
     * @return Map of parsed values i.e. key and value
     */
    public static LinkedHashMap<String, String> parseQueryString(String qs) {
        LinkedHashMap<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = qs.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(urlDecode(pair.substring(0, idx)), urlDecode(pair.substring(idx + 1)));
        }
        return query_pairs;
    }

}