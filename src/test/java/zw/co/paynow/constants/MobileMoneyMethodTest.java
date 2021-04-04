package zw.co.paynow.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mobile Money Method Test")
class MobileMoneyMethodTest {

    @Test
    @DisplayName("Mobile Money Method ToString Current Constant Values Set Status ToString Has Correct Value")
    void MobileMoneyMethodToString_CurrentConstantValuesSet_StatusToStringHasCorrectValue() {
        assertThat(MobileMoneyMethod.ECOCASH.toString()).hasToString("ECOCASH");
        assertThat(MobileMoneyMethod.TELECASH.toString()).hasToString("TELECASH");
        assertThat(MobileMoneyMethod.ONEMONEY.toString()).hasToString("ONEMONEY");
    }
}
