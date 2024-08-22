package code.elif.checkout.service;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.promotions.Promotion;
import code.elif.checkout.valueobjects.Discount;

import java.math.BigDecimal;
import java.util.List;

public class PromotionService {
    private final List<Promotion> promotions;

    public PromotionService(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Discount getBestPromotion(Cart cart) {
        if (promotions == null || cart.getItems().isEmpty()) {
            return Discount.ZERO_DISCOUNT;
        }

        return promotions.stream()
                .map(promotion -> promotion.apply(cart))
                .filter(discount -> discount.getValue().compareTo(BigDecimal.ZERO) > 0)
                .max(Discount::compareTo)
                .orElse(Discount.ZERO_DISCOUNT);
    }
}
