package zw.co.paynow.responses;

import org.junit.Test;
import zw.co.paynow.constants.TransactionStatus;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StatusResponseTest {

    @Test
    public void StatusResponseConstructor_SampleMapAsParam_InstantiatedObjectWithCorrectValues() {

        HashMap<String, String> sampleMap = new HashMap<>();
        sampleMap.put("reference", "a1");
        sampleMap.put("paynowreference", "b2");
        sampleMap.put("amount", "2.2");
        sampleMap.put("hash", "DB191C4CDE56138E11A398CA6AB1E1FC5DEEA870FFDDE4557F2A8994EFF8BA284C5318BC2213AA65449B5565AC6FC6A164554E5463EDF0164D149A0E0D24AB8E");
        sampleMap.put("status", "Sent");

        StatusResponse response = new StatusResponse(sampleMap);
        assertEquals(TransactionStatus.SENT, response.getStatus());
        assertTrue(response.success());
        assertEquals(0, response.getErrors().size());
        assertEquals(new BigDecimal(sampleMap.get("amount")), response.getAmount());
        assertEquals(sampleMap.get("hash"), response.hash());
        assertEquals(sampleMap.get("reference"), response.getMerchantReference());
        assertEquals(sampleMap.get("paynowreference"), response.getPaynowReference());

    }

}