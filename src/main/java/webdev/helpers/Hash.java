package webdev.helpers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;

/**
 * This class handles generating hashes for transactions being
 * sent to Paynow
 */
public final class Hash {
    /**
     * Hash the values in the given Map
     *
     * @param values         Values to hash
     * @param integrationKey Paynow integration key
     * @return The hash of the files
     */
    public static String make(Map<String, String> values, String integrationKey) {
        return generateHash(
                Concat(values).concat(integrationKey)
        );
    }

    /**
     * Generate a SHA512 hash of the given string
     *
     * @param input The string to hash
     * @return The SHA512 hash of the input string
     */
    private static String generateHash(String input) {

        String hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            digest.reset();
            digest.update(input.getBytes("utf8"));

            hash = String.format("%040x", new BigInteger(1, digest.digest()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }


    /**
     * Concatenates the values to be sent to Paynow
     *
     * @param items The values to concatenate
     * @return The concatenated string values
     */
    private static String Concat(Map<String, String> items) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> pair : items.entrySet()) {
            if (pair.getKey().toLowerCase().equals("hash")) continue;

            sb.append(pair.getValue());
        }

        return sb.toString();
    }

    /**
     * Verifies the hash from Paynow with the locally generated one
     *
     * @param data           The data from Paynow
     * @param integrationKey Integration key to use during hashing
     * @return Boolean value indicating whether hashes match or not. True for match, false for mismatch
     */
    public static boolean Verify(Map<String, String> data, String integrationKey) {
        return make(data, integrationKey).equals(data.get("hash"));
    }
}