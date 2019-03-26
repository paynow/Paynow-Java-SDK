package zw.co.paynow.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionTypeTest {

    @Test
    public void ToString_CurrentConstantValuesSet_ToStringHasCorrectValue() {
        assertEquals("WEB", TransactionType.WEB.toString());
        assertEquals("MOBILE", TransactionType.MOBILE.toString());
    }

}