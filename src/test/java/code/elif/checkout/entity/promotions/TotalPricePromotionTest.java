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

 class TotalPricePromotionTest {

    @Test
     void givenCartTotalOver50000_whenApply_then2000DiscountApplied() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(1, 1001, 7677, new Price(new BigDecimal(60000)), new Quantity(1)));

        TotalPricePromotion totalPricePromotion = new TotalPricePromotion();

        // When
        Discount discount = totalPricePromotion.apply(cart);

        // Then
        assertEquals(new BigDecimal("2000").setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertEquals(1232, discount.getPromotionId());
    }

    @Test
     void givenCartTotalOver10000_whenApply_then1000DiscountApplied() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(2, 1002, 7678, new Price(new BigDecimal(15000)), new Quantity(1)));

        TotalPricePromotion totalPricePromotion = new TotalPricePromotion();

        // When
        Discount discount = totalPricePromotion.apply(cart);

        // Then
        assertEquals(new BigDecimal("1000").setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertEquals(1232, discount.getPromotionId());
    }

    @Test
     void givenCartTotalOver5000_whenApply_then500DiscountApplied() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(3, 1003, 7679, new Price(new BigDecimal(6000)), new Quantity(1)));

        TotalPricePromotion totalPricePromotion = new TotalPricePromotion();

        // When
        Discount discount = totalPricePromotion.apply(cart);

        // Then
        assertEquals(new BigDecimal("500").setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertEquals(1232, discount.getPromotionId());
    }

    @Test
     void givenCartTotalOver500_whenApply_then250DiscountApplied() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(4, 1004, 7680, new Price(new BigDecimal(600)), new Quantity(1)));

        TotalPricePromotion totalPricePromotion = new TotalPricePromotion();

        // When
        Discount discount = totalPricePromotion.apply(cart);

        // Then
        assertEquals(new BigDecimal("250").setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertEquals(1232, discount.getPromotionId());
    }

    @Test
     void givenCartTotalBelow500_whenApply_thenNoDiscountApplied() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new DefaultItem(5, 1005, 7681, new Price(new BigDecimal(400)), new Quantity(1)));

        TotalPricePromotion totalPricePromotion = new TotalPricePromotion();

        // When
        Discount discount = totalPricePromotion.apply(cart);

        // Then
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertNull(discount.getPromotionId());
    }
}
