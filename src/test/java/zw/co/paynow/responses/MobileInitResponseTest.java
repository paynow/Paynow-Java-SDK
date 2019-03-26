package zw.co.paynow.responses;

import org.junit.Test;
import zw.co.paynow.constants.TransactionStatus;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MobileInitResponseTest {

    @Test
    public void MobileInitResponseConstructor_SampleMapAsParam_InstantiatedObjectWithCorrectValues() {

        HashMap<String, String> sampleMap = new HashMap<>();
        sampleMap.put("paynowreference", "0123456");
        sampleMap.put("instructions", "Some_random_instructions");
        sampleMap.put("pollurl", "https://www.paynow.co.zw/Interface/CheckPayment/?guid=09471727-37a6-4a0c-a7f3-c1ac48f79431");
        sampleMap.put("hash", "DB191C4CDE56138E11A398CA6AB1E1FC5DEEA870FFDDE4557F2A8994EFF8BA284C5318BC2213AA65449B5565AC6FC6A164554E5463EDF0164D149A0E0D24AB8E");
        sampleMap.put("status", "Ok");

        MobileInitResponse response = new MobileInitResponse(sampleMap);
        assertEquals(TransactionStatus.OK, response.getStatus());
        assertTrue(response.success());
        assertEquals(0, response.getErrors().size());
        assertEquals("0123456", response.getPaynowReference());
        assertEquals(sampleMap.get("hash"), response.getHash());
        assertEquals(sampleMap.get("instructions"), response.getInstructions());
        assertEquals(sampleMap.get("pollurl"), response.getPollUrl());

    }

}