package zw.co.paynow.parsers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Payment Parser Test")
class PaymentParserTest {

    private HashMap<String, BigDecimal> items;

    @BeforeEach
    void init() {
        items = new HashMap<>();
        items.put("Bananas", new BigDecimal(1));
        items.put("Apple", new BigDecimal(2));
        items.put("Oranges", new BigDecimal(3));
    }

    @Test
    @DisplayName("Flatten Collection Items Set In Init Correct String From Map")
    void FlattenCollection_ItemsSetInInit_CorrectStringFromMap() {
        assertThat(PaymentParser.flattenCollection(items)).isEqualTo("Apple, Bananas, Oranges");
    }

    @Test
    @DisplayName("Flatten Collection Empty Map Correct String From Map")
    void FlattenCollection_EmptyMap_CorrectStringFromMap() {
        items.clear();
        assertThat(PaymentParser.flattenCollection(items)).isEmpty();
    }

    @Test
    @DisplayName("Add Collection Values Items Set In Init Correct Total")
    void AddCollectionValues_ItemsSetInInit_CorrectTotal() {
        BigDecimal total = PaymentParser.addCollectionValues(items);
        assertThat(total).isEqualByComparingTo(new BigDecimal(6));
    }

    @Test
    @DisplayName("Add Collection Values Empty Map Correct Total")
    void AddCollectionValues_EmptyMap_CorrectTotal() {
        items.clear();
        BigDecimal total = PaymentParser.addCollectionValues(items);
        assertThat(total).isEqualByComparingTo(new BigDecimal(0));
    }
}
