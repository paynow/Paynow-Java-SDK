package zw.co.paynow.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import zw.co.paynow.exceptions.ConnectionException;
import zw.co.paynow.exceptions.HashMismatchException;
import zw.co.paynow.http.HttpClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Paynow Test")
class PaynowTest {

    private static String integrationKey;

    private static String integrationId;

    private static String resultUrl;

    private static Paynow paynow;

    @Mock
    private HttpClient httpClient;

    @BeforeAll
    static void setup() {
        integrationKey = "0123456789";
        integrationId = "9876543210";
        resultUrl = "www.example.org/resultUrl";
    }

    @BeforeEach
    void init() {
        paynow = new Paynow(integrationId, integrationKey, resultUrl);
        paynow.setHttpHttpClient(httpClient);
    }

    @Test
    @DisplayName("Paynow Constructors Expected Params Paynow Object With Correct Field Values")
    void PaynowConstructors_ExpectedParams_PaynowObjectWithCorrectFieldValues() {
        Paynow paynow1 = new Paynow(integrationId, integrationKey);
        assertThat(paynow1).isNotNull();
        assertThat(paynow1.getIntegrationId()).isEqualTo(integrationId);
        assertThat(paynow1.getIntegrationKey()).isEqualTo(integrationKey);
        assertThat(paynow1.getResultUrl()).isEqualTo("http://localhost");
        assertThat(paynow1.getHttpHttpClient()).isNotNull();

        Paynow paynow2 = new Paynow(integrationId, integrationKey, resultUrl);
        assertThat(paynow2).isNotNull();
        assertThat(paynow2.getIntegrationId()).isEqualTo(integrationId);
        assertThat(paynow2.getIntegrationKey()).isEqualTo(integrationKey);
        assertThat(paynow2.getResultUrl()).isEqualTo(resultUrl);
        assertThat(paynow2.getHttpHttpClient()).isNotNull();

    }

    @Test
    @DisplayName("Paynow Constructor With Two Params No Integration Id Exception Thrown")
    void PaynowConstructorWithTwoParams_NoIntegrationId_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Paynow("", "lorem_ipsum"));
    }

    @Test
    @DisplayName("Paynow Constructor Two Param No Integration Key Exception Thrown")
    void PaynowConstructorTwoParam_NoIntegrationKey_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Paynow("lorem_ipsum", ""));
    }

    @Test
    @DisplayName("Paynow Constructor Three Param No Integration Id Exception Thrown")
    void PaynowConstructorThreeParam_NoIntegrationId_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Paynow("lorem_ipsum", "", ""));
    }

    @Test
    @DisplayName("Paynow Constructor Three Param No Integration Key Exception Thrown")
    void PaynowConstructorThreeParam_NoIntegrationKey_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Paynow("", "lorem_ipsum", ""));
    }

    @Test
    @DisplayName("Poll Transaction Response String With Bad Hash Response Is Returned With Correct Values")
    void PollTransaction_ResponseStringWithBadHash_ResponseIsReturnedWithCorrectValues() throws Exception {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=WrongHash";
        String pollUrl = "www.dummypollurl.co.zw";
        when(httpClient.postAsync(pollUrl, null)).thenReturn(rawResponse);

        assertThrows(HashMismatchException.class, () -> paynow.pollTransaction(pollUrl));
    }

    @Test
    @DisplayName("Poll Transaction Response String With No Hash Response Is Returned With Correct Values")
    void PollTransaction_ResponseStringWithNoHash_ResponseIsReturnedWithCorrectValues() throws Exception {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68";
        String pollUrl = "www.dummypollurl.co.zw";
        when(httpClient.postAsync(pollUrl, null)).thenReturn(rawResponse);

        assertThrows(HashMismatchException.class, () -> paynow.pollTransaction(pollUrl));
    }

    @Test
    @DisplayName("Poll Transaction Exception Occurs During Execution Exception Is Caught And Custom Exception Is Thrown")
    void PollTransaction_ExceptionOccursDuringExecution_ExceptionIsCaughtAndCustomExceptionIsThrown() throws Exception {
        String pollUrl = "www.dummypollurl.co.zw";
        when(httpClient.postAsync(pollUrl, null)).thenThrow(new IOException());

        assertThrows(ConnectionException.class, () -> paynow.pollTransaction(pollUrl));
    }

}
