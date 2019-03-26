package zw.co.paynow.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmailValidatorTest {

    @Test
    public void StatusResponseConstructor_ValidEmail_InstantiatedObjectWithCorrectValues() {
        String email = "example@example.org";
        boolean result = EmailValidator.validateEmail(email);
        assertTrue(result);
    }

    @Test
    public void StatusResponseConstructor_InvalidEmail_InstantiatedObjectWithCorrectValues() {
        String badEmail1 = "example";
        String badEmail2 = "example.org";
        String badEmail3 = "example@org";
        boolean result1 = EmailValidator.validateEmail(badEmail1);
        boolean result2 = EmailValidator.validateEmail(badEmail2);
        boolean result3 = EmailValidator.validateEmail(badEmail3);
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

}