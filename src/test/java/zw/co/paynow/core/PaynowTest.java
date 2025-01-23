package zw.co.paynow.core;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import zw.co.paynow.exceptions.ConnectionException;
import zw.co.paynow.exceptions.HashMismatchException;
import zw.co.paynow.http.HttpClient;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaynowTest {

    private static String integrationKey;
    private static String integrationId;
    private static String resultUrl;
    private static Paynow paynow;

    @Mock
    private HttpClient httpClient;

    @BeforeClass
    public static void setup() {
        integrationKey = "0123456789";
        integrationId = "9876543210";
        resultUrl = "www.example.org/resultUrl";
    }

    @Before
    public void init() {
        paynow = new Paynow(integrationId, integrationKey, resultUrl);
        paynow.setHttpHttpClient(httpClient);
    }

    @Test
    public void PaynowConstructors_ExpectedParams_PaynowObjectWithCorrectFieldValues() {

        Paynow paynow1 = new Paynow(integrationId, integrationKey);
        assertThat(paynow1, not(nullValue()));
        assertEquals(integrationId, paynow1.getIntegrationId());
        assertEquals(integrationKey, paynow1.getIntegrationKey());
        assertEquals("http://localhost", paynow1.getResultUrl());
        assertThat(paynow1.getHttpHttpClient(), not(nullValue()));

        Paynow paynow2 = new Paynow(integrationId, integrationKey, resultUrl);
        assertThat(paynow2, not(nullValue()));
        assertEquals(integrationId, paynow2.getIntegrationId());
        assertEquals(integrationKey, paynow2.getIntegrationKey());
        assertEquals(resultUrl, paynow2.getResultUrl());
        assertThat(paynow2.getHttpHttpClient(), not(nullValue()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void PaynowConstructorWithTwoParams_NoIntegrationId_ExceptionThrown() {
        new Paynow("", "lorem_ipsum");
    }

    @Test(expected = IllegalArgumentException.class)
    public void PaynowConstructorTwoParam_NoIntegrationKey_ExceptionThrown() {
        new Paynow("lorem_ipsum", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void PaynowConstructorThreeParam_NoIntegrationId_ExceptionThrown() {
        new Paynow("lorem_ipsum", "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void PaynowConstructorThreeParam_NoIntegrationKey_ExceptionThrown() {
        new Paynow("", "lorem_ipsum", "");
    }

    @Test(expected = HashMismatchException.class)
    public void PollTransaction_ResponseStringWithBadHash_ResponseIsReturnedWithCorrectValues() throws IOException {

        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=WrongHash";
        String pollUrl = "www.dummypollurl.co.zw";

        when(httpClient.postAsync(pollUrl, null)).thenReturn(rawResponse);

        paynow.pollTransaction(pollUrl);
    }

    @Test(expected = HashMismatchException.class)
    public void PollTransaction_ResponseStringWithNoHash_ResponseIsReturnedWithCorrectValues() throws IOException {

        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68";
        String pollUrl = "www.dummypollurl.co.zw";

        when(httpClient.postAsync(pollUrl, null)).thenReturn(rawResponse);

        paynow.pollTransaction(pollUrl);
    }

    @Test(expected = ConnectionException.class)
    public void PollTransaction_ExceptionOccursDuringExecution_ExceptionIsCaughtAndCustomExceptionIsThrown() throws IOException {

        String pollUrl = "www.dummypollurl.co.zw";

        when(httpClient.postAsync(pollUrl, null)).thenThrow(new IOException());

        paynow.pollTransaction(pollUrl);

    }

}
