package zw.co.paynow.core;

import zw.co.paynow.exceptions.HashGenerationFailedException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

/**
 * This class handles generating hashes for transactions being
 * sent to Paynow
 */
public final class HashGenerator {

    /**
     * HashGenerator the values in the given Map
     *
     * @param values         Values to hash
     * @param integrationKey Paynow integration key
     * @return The generated hash
     */
    public static String make(Map<String, String> values, String integrationKey) {
        String str = concat(values).concat(integrationKey);

        return generateHash(str);
    }

    /**
     * Generate a SHA512 hash of the given string
     *
     * @param input The string to hash
     * @return The generated SHA512 hash of the input string
     */
    public static String generateHash(String input) {
        String hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            digest.reset();
            digest.update(input.getBytes(StandardCharsets.UTF_8));

            hash = String.format("%040x", new BigInteger(1, digest.digest()));

            return hash.toUpperCase();

        } catch (Exception e) {
            throw new HashGenerationFailedException();
        }

    }


    /**
     * Concatenates the values to be sent to Paynow
     *
     * @param items The values to concatenate
     * @return The concatenated string values
     */
    private static String concat(Map<String, String> items) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> pair : items.entrySet()) {
            if (pair.getKey().equalsIgnoreCase("hash")) continue;

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
    public static boolean verify(Map<String, String> data, String integrationKey) {
        return make(data, integrationKey).equalsIgnoreCase(data.get("hash"));
    }

}