package zw.co.paynow.validators;

import java.util.regex.Pattern;

public class EmailValidator {

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

}
