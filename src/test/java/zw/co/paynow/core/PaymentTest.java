package zw.co.paynow.core;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class PaymentTest {

    private static String dummyEmail;
    private static HashMap<String, BigDecimal> dummyCart;
    private static String dummyMobile;
    private static Payment dummyPayment;

    @BeforeClass
    public static void setup() {
        dummyEmail = "example@example.org";
        dummyMobile = "0771222333";
    }

    @Before
    public void init() {

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
    public void PaymentConstructors_ValidParams_PaymentObjectWithCorrectFieldValues() {

        Payment payment1 = new Payment("1", dummyEmail);
        assertEquals("1", payment1.getMerchantReference());
        assertEquals(dummyEmail, payment1.getAuthEmail());
        assertThat(payment1.getCart(), not(nullValue()));
        assertEquals(0, payment1.getCart().size());

        Payment payment2 = new Payment("1", dummyCart, dummyEmail);
        assertEquals("1", payment2.getMerchantReference());
        assertThat(payment2.getCart(), not(nullValue()));
        assertEquals(dummyCart, payment2.getCart());
        assertEquals(dummyCart.size(), payment2.getCart().size());
        assertEquals(dummyEmail, payment2.getAuthEmail());

    }

    @Test
    public void AddUsingStringAndDouble_ValidParams_CartWithOneNewEntry() {

        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.add("Coconuts", 3.2);

        assertEquals(currentCartSize + 1, dummyPayment.getCart().size());
        assertTrue(dummyPayment.getCart().containsKey("Coconuts"));
        assertTrue(dummyPayment.getCart().get("Coconuts").compareTo(new BigDecimal(3.2)) == 0);

    }

    @Test
    public void AddUsingStringAndInt_ValidParams_CartWithOneNewEntry() {

        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.add("Coconuts", 5);

        assertEquals(currentCartSize + 1, dummyPayment.getCart().size());
        assertTrue(dummyPayment.getCart().containsKey("Coconuts"));
        assertTrue(dummyPayment.getCart().get("Coconuts").compareTo(new BigDecimal(5)) == 0);

    }

    @Test
    public void AddUsingStringAndBigDecimal_ValidParams_CartWithOneNewEntry() {

        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.add("Coconuts", new BigDecimal(8));

        assertEquals(currentCartSize + 1, dummyPayment.getCart().size());
        assertTrue(dummyPayment.getCart().containsKey("Coconuts"));
        assertTrue(dummyPayment.getCart().get("Coconuts").compareTo(new BigDecimal(8)) == 0);

    }

    @Test
    public void AddUsingStringAndString_ValidParams_CartWithOneNewEntry() {

        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.add("Coconuts", "3.2");

        assertEquals(currentCartSize + 1, dummyPayment.getCart().size());
        assertTrue(dummyPayment.getCart().containsKey("Coconuts"));
        assertTrue(dummyPayment.getCart().get("Coconuts").compareTo(new BigDecimal("3.2")) == 0);

    }

    @Test
    public void Remove_ExistingCartItemAsParam_CartWithOneNewEntry() {

        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.remove("Bananas");

        assertEquals(currentCartSize - 1, dummyPayment.getCart().size());
        assertTrue(!dummyPayment.getCart().containsKey("Bananas"));

    }

    @Test
    public void Remove_NoneExistingCartItemAsParam_CartWithOneNewEntry() {

        int currentCartSize = dummyPayment.getCart().size();
        dummyPayment.remove("Coconuts");

        assertEquals(currentCartSize, dummyPayment.getCart().size());
        assertTrue(!dummyPayment.getCart().containsKey("Coconuts"));

    }

    @Test
    public void SetCartDescription_NewDescriptionAsParam_NewDescription() {

        String newDescription = "Some new description";
        dummyPayment.setCartDescription(newDescription);

        assertTrue(dummyPayment.isOverrideDescription());
        assertEquals("Some new description", dummyPayment.getCartDescription());

    }

}