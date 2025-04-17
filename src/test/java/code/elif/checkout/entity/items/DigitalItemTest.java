package code.elif.checkout.entity.items;

import code.elif.checkout.exception.CategoryException;
import code.elif.checkout.exception.QuantityException;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DigitalItemTest {

    @Test
    void givenValidCategoryIdAndQuantity_whenCreateDigitalItem_thenSuccess() {
        // Given
        Integer itemId = 1;
        Integer categoryId = 7889;
        Integer sellerId = 1001;
        Price price = new Price(new BigDecimal("100"));
        Quantity quantity = new Quantity(5);

        //  When
        DigitalItem digitalItem = new DigitalItem(itemId, categoryId, sellerId, price, quantity);

        //  Then
        assertEquals(itemId, digitalItem.getItemId());
        assertEquals(categoryId, digitalItem.getCategoryId());
        assertEquals(sellerId, digitalItem.getSellerId());
        assertEquals(price, digitalItem.getPrice());
        assertEquals(quantity, digitalItem.getQuantity());
    }

    @Test
    void givenInvalidCategoryId_whenCreateDigitalItem_thenThrowsCategoryException() {
        // Given
        Integer itemId = 1;
        Integer invalidCategoryId = 1234;
        Integer sellerId = 1001;
        Price price = new Price(new BigDecimal("100"));
        Quantity quantity = new Quantity(5);

        //  When & Assert
        assertThrows(CategoryException.class, () -> new DigitalItem(itemId, invalidCategoryId, sellerId, price, quantity));
    }

    @Test
    void givenQuantityExceedingMaxLimit_whenCreateDigitalItem_thenThrowsQuantityException() {
        // Given
        Integer itemId = 1;
        Integer categoryId = 7889;
        Integer sellerId = 1001;
        Price price = new Price(new BigDecimal("100"));
        Quantity quantityExceedingLimit = new Quantity(6);

        //  When & Assert
        assertThrows(QuantityException.class, () -> new DigitalItem(itemId, categoryId, sellerId, price, quantityExceedingLimit));
    }
}
