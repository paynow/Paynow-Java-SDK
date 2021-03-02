package zw.co.paynow.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Transaction Status Test")
class TransactionStatusTest {

    @Test
    @DisplayName("Get Response String Current Constant Values Set Response String Returns Correct Value")
    void GetResponseString_CurrentConstantValuesSet_ResponseStringReturnsCorrectValue() {
        assertThat(TransactionStatus.OK.getResponseString()).isEqualToIgnoringCase("OK");
        assertThat(TransactionStatus.PAID.getResponseString()).isEqualToIgnoringCase("PAID");
        assertThat(TransactionStatus.CANCELLED.getResponseString()).isEqualToIgnoringCase("CANCELLED");
        assertThat(TransactionStatus.SENT.getResponseString()).isEqualToIgnoringCase("SENT");
        assertThat(TransactionStatus.DELIVERY.getResponseString()).isEqualToIgnoringCase("AWAITING DELIVERY");
        assertThat(TransactionStatus.DISPUTED.getResponseString()).isEqualToIgnoringCase("DISPUTED");
        assertThat(TransactionStatus.REFUNDED.getResponseString()).isEqualToIgnoringCase("REFUNDED");
        assertThat(TransactionStatus.DELIVERED.getResponseString()).isEqualToIgnoringCase("DELIVERED");
        assertThat(TransactionStatus.ERROR.getResponseString()).isEqualToIgnoringCase("ERROR");
        assertThat(TransactionStatus.CREATED.getResponseString()).isEqualToIgnoringCase("CREATED");
        assertThat(TransactionStatus.INVALID_ID.getResponseString()).isEqualToIgnoringCase("INVALID ID.");
    }

    @Test
    @DisplayName("Get Transaction Status Sample Expected String Returns Correct Status Enum")
    void GetTransactionStatus_SampleExpectedString_ReturnsCorrectStatusEnum() {
        TransactionStatus transactionStatusOk = TransactionStatus.getTransactionStatus("Ok");
        assertThat(transactionStatusOk).isEqualTo(TransactionStatus.OK);

        TransactionStatus transactionStatusPaid = TransactionStatus.getTransactionStatus("PAID");
        assertThat(transactionStatusPaid).isEqualTo(TransactionStatus.PAID);

        TransactionStatus transactionStatusCancelled = TransactionStatus.getTransactionStatus("CANCELLED");
        assertThat(transactionStatusCancelled).isEqualTo(TransactionStatus.CANCELLED);

        TransactionStatus transactionStatusSent = TransactionStatus.getTransactionStatus("SENT");
        assertThat(transactionStatusSent).isEqualTo(TransactionStatus.SENT);

        TransactionStatus transactionStatusDelivery = TransactionStatus.getTransactionStatus("AWAITING DELIVERY");
        assertThat(transactionStatusDelivery).isEqualTo(TransactionStatus.DELIVERY);

        TransactionStatus transactionStatusDisputed = TransactionStatus.getTransactionStatus("DISPUTED");
        assertThat(transactionStatusDisputed).isEqualTo(TransactionStatus.DISPUTED);

        TransactionStatus transactionStatusRefunded = TransactionStatus.getTransactionStatus("REFUNDED");
        assertThat(transactionStatusRefunded).isEqualTo(TransactionStatus.REFUNDED);

        TransactionStatus transactionStatusDelivered = TransactionStatus.getTransactionStatus("DELIVERED");
        assertThat(transactionStatusDelivered).isEqualTo(TransactionStatus.DELIVERED);

        TransactionStatus transactionStatusError = TransactionStatus.getTransactionStatus("ERROR");
        assertThat(transactionStatusError).isEqualTo(TransactionStatus.ERROR);

        TransactionStatus transactionStatusCreated = TransactionStatus.getTransactionStatus("CREATED");
        assertThat(transactionStatusCreated).isEqualTo(TransactionStatus.CREATED);

        TransactionStatus transactionStatusInvalidId = TransactionStatus.getTransactionStatus("INVALID ID.");
        assertThat(transactionStatusInvalidId).isEqualTo(TransactionStatus.INVALID_ID);
    }

    @Test
    @DisplayName("Get Transaction Status Sample Non Existent String Returns Undefined Status Enum")
    void GetTransactionStatus_SampleNonExistentString_ReturnsUndefinedStatusEnum() {
        TransactionStatus transactionStatusInvalidId = TransactionStatus.getTransactionStatus("Some random text");
        assertThat(transactionStatusInvalidId).isEqualTo(TransactionStatus.UNDEFINED);
    }

    @Test
    @DisplayName("Get Description Current Constant Values Set Description Is Not Null")
    void GetDescription_CurrentConstantValuesSet_DescriptionIsNotNull() {
        assertThat(TransactionStatus.OK.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.PAID.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.CANCELLED.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.ERROR.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.DELIVERED.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.REFUNDED.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.DISPUTED.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.DELIVERY.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.SENT.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.CREATED.getDescription()).isNotEmpty();
        assertThat(TransactionStatus.INVALID_ID.getDescription()).isNotEmpty();
    }

    @Test
    @DisplayName("To String Current Constant Values Set To String Is Not Null Or Empty")
    void ToString_CurrentConstantValuesSet_ToStringIsNotNullOrEmpty() {
        assertThat(TransactionStatus.OK.toString()).isNotEmpty();
        assertThat(TransactionStatus.PAID.toString()).isNotEmpty();
        assertThat(TransactionStatus.CANCELLED.toString()).isNotEmpty();
        assertThat(TransactionStatus.ERROR.toString()).isNotEmpty();
        assertThat(TransactionStatus.DELIVERED.toString()).isNotEmpty();
        assertThat(TransactionStatus.REFUNDED.toString()).isNotEmpty();
        assertThat(TransactionStatus.DISPUTED.toString()).isNotEmpty();
        assertThat(TransactionStatus.DELIVERY.toString()).isNotEmpty();
        assertThat(TransactionStatus.SENT.toString()).isNotEmpty();
        assertThat(TransactionStatus.CREATED.toString()).isNotEmpty();
        assertThat(TransactionStatus.INVALID_ID.toString()).isNotEmpty();
    }
}
