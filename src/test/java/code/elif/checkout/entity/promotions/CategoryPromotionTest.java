package code.elif.checkout.entity.promotions;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.valueobjects.Discount;
import code.elif.checkout.entity.items.DefaultItem;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryPromotionTest {

    @Test
    void givenCartWithItemsInTargetCategory_whenApply_thenDiscountApplied() {
        // Given
        Cart cart = new Cart();
        DefaultItem item1 = new DefaultItem(1, 3003, 7677, new Price(new BigDecimal("100.00")), new Quantity(3));
        DefaultItem item2 = new DefaultItem(2, 2001, 7678, new Price(new BigDecimal("50.00")), new Quantity(2));
        cart.addItem(item1);
        cart.addItem(item2);

        Promotion categoryPromotion = new CategoryPromotion();

        // When
        Discount discount = categoryPromotion.apply(cart);

        // Then
        assertEquals(new BigDecimal("15.00"), discount.getValue());
        assertEquals(5676, discount.getPromotionId());
    }

    @Test
    void givenCartWithNoItemsInTargetCategory_whenApply_thenNoDiscount() {
        // Given
        Cart cart = new Cart();
        DefaultItem item1 = new DefaultItem(1, 2001, 7677, new Price(new BigDecimal("100.00")), new Quantity(2));
        DefaultItem item2 = new DefaultItem(2, 4002, 7678, new Price(new BigDecimal("50.00")), new Quantity(1));
        cart.addItem(item1);
        cart.addItem(item2);

        Promotion categoryPromotion = new CategoryPromotion();

        // When
        Discount discount = categoryPromotion.apply(cart);

        // Then
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertNull(discount.getPromotionId());
    }
}
