package zw.co.paynow.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Core Test")
class CoreTest {

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

    @BeforeAll
    static void setup() {
        integrationKey = "0123456789";
        integrationId = "9876543210";
        resultUrl = "www.example.org/resultUrl";
        dummyEmail = "example@example.org";
        dummyMobile = "0771222333";
    }

    @BeforeEach
    void init() {
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
    @DisplayName("Create Payment Methods Valid Params Payment Object With Correct Field Values")
    void CreatePaymentMethods_ValidParams_PaymentObjectWithCorrectFieldValues() {
        Payment payment1 = paynow.createPayment("1");
        assertThat(payment1.getMerchantReference()).isEqualTo("1");
        assertThat(payment1.getCart()).isNotNull();
        assertThat(payment1.getCart().size()).isZero();
        assertThat(payment1.getAuthEmail()).isEmpty();
        Payment payment2 = paynow.createPayment("1", dummyEmail);
        assertThat(payment2.getMerchantReference()).isEqualTo("1");
        assertThat(payment2.getAuthEmail()).isEqualTo(dummyEmail);
        assertThat(payment2.getCart()).isNotNull();
        assertThat(payment2.getCart().size()).isZero();

        Payment payment3 = paynow.createPayment("1", dummyCart);
        assertThat(payment3.getMerchantReference()).isEqualTo("1");
        assertThat(payment3.getCart()).isNotNull();
        assertThat(payment3.getCart()).isEqualTo(dummyCart);
        assertThat(payment3.getCart().size()).isEqualTo(dummyCart.size());
        assertThat(payment3.getAuthEmail()).isEmpty();
        Payment payment4 = paynow.createPayment("1", dummyCart, dummyEmail);
        assertThat(payment4.getMerchantReference()).isEqualTo("1");
        assertThat(payment4.getCart()).isNotNull();
        assertThat(payment4.getCart()).isEqualTo(dummyCart);
        assertThat(payment4.getCart().size()).isEqualTo(dummyCart.size());
        assertThat(payment4.getAuthEmail()).isEqualTo(dummyEmail);
    }

    @Test
    @DisplayName("Create Payment For Web Payment With Empty Reference Exception Thrown")
    void CreatePaymentForWeb_PaymentWithEmptyReference_ExceptionThrown() {
        dummyPayment = paynow.createPayment("", dummyEmail);

        assertThrows(InvalidReferenceException.class, () -> paynow.send(dummyPayment));
    }

    @Test
    @DisplayName("Create Payment For Mobile Payment With Empty Reference Exception Thrown")
    void CreatePaymentForMobile_PaymentWithEmptyReference_ExceptionThrown() {
        dummyPayment = paynow.createPayment("", dummyEmail);

        assertThrows(InvalidReferenceException.class, () -> paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH));
    }

    @Test
    @DisplayName("Send Mobile Payment With No Email Exception Thrown")
    void SendMobile_PaymentWithNoEmail_ExceptionThrown() {
        dummyPayment = paynow.createPayment("1", "");
        dummyPayment.add("Bananas", new BigDecimal(1));

        assertThrows(IllegalArgumentException.class, () -> paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH));
    }

    @Test
    @DisplayName("Send Mobile Payment With Bad Email Exception Thrown")
    void SendMobile_PaymentWithBadEmail_ExceptionThrown() {
        dummyPayment = paynow.createPayment("1", "bad@email");
        dummyPayment.add("Bananas", new BigDecimal(1));

        assertThrows(IllegalArgumentException.class, () -> paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH));
    }

    @Test
    @DisplayName("Send Mobile Payment With Null Email Exception Thrown")
    void SendMobile_PaymentWithNullEmail_ExceptionThrown() {
        String nullString = null;
        // noinspection ConstantConditions
        dummyPayment = paynow.createPayment("1", nullString);
        dummyPayment.add("Bananas", new BigDecimal(1));

        assertThrows(IllegalArgumentException.class, () -> paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH));
    }

    @Test
    @DisplayName("Parse Status With String As Arg Valid Response String Parse Status With Correct Field Values")
    void ParseStatusWithStringAsArg_ValidResponseString_ParseStatusWithCorrectFieldValues() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=DD982F5F279A97BE50E0156742804CAFA5E658E58AC0DB4B7D91C1B45E95A3B42475F91A249DDBEB1FF2FB69D80D3FAE3DC2289649B03ADEDEDB9624D444485E";

        StatusResponse statusResponse = paynow.parseStatus(rawResponse);

        assertThat(statusResponse.getMerchantReference()).isEqualTo("Invoice 32");
        assertThat(statusResponse.getPaynowReference()).isEqualTo("1234567");
        assertThat(statusResponse.getMerchantReference()).isEqualTo("Invoice 32");
        assertThat(statusResponse.getAmount()).isEqualTo(new BigDecimal("3.5"));
        assertThat(statusResponse.paid()).isFalse();
        assertThat(statusResponse.getRawResponseContent()).isNotNull();
        assertThat(statusResponse.getRawResponseContent()).hasSize(6);
        assertThat(statusResponse.getRawResponseContent()).isEqualTo(UrlParser.parseMapFromQueryString(rawResponse));

    }

    @Test
    @DisplayName("Parse Status With Map As Arg Valid Response String Parse Status With Correct Field Values")
    void ParseStatusWithMapAsArg_ValidResponseString_ParseStatusWithCorrectFieldValues() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=DD982F5F279A97BE50E0156742804CAFA5E658E58AC0DB4B7D91C1B45E95A3B42475F91A249DDBEB1FF2FB69D80D3FAE3DC2289649B03ADEDEDB9624D444485E";

        HashMap<String, String> mapResponse = UrlParser.parseMapFromQueryString(rawResponse);

        StatusResponse statusResponse = paynow.parseStatus(mapResponse);
        assertThat(statusResponse.getMerchantReference()).isEqualTo("Invoice 32");
        assertThat(statusResponse.getPaynowReference()).isEqualTo("1234567");
        assertThat(statusResponse.getMerchantReference()).isEqualTo("Invoice 32");
        assertThat(statusResponse.getAmount()).isEqualTo(new BigDecimal("3.5"));
        assertThat(statusResponse.paid()).isFalse();
        assertThat(statusResponse.getRawResponseContent()).isNotNull();
        assertThat(statusResponse.getRawResponseContent()).hasSize(6);
        assertThat(statusResponse.getRawResponseContent()).isEqualTo(UrlParser.parseMapFromQueryString(rawResponse));
    }

    @Test
    @DisplayName("Parse Status With Map As Arg Bad Hash Exception Thrown")
    void ParseStatusWithMapAsArg_BadHash_ExceptionThrown() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=WrongHash";

        HashMap<String, String> mapResponse = UrlParser.parseMapFromQueryString(rawResponse);

        assertThrows(HashMismatchException.class, () -> paynow.parseStatus(mapResponse));
    }

    @Test
    @DisplayName("Parse Status With Map As Arg No Hash Entry In Map Exception Thrown")
    void ParseStatusWithMapAsArg_NoHashEntryInMap_ExceptionThrown() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68";

        HashMap<String, String> mapResponse = UrlParser.parseMapFromQueryString(rawResponse);

        assertThrows(HashMismatchException.class, () -> paynow.parseStatus(mapResponse));
    }

    @Test
    @DisplayName("Parse Status With String As Arg No Hash Entry In Map Exception Thrown")
    void ParseStatusWithStringAsArg_NoHashEntryInMap_ExceptionThrown() {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=WrongHash";

        assertThrows(HashMismatchException.class, () -> paynow.parseStatus(rawResponse));
    }

    @Test
    @DisplayName("Send For Web Correct And Valid Payment Obj Response Is Returned With Correct Values")
    void SendForWeb_CorrectAndValidPaymentObj_ResponseIsReturnedWithCorrectValues() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String rawResponse = "status=Ok&browserurl=https%3a%2f%2fwww.paynow.co.zw%2fPayment%2fConfirmPayment%2f2638504%2ftmoyo%40yahoo.com%2f%2f&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=CDC4027ED4E94CEF5B95DCC3A86A2BC071E0BDFD6C8B8E5323B8F93DD260CC0EC4195815985FCB767DDD2384E7F1AA331159E137928005A372B97D5CFA4562C7";

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitWebTransactionRequest", Payment.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment);

        when(httpClient.postAsync(PaynowUrls.INITIATE_TRANSACTION, data)).thenReturn(rawResponse);

        WebInitResponse response = paynow.send(dummyPayment);

        verify(httpClient, times(1)).postAsync(PaynowUrls.INITIATE_TRANSACTION, data);

        assertThat(response.getStatus()).isEqualTo(TransactionStatus.OK);
        assertThat(response.success()).isTrue();
        assertThat(response.getErrors()).isEmpty();
        assertThat(response.getHash()).isNotEmpty();
        assertThat(response.getRedirectURL()).isNotEmpty();
        assertThat(response.getPollUrl()).isNotEmpty();

    }

    @Test
    @DisplayName("Send For Mobile Correct And Valid Payment Obj Response Is Returned With Correct Values")
    void SendForMobile_CorrectAndValidPaymentObj_ResponseIsReturnedWithCorrectValues() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String rawResponse = "status=Ok&paynowreference=0123456&instructions=Some_random_instructions&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=DB191C4CDE56138E11A398CA6AB1E1FC5DEEA870FFDDE4557F2A8994EFF8BA284C5318BC2213AA65449B5565AC6FC6A164554E5463EDF0164D149A0E0D24AB8E";

        //Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitMobileTransactionRequest", Payment.class, String.class, MobileMoneyMethod.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked") HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

        when(httpClient.postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data)).thenReturn(rawResponse);

        MobileInitResponse response = paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);

        verify(httpClient, times(1)).postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data);

        assertThat(response.getStatus()).isEqualTo(TransactionStatus.OK);
        assertThat(response.success()).isTrue();
        assertThat(response.getErrors()).isEmpty();
        assertThat(response.getPaynowReference()).isEqualTo("0123456");
        assertThat(response.getHash()).isNotEmpty();
        assertThat(response.getInstructions()).isNotEmpty();
        assertThat(response.getPollUrl()).isNotEmpty();
    }

    @Test
    @DisplayName("Poll Transaction Valid Response String Response Is Returned With Correct Values")
    void PollTransaction_ValidResponseString_ResponseIsReturnedWithCorrectValues() throws IOException {
        String rawResponse = "reference=Invoice+32&paynowreference=1234567&amount=3.50&status=Created&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d06f656ab-ed60-4a03-8373-a3321b4b3e68&hash=DD982F5F279A97BE50E0156742804CAFA5E658E58AC0DB4B7D91C1B45E95A3B42475F91A249DDBEB1FF2FB69D80D3FAE3DC2289649B03ADEDEDB9624D444485E";
        String pollUrl = "www.dummypollurl.co.zw";

        when(httpClient.postAsync(pollUrl, null)).thenReturn(rawResponse);

        StatusResponse statusResponse = paynow.pollTransaction(pollUrl);

        verify(httpClient, times(1)).postAsync(pollUrl, null);
        assertThat(statusResponse.getMerchantReference()).isEqualTo("Invoice 32");
        assertThat(statusResponse.getPaynowReference()).isEqualTo("1234567");
        assertThat(statusResponse.getMerchantReference()).isEqualTo("Invoice 32");
        assertThat(statusResponse.getAmount()).isEqualTo(new BigDecimal("3.5"));
        assertThat(statusResponse.paid()).isFalse();
        assertThat(statusResponse.getRawResponseContent()).isNotNull();
        assertThat(statusResponse.getRawResponseContent()).hasSize(6);
        assertThat(statusResponse.getRawResponseContent()).isEqualTo(UrlParser.parseMapFromQueryString(rawResponse));
    }

    @Test
    @DisplayName("Send For Web Payment Obj With Bad Hash Exception Is Thrown")
    void SendForWeb_PaymentObjWithBadHash_ExceptionIsThrown() throws Exception {
        String rawResponse = "status=Ok&browserurl=https%3a%2f%2fwww.paynow.co.zw%2fPayment%2fConfirmPayment%2f2638504%2ftmoyo%40yahoo.com%2f%2f&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=Wronghash";

        // Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitWebTransactionRequest", Payment.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment);
        when(httpClient.postAsync(PaynowUrls.INITIATE_TRANSACTION, data)).thenReturn(rawResponse);

        assertThrows(HashMismatchException.class, () -> paynow.send(dummyPayment));
    }

    @Test
    @DisplayName("Send For Mobile Payment Obj With Bad Hash Exception Is Thrown")
    void SendForMobile_PaymentObjWithBadHash_ExceptionIsThrown() throws Exception {
        String rawResponse = "status=Ok&paynowreference=0123456&instructions=Some_random_instructions&pollurl=https%3a%2f%2fwww.paynow.co.zw%2fInterface%2fCheckPayment%2f%3fguid%3d09471727-37a6-4a0c-a7f3-c1ac48f79431&hash=WrongHash";

        // Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitMobileTransactionRequest", Payment.class, String.class, MobileMoneyMethod.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);
        when(httpClient.postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data)).thenReturn(rawResponse);

        assertThrows(HashMismatchException.class, () -> paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH));
    }

    @Test
    @DisplayName("Send For Mobile Exception Occurs During Execution Exception Is Caught And Custom Exception Is Thrown")
    void SendForMobile_ExceptionOccursDuringExecution_ExceptionIsCaughtAndCustomExceptionIsThrown() throws Exception {
        // Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitMobileTransactionRequest", Payment.class, String.class, MobileMoneyMethod.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH);
        when(httpClient.postAsync(PaynowUrls.INITIATE_MOBILE_TRANSACTION, data)).thenThrow(new IOException());

        assertThrows(ConnectionException.class, () -> paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH));
    }

    @Test
    @DisplayName("Send For Web Exception Occurs During Execution Exception Is Caught And Custom Exception Is Thrown")
    void SendForWeb_ExceptionOccursDuringExecution_ExceptionIsCaughtAndCustomExceptionIsThrown() throws Exception {
        // Access private method using reflection
        Method privateMethod = paynow.getClass().getDeclaredMethod("formatInitWebTransactionRequest", Payment.class);
        privateMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        HashMap<String, String> data = (HashMap<String, String>) privateMethod.invoke(paynow, dummyPayment);
        when(httpClient.postAsync(PaynowUrls.INITIATE_TRANSACTION, data)).thenThrow(new IOException());

        assertThrows(ConnectionException.class, () -> paynow.send(dummyPayment));
    }

    @Test
    @DisplayName("Send For Web Empty Cart Exception Thrown")
    void SendForWeb_EmptyCart_ExceptionThrown() {
        dummyPayment.getCart().clear();

        assertThrows(EmptyCartException.class, () -> paynow.send(dummyPayment));
    }

    @Test
    @DisplayName("Send For Mobile Empty Cart Exception Thrown")
    void SendForMobile_EmptyCart_ExceptionThrown() {
        dummyPayment.getCart().clear();

        assertThrows(EmptyCartException.class, () -> paynow.sendMobile(dummyPayment, dummyMobile, MobileMoneyMethod.ECOCASH));
    }

    @Test
    @DisplayName("Calculate Total Cart Items Set In Init Correct Total")
    void CalculateTotal_CartItemsSetInInit_CorrectTotal() {
        BigDecimal total = dummyPayment.calculateTotal();

        assertThat(total).isEqualByComparingTo(new BigDecimal(6));

    }

    @Test
    @DisplayName("To Dictionary No Params Correct Values")
    void ToDictionary_NoParams_CorrectValues() {
        HashMap<String, String> map = dummyPayment.toDictionary();
        assertThat(map.get("resulturl")).isEmpty();
        assertThat(map.get("returnurl")).isEmpty();
        assertThat(map.get("reference")).isEqualTo("1");
        assertThat(map.get("amount")).isEqualTo("6.00");
        assertThat(map.get("id")).isEmpty();
        assertThat(map.get("additionalinfo")).isEqualTo("Apple, Bananas, Oranges");
        assertThat(map.get("authemail")).isEqualTo(dummyEmail);
        assertThat(map.get("status")).isEqualTo("Message");
    }

}
