package zw.co.paynow.parsers;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentParserTest {

    private HashMap<String, BigDecimal> items;

    @Before
    public void init() {
        items = new HashMap<>();
        items.put("Bananas", new BigDecimal(1));
        items.put("Apple", new BigDecimal(2));
        items.put("Oranges", new BigDecimal(3));
    }

    @Test
    public void FlattenCollection_ItemsSetInInit_CorrectStringFromMap() {
        assertEquals("Apple, Bananas, Oranges", PaymentParser.flattenCollection(items));
    }

    @Test
    public void FlattenCollection_EmptyMap_CorrectStringFromMap() {
        items.clear();
        assertEquals("", PaymentParser.flattenCollection(items));
    }

    @Test
    public void AddCollectionValues_ItemsSetInInit_CorrectTotal() {
        BigDecimal total = PaymentParser.addCollectionValues(items);
        assertTrue(new BigDecimal(6).compareTo(total) == 0);
    }

    @Test
    public void AddCollectionValues_EmptyMap_CorrectTotal() {
        items.clear();
        BigDecimal total = PaymentParser.addCollectionValues(items);
        assertTrue(new BigDecimal(0).compareTo(total) == 0);
    }


}