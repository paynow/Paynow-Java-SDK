package zw.co.paynow.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

@DisplayName("Mobile Money Method Test")
class MobileMoneyMethodTest {

    @Test
    @DisplayName("Mobile Money Method ToString Current Constant Values Set Status ToString Has Correct Value")
    public void MobileMoneyMethodToString_CurrentConstantValuesSet_StatusToStringHasCorrectValue() {
        assertEquals("ECOCASH", MobileMoneyMethod.ECOCASH.toString());
        assertEquals("TELECASH", MobileMoneyMethod.TELECASH.toString());
        assertEquals("ONEMONEY", MobileMoneyMethod.ONEMONEY.toString());
        assertEquals("INNBUCKS", MobileMoneyMethod.INNBUCKS.toString());
    }
}
