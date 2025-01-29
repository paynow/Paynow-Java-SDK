package zw.co.paynow.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Transaction Type Test")
class TransactionTypeTest {

    @Test
    @DisplayName("To String Current Constant Values Set To String Has Correct Value")
    void ToString_CurrentConstantValuesSet_ToStringHasCorrectValue() {
        assertThat(TransactionType.WEB.toString()).hasToString("WEB");
        assertThat(TransactionType.MOBILE.toString()).hasToString("MOBILE");
    }
}
