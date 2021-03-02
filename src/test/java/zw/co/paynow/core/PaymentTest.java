package zw.co.paynow.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Payment Test")
class PaymentTest {

    private static String dummyEmail;
    private static HashMap<String, BigDecimal> dummyCart;
    private static String dummyMobile;
    private static Payment dummyPayment;

    @BeforeAll
    static void setup() {
        dummyEmail = "example@example.org";
        dummyMobile = "0771222333";
    }

    @BeforeEach
    void init() {
        dummyPayment = new Payment("some_reference", dummyEmail);
        dummyPayment.add("Bananas", new BigDecimal(1));
        dummyPayment.add("Apple", new BigDecimal(2));
        dummyPayment.add("Oranges", new BigDecimal(3));

        dummyCart = new HashMap<>();
        dummyCart.put("Bananas", new BigDecimal(1));
        dummyCart.put("Apple", new BigDecimal(2));
        dummyCart.put("Oranges", new BigDecimal(3));

    }

    @Test
    @DisplayName("Payment Constructors Valid Params Payment Object With Correct Field Values")
    void PaymentConstructors_ValidParams_PaymentObjectWithCorrectFieldValues() {
        Payment payment1 = new Payment("1", dummyEmail);
        assertThat(payment1.getMerchantReference()).isEqualTo("1");
        assertThat(payment1.getAuthEmail()).isEqualTo(dummyEmail);
        assertThat(payment1.getCart()).isNotNull();
        assertThat(payment1.getCart().size()).isZero();

        Payment payment2 = new Payment("1", dummyCart, dummyEmail);
        assertThat(payment2.getMerchantReference()).isEqualTo("1");
        assertThat(payment2.getCart()).isNotNull();
        assertThat(payment2.getCart()).isEqualTo(dummyCart);
        assertThat(payment2.getCart().size()).isEqualTo(dummyCart.size());
        assertThat(payment2.getAuthEmail()).isEqualTo(dummyEmail);

    }

    @Test
    @DisplayName("Add Using String And Double Valid Params Cart With One New Entry")
    void AddUsingStringAndDouble_ValidParams_CartWithOneNewEntry() {
        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.add("Coconuts", 3.2);
        assertThat(dummyPayment.getCart().size()).isEqualTo(currentCartSize + 1);
        assertThat(dummyPayment.getCart()).containsKey("Coconuts");
        assertThat(dummyPayment.getCart().get("Coconuts")).isEqualByComparingTo(new BigDecimal(3.2));
    }

    @Test
    @DisplayName("Add Using String And Int Valid Params Cart With One New Entry")
    void AddUsingStringAndInt_ValidParams_CartWithOneNewEntry() {
        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.add("Coconuts", 5);

        assertThat(dummyPayment.getCart().size()).isEqualTo(currentCartSize + 1);
        assertThat(dummyPayment.getCart()).containsKey("Coconuts");
        assertThat(dummyPayment.getCart().get("Coconuts")).isEqualByComparingTo(new BigDecimal(5));

    }

    @Test
    @DisplayName("Add Using String And Big Decimal Valid Params Cart With One New Entry")
    void AddUsingStringAndBigDecimal_ValidParams_CartWithOneNewEntry() {
        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.add("Coconuts", new BigDecimal(8));

        assertThat(dummyPayment.getCart().size()).isEqualTo(currentCartSize + 1);
        assertThat(dummyPayment.getCart()).containsKey("Coconuts");
        assertThat(dummyPayment.getCart().get("Coconuts")).isEqualByComparingTo(new BigDecimal(8));

    }

    @Test
    @DisplayName("Remove Existing Cart Item As Param Cart With One New Entry")
    void Remove_ExistingCartItemAsParam_CartWithOneNewEntry() {
        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.remove("Bananas");

        assertThat(dummyPayment.getCart().size()).isEqualTo(currentCartSize - 1);
        assertThat(dummyPayment.getCart()).doesNotContainKey("Bananas");

    }

    @Test
    @DisplayName("Remove None Existing Cart Item As Param Cart With One New Entry")
    void Remove_NoneExistingCartItemAsParam_CartWithOneNewEntry() {
        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.remove("Coconuts");

        assertThat(dummyPayment.getCart().size()).isEqualTo(currentCartSize);
        assertThat(dummyPayment.getCart()).doesNotContainKey("Coconuts");

    }

    @Test
    @DisplayName("Set Cart Description New Description As Param New Description")
    void SetCartDescription_NewDescriptionAsParam_NewDescription() {
        String newDescription = "Some new description";
        dummyPayment.setCartDescription(newDescription);

        assertThat(dummyPayment.isOverrideDescription()).isTrue();
        assertThat(dummyPayment.getCartDescription()).isEqualTo("Some new description");
    }
}