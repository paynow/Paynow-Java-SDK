package zw.co.paynow.parsers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Decode/encode various parts of the URL
 */
public class UrlParser {

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
            query_pairs.put(UrlParser.urlDecode(pair.substring(0, idx)), UrlParser.urlDecode(pair.substring(idx + 1)));
        }
        return query_pairs;
    }

}
