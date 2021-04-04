package zw.co.paynow.responses;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zw.co.paynow.constants.TransactionStatus;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Status Response Test")
class StatusResponseTest {

    @Test
    @DisplayName("Status Response Constructor Sample Map As Param Instantiated Object With Correct Values")
    void StatusResponseConstructor_SampleMapAsParam_InstantiatedObjectWithCorrectValues() {
        HashMap<String, String> sampleMap = new HashMap<>();
        sampleMap.put("reference", "a1");
        sampleMap.put("paynowreference", "b2");
        sampleMap.put("amount", "2.2");
        sampleMap.put("hash", "DB191C4CDE56138E11A398CA6AB1E1FC5DEEA870FFDDE4557F2A8994EFF8BA284C5318BC2213AA65449B5565AC6FC6A164554E5463EDF0164D149A0E0D24AB8E");
        sampleMap.put("status", "Sent");

        StatusResponse response = new StatusResponse(sampleMap);
        assertThat(response.getStatus()).isEqualTo(TransactionStatus.SENT);
        assertThat(response.success()).isTrue();
        assertThat(response.getErrors().size()).isZero();
        assertThat(response.getAmount()).isEqualTo(new BigDecimal(sampleMap.get("amount")));
        assertThat(response.hash()).isEqualTo(sampleMap.get("hash"));
        assertThat(response.getMerchantReference()).isEqualTo(sampleMap.get("reference"));
        assertThat(response.getPaynowReference()).isEqualTo(sampleMap.get("paynowreference"));

    }

}