package zw.co.paynow.core;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import zw.co.paynow.constants.MobileMoneyMethod;
import zw.co.paynow.constants.PaynowUrls;
import zw.co.paynow.constants.TransactionStatus;
import zw.co.paynow.exceptions.ConnectionException;
import zw.co.paynow.exceptions.EmptyCartException;
import zw.co.paynow.exceptions.HashMismatchException;
import zw.co.paynow.exceptions.InvalidReferenceException;
import zw.co.paynow.http.HttpClient;
import zw.co.paynow.parsers.UrlParser;
import zw.co.paynow.responses.MobileInitResponse;
import zw.co.paynow.responses.StatusResponse;
import zw.co.paynow.responses.WebInitResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CoreTest {

    private static String integrationKey;
    private static String integrationId;
    private static String resultUrl;
    private static String dummyEmail;
    private static Paynow paynow;
    private static HashMap<String, BigDecimal> dummyCart;
    private static Payment dummyPayment;
    private static String dummyMobile;

    @Mock
    private HttpClient httpClient;

    @BeforeClass
    public static void setup() {
        integrationKey = "0123456789";
        integrationId = "9876543210";
        resultUrl = "www.example.org/resultUrl";
        dummyEmail = "example@example.org";
        dummyMobile = "0771222333";
    }

    @Before
    public void init() {
        paynow = new Paynow(integrationId, integrationKey, resultUrl);
        paynow.setHttpHttpClient(httpClient);

        dummyCart = new HashMap<>();
        dummyCart.put("Bananas", new BigDecimal(1));
        dummyCart.put("Apple", new BigDecimal(2));
        dummyCart.put("Oranges", new BigDecimal(3));

        dummyPayment = paynow.createPayment("1", dummyEmail);
        dummyPayment.add("Bananas", new BigDecimal(1));
        dummyPayment.add("Apple", new BigDecimal(2));
        dummyPayment.add("Oranges", new BigDecimal(3));

    }

    @Test
    public void CreatePaymentMethods_ValidParams_PaymentObjectWithCorrectFieldValues() {

        Payment payment1 = paynow.createPayment("1");
        assertEquals("1", payment1.getMerchantReference());
        assertThat(payment1.getCart(), not(nullValue()));
        assertEquals(0, payment1.getCart().size());
        assertEquals("", payment1.getAuthEmail());

        Payment payment2 = paynow.createPayment("1", dummyEmail);
        assertEquals("1", payment2.getMerchantReference());
        assertEquals(dummyEmail, payment2.getAuthEmail());
        assertThat(payment2.getCart(), not(nullValue()));
        assertEquals(0, payment2.getCart().size());

        Payment payment3 = paynow.createPayment("1", dummyCart);
        assertEquals("1", payment3.getMerchantReference());
        assertThat(payment3.getCart(), not(nullValue()));
        assertEquals(dummyCart, payment3.getCart());
        assertEquals(dummyCart.size(), payment3.getCart().size());
        assertEquals("", payment3.getAuthEmail());

        Payment payment4 = paynow.createPayment("1", dummyCart, dummyEmail);
        assertEquals("1", payment4.getMerchantReference());
        assertThat(payment4.getCart(), not(nullValue()));
        assertEquals(dummyCart, payment4.getCart());
        assertEquals(dummyCart.size(), payment4.getCart().size());
        assertEquals(dummyEmail, payment4.getAuthEmail());

    }

    @Test(expected = InvalidReferenceException.class)
    public void CreatePaymentForWeb_PaymentWithEmptyReference_ExceptionThrown() {

        dummyPayment = paynow.createPayment("", dummyEmail);

        paynow.send(dummyPayment);

    }

    @Test(expected = InvalidReferenceException.class)
    public void CreatePaymentForMobile_PaymentWithEmptyReference_ExceptionThrown() {

        dummyPayment = paynow.createPayment("", dummyEmail);

        paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

    }

    @Test(expected = IllegalArgumentException.class)
    public void SendMobile_PaymentWithNoEmail_ExceptionThrown() {
        dummyPayment = paynow.createPayment("1", "");
        dummyPayment.add("Bananas", new BigDecimal(1));
        paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void SendMobile_PaymentWithBadEmail_ExceptionThrown() {
        dummyPayment = paynow.createPayment("1", "bad@email");
        dummyPayment.add("Bananas", new BigDecimal(1));
        paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void SendMobile_PaymentWithNullEmail_ExceptionThrown() {
        String nullString = null;
        //noinspection ConstantConditions
        dummyPayment = paynow.createPayment("1", nullString);
        dummyPayment.add("Bananas", new BigDecimal(1));
        paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);
    }

    @Test
    public void ParseStatusWithStringAsArg_ValidResponseString_ParseStatusWithCorrectFieldValues() {

        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=DD982F5F279A97BE50E0156742804CAFA5E658E58AC0DB4B7D91C1B45E95A3B42475F91A249DDBEB1FF2FB69D80D3FAE3DC2289649B03ADEDEDB9624D444485E";
        StatusResponse statusResponse = paynow.parseStatus(rawResponse);
        assertEquals("Invoice 32", statusResponse.getMerchantReference());
        assertEquals("1234567", statusResponse.getPaynowReference());
        assertEquals("Invoice 32", statusResponse.getMerchantReference());
        assertEquals(new BigDecimal(3.5), statusResponse.getAmount());
        assertFalse(statusResponse.paid());
        assertThat(statusResponse.getRawResponseContent(), not(nullValue()));
        assertEquals(6, statusResponse.getRawResponseContent().size());
        assertEquals(UrlParser.parseMapFromQueryString(rawResponse), statusResponse.getRawResponseContent());

    }

    @Test
    public void ParseStatusWithMapAsArg_ValidResponseString_ParseStatusWithCorrectFieldValues() {

        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=DD982F5F279A97BE50E0156742804CAFA5E658E58AC0DB4B7D91C1B45E95A3B42475F91A249DDBEB1FF2FB69D80D3FAE3DC2289649B03ADEDEDB9624D444485E";
        HashMap<String, String> mapResponse = UrlParser.parseMapFromQueryString(rawResponse);
        StatusResponse statusResponse = paynow.parseStatus(mapResponse);
        assertEquals("Invoice 32", statusResponse.getMerchantReference());
        assertEquals("1234567", statusResponse.getPaynowReference());
        assertEquals("Invoice 32", statusResponse.getMerchantReference());
        assertEquals(new BigDecimal(3.5), statusResponse.getAmount());
        assertFalse(statusResponse.paid());
        assertThat(statusResponse.getRawResponseContent(), not(nullValue()));
        assertEquals(6, statusResponse.getRawResponseContent().size());
        assertEquals(UrlParser.parseMapFromQueryString(rawResponse), statusResponse.getRawResponseContent());

    }

    @Test(expected = HashMismatchException.class)
    public void ParseStatusWithMapAsArg_BadHash_ExceptionThrown() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=WrongHash";
        HashMap<String, String> mapResponse = UrlParser.parseMapFromQueryString(rawResponse);
        paynow.parseStatus(mapResponse);
    }

    @Test(expected = HashMismatchException.class)
    public void ParseStatusWithMapAsArg_NoHashEntryInMap_ExceptionThrown() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68";
        HashMap<String, String> mapResponse = UrlParser.parseMapFromQueryString(rawResponse);
        paynow.parseStatus(mapResponse);
    }

    @Test(expected = HashMismatchException.class)
    public void ParseStatusWithStringAsArg_NoHashEntryInMap_ExceptionThrown() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=WrongHash";
        paynow.parseStatus(rawResponse);
    }

    @Test
    public void SendForWeb_CorrectAndValidPaymentObj_ResponseIsReturnedWithCorrectValues() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        String rawResponse = "status=Ok&browserurl=https%3a%2f%2fwww.paynow.co.zw%2fPayment%2fConfirmPayment%2f2638504%2ftmoyo%40yahoo.com%2f%2f&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=CDC4027ED4E94CEF5B95DCC3A86A2BC071E0BDFD6C8B8E5323B8F93DD260CC0EC4195815985FCB767DDD2384E7F1AA331159E137928005A372B97D5CFA4562C7";

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitWebTransactionRequest", Payment.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment);

        when(httpClient.postAsync(PaynowUrls.INITIATE_TRANSACTION, data)).thenReturn(rawResponse);

        WebInitResponse response = paynow.send(dummyPayment);

        verify(httpClient, times(1)).postAsync(PaynowUrls.INITIATE_TRANSACTION, data);

        assertEquals(TransactionStatus.OK, response.getStatus());
        assertTrue(response.success());
        assertEquals(0, response.getErrors().size());
        assertThat(response.getHash(), not(isEmptyOrNullString()));
        assertThat(response.getRedirectURL(), not(isEmptyOrNullString()));
        assertThat(response.getPollUrl(), not(isEmptyOrNullString()));

    }

    @Test
    public void SendForMobile_CorrectAndValidPaymentObj_ResponseIsReturnedWithCorrectValues() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        String rawResponse = "status=Ok&paynowreference=0123456&instructions=Some_random_instructions&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=DB191C4CDE56138E11A398CA6AB1E1FC5DEEA870FFDDE4557F2A8994EFF8BA284C5318BC2213AA65449B5565AC6FC6A164554E5463EDF0164D149A0E0D24AB8E";

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitMobileTransactionRequest", Payment.class, String.class, MobileMoneyMethod.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

        when(httpClient.postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data)).thenReturn(rawResponse);

        MobileInitResponse response = paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

        verify(httpClient, times(1)).postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data);

        assertEquals(TransactionStatus.OK, response.getStatus());
        assertTrue(response.success());
        assertEquals(0, response.getErrors().size());
        assertEquals("0123456", response.getPaynowReference());
        assertThat(response.getHash(), not(isEmptyOrNullString()));
        assertThat(response.getInstructions(), not(isEmptyOrNullString()));
        assertThat(response.getPollUrl(), not(isEmptyOrNullString()));

    }

    @Test
    public void PollTransaction_ValidResponseString_ResponseIsReturnedWithCorrectValues() throws IOException {

        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=DD982F5F279A97BE50E0156742804CAFA5E658E58AC0DB4B7D91C1B45E95A3B42475F91A249DDBEB1FF2FB69D80D3FAE3DC2289649B03ADEDEDB9624D444485E";
        String pollUrl = "www.dummypollurl.co.zw";

        when(httpClient.postAsync(pollUrl, null)).thenReturn(rawResponse);

        StatusResponse statusResponse = paynow.pollTransaction(pollUrl);

        verify(httpClient, times(1)).postAsync(pollUrl, null);

        assertEquals("Invoice 32", statusResponse.getMerchantReference());
        assertEquals("1234567", statusResponse.getPaynowReference());
        assertEquals("Invoice 32", statusResponse.getMerchantReference());
        assertEquals(new BigDecimal(3.5), statusResponse.getAmount());
        assertFalse(statusResponse.paid());
        assertThat(statusResponse.getRawResponseContent(), not(nullValue()));
        assertEquals(6, statusResponse.getRawResponseContent().size());
        assertEquals(UrlParser.parseMapFromQueryString(rawResponse), statusResponse.getRawResponseContent());

    }

    @Test(expected = HashMismatchException.class)
    public void SendForWeb_PaymentObjWithBadHash_ExceptionIsThrown() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        String rawResponse = "status=Ok&browserurl=https%3a%2f%2fwww.paynow.co.zw%2fPayment%2fConfirmPayment%2f2638504%2ftmoyo%40yahoo.com%2f%2f&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=Wronghash";

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitWebTransactionRequest", Payment.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment);

        when(httpClient.postAsync(PaynowUrls.INITIATE_TRANSACTION, data)).thenReturn(rawResponse);

        paynow.send(dummyPayment);

    }

    @Test(expected = HashMismatchException.class)
    public void SendForMobile_PaymentObjWithBadHash_ExceptionIsThrown() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        String rawResponse = "status=Ok&paynowreference=0123456&instructions=Some_random_instructions&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=WrongHash";

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitMobileTransactionRequest", Payment.class, String.class, MobileMoneyMethod.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

        when(httpClient.postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data)).thenReturn(rawResponse);

        paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

    }

    @Test(expected = ConnectionException.class)
    public void SendForMobile_ExceptionOccursDuringExecution_ExceptionIsCaughtAndCustomExceptionIsThrown() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitMobileTransactionRequest", Payment.class, String.class, MobileMoneyMethod.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

        when(httpClient.postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data)).thenThrow(new IOException());

        paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

    }

    @Test(expected = ConnectionException.class)
    public void SendForWeb_ExceptionOccursDuringExecution_ExceptionIsCaughtAndCustomExceptionIsThrown() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitWebTransactionRequest", Payment.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment);

        when(httpClient.postAsync(PaynowUrls.INITIATE_TRANSACTION, data)).thenThrow(new IOException());

        paynow.send(dummyPayment);

    }

    @Test(expected = EmptyCartException.class)
    public void SendForWeb_EmptyCart_ExceptionThrown() {
        dummyPayment.getCart().clear();
        paynow.send(dummyPayment);
    }

    @Test(expected = EmptyCartException.class)
    public void SendForMobile_EmptyCart_ExceptionThrown() {
        dummyPayment.getCart().clear();
        paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);
    }

    @Test
    public void CalculateTotal_CartItemsSetInInit_CorrectTotal() {

        BigDecimal total = dummyPayment.calculateTotal();

        assertTrue(new BigDecimal(6).compareTo(total) == 0);

    }

    @Test
    public void ToDictionary_NoParams_CorrectValues() {

        HashMap<String, String> map = dummyPayment.toDictionary();

        assertEquals("", map.get("resulturl"));
        assertEquals("", map.get("returnurl"));
        assertEquals("1", map.get("reference"));
        assertEquals("6.00", map.get("amount"));
        assertEquals("", map.get("id"));
        assertEquals("Apple, Bananas, Oranges", map.get("additionalinfo"));
        assertEquals(dummyEmail, map.get("authemail"));
        assertEquals("Message", map.get("status"));

    }

}
