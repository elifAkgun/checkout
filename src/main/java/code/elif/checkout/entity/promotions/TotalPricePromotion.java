package code.elif.checkout.entity.promotions;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.valueobjects.Discount;

import java.math.BigDecimal;

public class TotalPricePromotion implements Promotion {
    private static final int PROMOTION_ID = 1232;

    @Override
    public Discount apply(Cart cart) {
        BigDecimal totalAmount = cart.getTotalAmount();
        if (totalAmount.compareTo(new BigDecimal("50000")) >= 0) {
            return new Discount(new BigDecimal("2000"), PROMOTION_ID);
        } else if (totalAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return new Discount(new BigDecimal("1000"), PROMOTION_ID);
        } else if (totalAmount.compareTo(new BigDecimal("5000")) >= 0) {
            return new Discount(new BigDecimal("500"), PROMOTION_ID);
        } else if (totalAmount.compareTo(new BigDecimal("500")) >= 0) {
            return new Discount(new BigDecimal("250"), PROMOTION_ID);
        }

        return new Discount(BigDecimal.ZERO, null);
    }
}
