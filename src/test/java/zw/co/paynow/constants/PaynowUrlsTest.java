package zw.co.paynow.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Paynow Urls Test")
class PaynowUrlsTest {

    @Test
    @DisplayName("Paynow Urls Current Constant Values Set Strings Have Correct Values")
    void PaynowUrls_CurrentConstantValuesSet_StringsHaveCorrectValues() {
        assertThat(PaynowUrls.INITIATE_TRANSACTION).isEqualTo("https://www.paynow.co.zw/interface/initiatetransaction");
        assertThat(PaynowUrls.INITIATE_MOBILE_TRANSACTION).isEqualTo("https://www.paynow.co.zw/interface/remotetransaction");
    }
}
