package zw.co.paynow.responses;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zw.co.paynow.constants.TransactionStatus;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Mobile Init Response Test")
class MobileInitResponseTest {

    @Test
    @DisplayName("Mobile Init Response Constructor Sample Map As Param Instantiated Object With Correct Values")
    void MobileInitResponseConstructor_SampleMapAsParam_InstantiatedObjectWithCorrectValues() {
        HashMap<String, String> sampleMap = new HashMap<>();
        sampleMap.put("paynowreference", "0123456");
        sampleMap.put("instructions", "Some_random_instructions");
        sampleMap.put("pollurl", "https://www.paynow.co.zw/Interface/CheckPayment/?guid=09471727-37a6-4a0c-a7f3-c1ac48f79431");
        sampleMap.put("hash", "DB191C4CDE56138E11A398CA6AB1E1FC5DEEA870FFDDE4557F2A8994EFF8BA284C5318BC2213AA65449B5565AC6FC6A164554E5463EDF0164D149A0E0D24AB8E");
        sampleMap.put("status", "Ok");

        MobileInitResponse response = new MobileInitResponse(sampleMap);
        assertThat(response.getStatus()).isEqualTo(TransactionStatus.OK);
        assertThat(response.success()).isTrue();
        assertThat(response.getErrors().size()).isZero();
        assertThat(response.getPaynowReference()).isEqualTo("0123456");
        assertThat(response.getHash()).isEqualTo(sampleMap.get("hash"));
        assertThat(response.getInstructions()).isEqualTo(sampleMap.get("instructions"));
        assertThat(response.getPollUrl()).isEqualTo(sampleMap.get("pollurl"));

    }

}