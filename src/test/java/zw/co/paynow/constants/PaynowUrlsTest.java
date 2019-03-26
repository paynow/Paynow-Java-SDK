package zw.co.paynow.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaynowUrlsTest {

    @Test
    public void PaynowUrls_CurrentConstantValuesSet_StringsHaveCorrectValues() {
        assertEquals("https://www.paynow.co.zw/interface/initiatetransaction", PaynowUrls.INITIATE_TRANSACTION);
        assertEquals("https://www.paynow.co.zw/interface/remotetransaction", PaynowUrls.INITIATE_MOBILE_TRANSACTION);
    }

}