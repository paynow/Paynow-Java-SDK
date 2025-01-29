package zw.co.paynow.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MobileMoneyMethodTest {

    @Test
    public void MobileMoneyMethodToString_CurrentConstantValuesSet_StatusToStringHasCorrectValue() {
        assertEquals("ECOCASH", MobileMoneyMethod.ECOCASH.toString());
        assertEquals("TELECASH", MobileMoneyMethod.TELECASH.toString());
        assertEquals("ONEMONEY", MobileMoneyMethod.ONEMONEY.toString());
        assertEquals("INNBUCKS", MobileMoneyMethod.INNBUCKS.toString());
    }

}