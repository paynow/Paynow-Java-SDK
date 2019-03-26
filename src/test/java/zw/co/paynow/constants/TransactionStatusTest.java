package zw.co.paynow.constants;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.*;

public class TransactionStatusTest {

    @Test
    public void GetResponseString_CurrentConstantValuesSet_ResponseStringReturnsCorrectValue() {
        assertTrue("OK".equalsIgnoreCase(TransactionStatus.OK.getResponseString()));
        assertTrue("PAID".equalsIgnoreCase(TransactionStatus.PAID.getResponseString()));
        assertTrue("CANCELLED".equalsIgnoreCase(TransactionStatus.CANCELLED.getResponseString()));
        assertTrue("SENT".equalsIgnoreCase(TransactionStatus.SENT.getResponseString()));
        assertTrue("AWAITING DELIVERY".equalsIgnoreCase(TransactionStatus.DELIVERY.getResponseString()));
        assertTrue("DISPUTED".equalsIgnoreCase(TransactionStatus.DISPUTED.getResponseString()));
        assertTrue("REFUNDED".equalsIgnoreCase(TransactionStatus.REFUNDED.getResponseString()));
        assertTrue("DELIVERED".equalsIgnoreCase(TransactionStatus.DELIVERED.getResponseString()));
        assertTrue("ERROR".equalsIgnoreCase(TransactionStatus.ERROR.getResponseString()));
        assertTrue("CREATED".equalsIgnoreCase(TransactionStatus.CREATED.getResponseString()));
        assertTrue("INVALID ID.".equalsIgnoreCase(TransactionStatus.INVALID_ID.getResponseString()));
    }

    @Test
    public void GetTransactionStatus_SampleExpectedString_ReturnsCorrectStatusEnum() {
        TransactionStatus transactionStatusOk = TransactionStatus.getTransactionStatus("Ok");
        assertEquals(TransactionStatus.OK, transactionStatusOk);

        TransactionStatus transactionStatusPaid = TransactionStatus.getTransactionStatus("PAID");
        assertEquals(TransactionStatus.PAID, transactionStatusPaid);

        TransactionStatus transactionStatusCancelled = TransactionStatus.getTransactionStatus("CANCELLED");
        assertEquals(TransactionStatus.CANCELLED, transactionStatusCancelled);

        TransactionStatus transactionStatusSent = TransactionStatus.getTransactionStatus("SENT");
        assertEquals(TransactionStatus.SENT, transactionStatusSent);

        TransactionStatus transactionStatusDelivery = TransactionStatus.getTransactionStatus("AWAITING DELIVERY");
        assertEquals(TransactionStatus.DELIVERY, transactionStatusDelivery);

        TransactionStatus transactionStatusDisputed = TransactionStatus.getTransactionStatus("DISPUTED");
        assertEquals(TransactionStatus.DISPUTED, transactionStatusDisputed);

        TransactionStatus transactionStatusRefunded = TransactionStatus.getTransactionStatus("REFUNDED");
        assertEquals(TransactionStatus.REFUNDED, transactionStatusRefunded);

        TransactionStatus transactionStatusDelivered = TransactionStatus.getTransactionStatus("DELIVERED");
        assertEquals(TransactionStatus.DELIVERED, transactionStatusDelivered);

        TransactionStatus transactionStatusError = TransactionStatus.getTransactionStatus("ERROR");
        assertEquals(TransactionStatus.ERROR, transactionStatusError);

        TransactionStatus transactionStatusCreated = TransactionStatus.getTransactionStatus("CREATED");
        assertEquals(TransactionStatus.CREATED, transactionStatusCreated);

        TransactionStatus transactionStatusInvalidId = TransactionStatus.getTransactionStatus("INVALID ID.");
        assertEquals(TransactionStatus.INVALID_ID, transactionStatusInvalidId);

    }

    @Test
    public void GetTransactionStatus_SampleNonExistentString_ReturnsUndefinedStatusEnum() {
        TransactionStatus transactionStatusInvalidId = TransactionStatus.getTransactionStatus("Some random text");
        assertEquals(TransactionStatus.UNDEFINED, transactionStatusInvalidId);
    }

    @Test
    public void GetDescription_CurrentConstantValuesSet_DescriptionIsNotNull() {
        assertThat(TransactionStatus.OK.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.PAID.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.CANCELLED.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.ERROR.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.DELIVERED.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.REFUNDED.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.DISPUTED.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.DELIVERY.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.SENT.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.CREATED.getDescription(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.INVALID_ID.getDescription(), not(isEmptyOrNullString()));
    }

    @Test
    public void ToString_CurrentConstantValuesSet_ToStringIsNotNullOrEmpty() {
        assertThat(TransactionStatus.OK.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.PAID.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.CANCELLED.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.ERROR.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.DELIVERED.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.REFUNDED.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.DISPUTED.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.DELIVERY.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.SENT.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.CREATED.toString(), not(isEmptyOrNullString()));
        assertThat(TransactionStatus.INVALID_ID.toString(), not(isEmptyOrNullString()));
    }

}