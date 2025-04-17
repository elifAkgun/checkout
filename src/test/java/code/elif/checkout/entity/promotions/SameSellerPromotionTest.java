package code.elif.checkout.entity.promotions;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.items.DefaultItem;
import code.elif.checkout.valueobjects.Discount;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SameSellerPromotionTest {

    @Test
    void givenCartWithMultipleItemsFromSameSeller_whenApply_thenDiscountApplied() {
        // Given
        Cart cart = new Cart();
        DefaultItem item1 = new DefaultItem(1, 1001, 7677, new Price(new BigDecimal("100.00")), new Quantity(5));
        DefaultItem item2 = new DefaultItem(2, 1002, 7677, new Price(new BigDecimal("150.00")), new Quantity(2));
        DefaultItem item3 = new DefaultItem(3, 1003, 7678, new Price(new BigDecimal("50.00")), new Quantity(1));

        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);

        Promotion sameSellerPromotion = new SameSellerPromotion();

        // When
        Discount discount = sameSellerPromotion.apply(cart);

        // Then
        BigDecimal expectedDiscount = item1.getPrice().getValue().multiply(new BigDecimal(item1.getQuantity().getValue()))
                .add(item2.getPrice().getValue().multiply(new BigDecimal(item2.getQuantity().getValue())))
                .multiply(new BigDecimal("0.10"));
        assertEquals(expectedDiscount.setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertEquals(9909, discount.getPromotionId());
    }

    @Test
    void givenCartWithSingleItemFromEachSeller_whenApply_thenNoDiscount() {
        // Given
        Cart cart = new Cart();
        DefaultItem item1 = new DefaultItem(1, 1001, 7677, new Price(new BigDecimal("100.00")), new Quantity(1));
        DefaultItem item2 = new DefaultItem(2, 1002, 7678, new Price(new BigDecimal("150.00")), new Quantity(1));
        DefaultItem item3 = new DefaultItem(3, 1003, 7679, new Price(new BigDecimal("50.00")), new Quantity(1));

        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);

        Promotion sameSellerPromotion = new SameSellerPromotion();

        // When
        Discount discount = sameSellerPromotion.apply(cart);

        // Then
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertNull(discount.getPromotionId());
    }

    @Test
    void givenCartWithMultipleItemsFromDifferentSellers_whenApply_thenDiscountAppliedToCorrectSeller() {
        // Given
        Cart cart = new Cart();
        DefaultItem item1 = new DefaultItem(1, 1001, 7677, new Price(new BigDecimal("100.00")), new Quantity(5));
        DefaultItem item2 = new DefaultItem(2, 1002, 7677, new Price(new BigDecimal("150.00")), new Quantity(2));
        DefaultItem item3 = new DefaultItem(3, 1003, 7678, new Price(new BigDecimal("200.00")), new Quantity(1));
        DefaultItem item4 = new DefaultItem(4, 1004, 7678, new Price(new BigDecimal("50.00")), new Quantity(2));

        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);
        cart.addItem(item4);

        Promotion sameSellerPromotion = new SameSellerPromotion();

        // When
        Discount discount = sameSellerPromotion.apply(cart);

        // Then
        BigDecimal seller1Total = item1.getPrice().getValue().multiply(new BigDecimal(item1.getQuantity().getValue()))
                .add(item2.getPrice().getValue().multiply(new BigDecimal(item2.getQuantity().getValue())));
        BigDecimal seller2Total = item3.getPrice().getValue().multiply(new BigDecimal(item3.getQuantity().getValue()))
                .add(item4.getPrice().getValue().multiply(new BigDecimal(item4.getQuantity().getValue())));

        BigDecimal expectedDiscount = seller1Total.multiply(new BigDecimal("0.10"))
                .add(seller2Total.multiply(new BigDecimal("0.10")));
        assertEquals(expectedDiscount.setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertEquals(9909, discount.getPromotionId());
    }

    @Test
    void givenCartWithSingleItemFromOneSellerAndMultipleFromAnother_whenApply_thenDiscountAppliedToCorrectSeller() {
        // Given
        Cart cart = new Cart();
        DefaultItem item1 = new DefaultItem(1, 1001, 7677, new Price(new BigDecimal("100.00")), new Quantity(1));
        DefaultItem item2 = new DefaultItem(2, 1002, 7678, new Price(new BigDecimal("150.00")), new Quantity(2));
        DefaultItem item3 = new DefaultItem(3, 1003, 7678, new Price(new BigDecimal("200.00")), new Quantity(1));

        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);

        Promotion sameSellerPromotion = new SameSellerPromotion();

        // When
        Discount discount = sameSellerPromotion.apply(cart);

        // Then
        BigDecimal sellerTotal = item2.getPrice().getValue().multiply(new BigDecimal(item2.getQuantity().getValue()))
                .add(item3.getPrice().getValue().multiply(new BigDecimal(item3.getQuantity().getValue())));

        BigDecimal expectedDiscount = sellerTotal.multiply(new BigDecimal("0.10"));
        assertEquals(expectedDiscount.setScale(2, RoundingMode.HALF_DOWN), discount.getValue());
        assertEquals(9909, discount.getPromotionId());
    }
}
