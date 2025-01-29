package zw.co.paynow.parsers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Url Parser Test")
class UrlParserTest {

    @Test
    @DisplayName("Url Encode Sample String As Param Correct Url Encoded String")
    void UrlEncode_SampleStringAsParam_CorrectUrlEncodedString() {
        String sampleUnencodedUrl = "https://www.google.com/search?q=searching+some+info";
        String expectedEncodedUrl = "https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Dsearching%2Bsome%2Binfo";
        String result = UrlParser.urlEncode(sampleUnencodedUrl);
        assertThat(result).isEqualTo(expectedEncodedUrl);
    }

    @Test
    @DisplayName("Url Decode Sample String As Param Correct Url Decoded String")
    void UrlDecode_SampleStringAsParam_CorrectUrlDecodedString() {
        String sampleEncodedUrl = "https%3A%2F%2Fwww.google.com%2Fsearch%3Fq%3Dsearching%2Bsome%2Binfo";
        String expectedDecodedUrl = "https://www.google.com/search?q=searching+some+info";
        String result = UrlParser.urlDecode(sampleEncodedUrl);
        assertThat(result).isEqualTo(expectedDecodedUrl);
    }

    @Test
    @DisplayName("Url Encode Sample Map As Param Correct Url Encoded String")
    void UrlEncode_SampleMapAsParam_CorrectUrlEncodedString() {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("1", "one");
        hashMap.put("2", "two");
        hashMap.put("3", "three");

        String expectedString = "1=one&2=two&3=three";
        String result = UrlParser.parseQueryStringFromMap(hashMap);
        assertThat(result).isEqualTo(expectedString);

    }

    @Test
    @DisplayName("Url Encode Empty Map As Param Empty String")
    void UrlEncode_EmptyMapAsParam_EmptyString() {
        HashMap<String, String> hashMap = new HashMap();

        String expectedString = "";
        String result = UrlParser.parseQueryStringFromMap(hashMap);
        assertThat(result).isEqualTo(expectedString);

    }

    @Test
    @DisplayName("Parse Query String Sample Query String Map With Correct Values")
    void ParseQueryString_SampleQueryString_MapWithCorrectValues() {
        String sampleQueryString = "1=one&2=two&3=three";

        HashMap<String, String> expectedHashMap = new HashMap<>();
        expectedHashMap.put("1", "one");
        expectedHashMap.put("2", "two");
        expectedHashMap.put("3", "three");

        HashMap<String, String> result = UrlParser.parseMapFromQueryString(sampleQueryString);
        assertThat(result).isEqualTo(expectedHashMap);

    }

    @Test
    @DisplayName("Parse Query String Empty Query String Empty Map")
    void ParseQueryString_EmptyQueryString_EmptyMap() {
        String sampleQueryString = "";

        HashMap<String, String> expectedHashMap = new HashMap<>();

        HashMap<String, String> result = UrlParser.parseMapFromQueryString(sampleQueryString);
        assertThat(result).isEqualTo(expectedHashMap);

    }

}