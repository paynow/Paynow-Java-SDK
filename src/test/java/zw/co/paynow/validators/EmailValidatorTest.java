package zw.co.paynow.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Email Validator Test")
class EmailValidatorTest {

    @Test
    @DisplayName("Status Response Constructor Valid Email Instantiated Object With Correct Values")
    void StatusResponseConstructor_ValidEmail_InstantiatedObjectWithCorrectValues() {
        String email = "example@example.org";
        boolean result = EmailValidator.validateEmail(email);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Status Response Constructor Invalid Email Instantiated Object With Correct Values")
    void StatusResponseConstructor_InvalidEmail_InstantiatedObjectWithCorrectValues() {
        String badEmail1 = "example";
        String badEmail2 = "example.org";
        String badEmail3 = "example@org";
        boolean result1 = EmailValidator.validateEmail(badEmail1);
        boolean result2 = EmailValidator.validateEmail(badEmail2);
        boolean result3 = EmailValidator.validateEmail(badEmail3);
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
    }

}