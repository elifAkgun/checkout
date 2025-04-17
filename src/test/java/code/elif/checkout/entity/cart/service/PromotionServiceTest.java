package code.elif.checkout.entity.cart.service;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.items.DefaultItem;
import code.elif.checkout.entity.promotions.CategoryPromotion;
import code.elif.checkout.entity.promotions.Promotion;
import code.elif.checkout.entity.promotions.SameSellerPromotion;
import code.elif.checkout.entity.promotions.TotalPricePromotion;
import code.elif.checkout.service.PromotionService;
import code.elif.checkout.valueobjects.Discount;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PromotionServiceTest {

    @Test
    void givenMultiplePromotions_whenApplyBestPromotion_thenSameSellerDiscountApplied() {
        // Given
        Cart cart = new Cart();

        // Create items
        DefaultItem item1 = new DefaultItem(1, 1001, 7677, new Price(new BigDecimal("10.00")), new Quantity(3));
        DefaultItem item2 = new DefaultItem(2, 1001, 7677, new Price(new BigDecimal("90.00")), new Quantity(3));
        DefaultItem item3 = new DefaultItem(3, 1001, 7677, new Price(new BigDecimal("50.00")), new Quantity(1));
        DefaultItem item4 = new DefaultItem(4, 1003, 7678, new Price(new BigDecimal("20.00")), new Quantity(1));
        DefaultItem item5 = new DefaultItem(5, 3003, 7679, new Price(new BigDecimal("20.00")), new Quantity(1));

        // Add items to cart
        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item5);

        // Create list of promotions
        List<Promotion> promotions = new ArrayList<>();
        promotions.add(new SameSellerPromotion());
        promotions.add(new CategoryPromotion());
        promotions.add(new TotalPricePromotion());

        // Create promotion service
        PromotionService promotionService = new PromotionService(promotions);

        // When
        Discount bestPromotion = promotionService.getBestPromotion(cart);

        // Then
        assertEquals(9909, bestPromotion.getPromotionId());
        assertEquals(new BigDecimal("35.00"), bestPromotion.getValue());
    }

    @Test
    void givenMultiplePromotions_whenApplyBestPromotion_thenCategoryDiscountApplied() {
        // Given
        Cart cart = new Cart();

        // Create items
        DefaultItem item1 = new DefaultItem(1, 1001, 7677, new Price(new BigDecimal("10.00")), new Quantity(3));
        DefaultItem item2 = new DefaultItem(2, 1001, 7677, new Price(new BigDecimal("5.00")), new Quantity(3));
        DefaultItem item3 = new DefaultItem(3, 1001, 7677, new Price(new BigDecimal("20.00")), new Quantity(1));
        DefaultItem item4 = new DefaultItem(4, 1003, 7678, new Price(new BigDecimal("25.00")), new Quantity(1));
        DefaultItem item5 = new DefaultItem(5, 3003, 7679, new Price(new BigDecimal("300.00")), new Quantity(1));

        // Add items to cart
        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item5);

        // Create list of promotions
        List<Promotion> promotions = new ArrayList<>();
        promotions.add(new SameSellerPromotion());
        promotions.add(new CategoryPromotion());
        promotions.add(new TotalPricePromotion());

        // Create promotion service
        PromotionService promotionService = new PromotionService(promotions);

        // When
        Discount bestPromotion = promotionService.getBestPromotion(cart);

        // Then
        assertEquals(5676, bestPromotion.getPromotionId());
        assertEquals(new BigDecimal("15.00"), bestPromotion.getValue());
    }

    @Test
    void givenMultiplePromotions_whenApplyBestPromotion_thenTotalPriceDiscountApplied() {
        // Given
        Cart cart = new Cart();

        // Create items
        DefaultItem item1 = new DefaultItem(1, 1001, 7677, new Price(new BigDecimal("10.00")), new Quantity(3));
        DefaultItem item2 = new DefaultItem(2, 1001, 7677, new Price(new BigDecimal("5.00")), new Quantity(3));
        DefaultItem item3 = new DefaultItem(3, 1001, 7677, new Price(new BigDecimal("20.00")), new Quantity(1));
        DefaultItem item4 = new DefaultItem(4, 1003, 7678, new Price(new BigDecimal("250.00")), new Quantity(1));
        DefaultItem item5 = new DefaultItem(5, 3003, 7679, new Price(new BigDecimal("300.00")), new Quantity(1));

        // Add items to cart
        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item5);

        // Create list of promotions
        List<Promotion> promotions = new ArrayList<>();
        promotions.add(new SameSellerPromotion());
        promotions.add(new CategoryPromotion());
        promotions.add(new TotalPricePromotion());

        // Create promotion service
        PromotionService promotionService = new PromotionService(promotions);

        // When
        Discount bestPromotion = promotionService.getBestPromotion(cart);

        // Then
        assertEquals(1232, bestPromotion.getPromotionId());
        assertEquals(new BigDecimal("250.00"), bestPromotion.getValue());
    }
}