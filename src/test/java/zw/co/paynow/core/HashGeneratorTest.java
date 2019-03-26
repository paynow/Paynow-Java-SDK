package zw.co.paynow.core;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.*;

public class HashGeneratorTest {

    private static String dummyText;
    private static Map<String, String> dummyValues = new HashMap<>();
    private static String integrationKey;

    @BeforeClass
    public static void setup() {
        dummyText = "0123456789";
        integrationKey = "0123456789";

        dummyValues.put("item1", "value1");
        dummyValues.put("item2", "value2");
        dummyValues.put("item3", "value3");
    }

    @Test
    public void MakeMethod_SampleIntegrationKeyAndMap_GeneratedHashIsNotNullOrEmpty() {
        String integrationKey = "0123456789";
        String generatedHash = HashGenerator.make(dummyValues, integrationKey);
        assertThat(generatedHash, not(isEmptyOrNullString()));
    }

    @Test
    public void GenerateHashMethod_SampleText_GeneratedHashIsNotNullOrEmpty() {
        String generatedHash = HashGenerator.generateHash(dummyText);
        assertThat(generatedHash, not(isEmptyOrNullString()));
    }

    @Test
    public void GenerateHashMethod_SampleText_GeneratedHashIsValidSHA512Hash() {
        String generatedHash = HashGenerator.generateHash(dummyText);
        assertEquals(128, generatedHash.length());
    }

    @Test
    public void GenerateHashMethod_SampleText_GeneratedHashIsCorrectHash() {
        String generatedHash = HashGenerator.generateHash(dummyText);
        assertEquals("BB96C2FC40D2D54617D6F276FEBE571F623A8DADF0B734855299B0E107FDA32CF6B69F2DA32B36445D73690B93CBD0F7BFC20E0F7F28553D2A4428F23B716E90", generatedHash);
    }

    @Test
    public void VerifyMethod_SampleMap_VerificationReturnsCorrectResult() {
        String correctHashUsingDummyValuesAndKey = "D7B2E3E63D1DE252C3DDC16546EA4DE6FC5DA5CB7DA3CA27FA982BC6F1B4972934AF583FE4EE7BDD59378BC0DB1A236D0D66194A90B5BB015EE5DA60FEAB2A1C";
        dummyValues.put("hash", correctHashUsingDummyValuesAndKey);

        boolean result = HashGenerator.verify(dummyValues, integrationKey);
        assertTrue(result);
    }

    @Test
    public void VerifyMethod_SampleMap_VerificationReturnsFalseResult() {
        String wrongHashUsingDummyValuesAndKey = "D7B2E3E63D1DE25FC3DDC16546EA4DE6FC5DA5PB7DA3CA27FA982BC6F1B4972934AF583FE4EE7BDD59378BC0DB1A236D0D66194A90B5BB015EE5DA60FEAB2A1C";
        dummyValues.put("hash", wrongHashUsingDummyValuesAndKey);

        boolean result = HashGenerator.verify(dummyValues, integrationKey);
        assertFalse(result);
    }

}