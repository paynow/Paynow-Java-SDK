package zw.co.paynow.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Hash Generator Test")
class HashGeneratorTest {

    private static String dummyText;
    private static final Map<String, String> dummyValues = new HashMap<>();
    private static String integrationKey;

    @BeforeAll
    static void setup() {
        dummyText = "0123456789";
        integrationKey = "0123456789";
        dummyValues.put("item1", "value1");
        dummyValues.put("item2", "value2");
        dummyValues.put("item3", "value3");
    }

    @Test
    @DisplayName("Make Method Sample Integration Key And Map Generated Hash Is Not Null Or Empty")
    void MakeMethod_SampleIntegrationKeyAndMap_GeneratedHashIsNotNullOrEmpty() {
        String integrationKey = "0123456789";
        String generatedHash = HashGenerator.make(dummyValues, integrationKey);
        assertThat(generatedHash).isNotEmpty();
    }

    @Test
    @DisplayName("Generate Hash Method Sample Text Generated Hash Is Not Null Or Empty")
    void GenerateHashMethod_SampleText_GeneratedHashIsNotNullOrEmpty() {
        String generatedHash = HashGenerator.generateHash(dummyText);
        assertThat(generatedHash).isNotEmpty();
    }

    @Test
    @DisplayName("Generate Hash Method Sample Text Generated Hash Is Valid SHA 512 Hash")
    void GenerateHashMethod_SampleText_GeneratedHashIsValidSHA512Hash() {
        String generatedHash = HashGenerator.generateHash(dummyText);
        assertThat(generatedHash).hasSize(128);
    }

    @Test
    @DisplayName("Generate Hash Method Sample Text Generated Hash Is Correct Hash")
    void GenerateHashMethod_SampleText_GeneratedHashIsCorrectHash() {
        String generatedHash = HashGenerator.generateHash(dummyText);
        assertThat(generatedHash).isEqualTo("BB96C2FC40D2D54617D6F276FEBE571F623A8DADF0B734855299B0E107FDA32CF6B69F2DA32B36445D73690B93CBD0F7BFC20E0F7F28553D2A4428F23B716E90");
    }

    @Test
    @DisplayName("Verify Method Sample Map Verification Returns Correct Result")
    void VerifyMethod_SampleMap_VerificationReturnsCorrectResult() {
        String correctHashUsingDummyValuesAndKey = "D7B2E3E63D1DE252C3DDC16546EA4DE6FC5DA5CB7DA3CA27FA982BC6F1B4972934AF583FE4EE7BDD59378BC0DB1A236D0D66194A90B5BB015EE5DA60FEAB2A1C";
        dummyValues.put("hash", correctHashUsingDummyValuesAndKey);

        boolean result = HashGenerator.verify(dummyValues, integrationKey);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Verify Method Sample Map Verification Returns False Result")
    void VerifyMethod_SampleMap_VerificationReturnsFalseResult() {
        String wrongHashUsingDummyValuesAndKey = "D7B2E3E63D1DE25FC3DDC16546EA4DE6FC5DA5PB7DA3CA27FA982BC6F1B4972934AF583FE4EE7BDD59378BC0DB1A236D0D66194A90B5BB015EE5DA60FEAB2A1C";
        dummyValues.put("hash", wrongHashUsingDummyValuesAndKey);

        boolean result = HashGenerator.verify(dummyValues, integrationKey);
        assertThat(result).isFalse();
    }

}
