package code.elif.checkout.entity.promotions;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.items.Item;
import code.elif.checkout.valueobjects.Discount;

import java.math.BigDecimal;

public class CategoryPromotion implements Promotion {
    private static final int PROMOTION_ID = 5676;
    private static final int TARGET_CATEGORY_ID = 3003;
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.05");

    @Override
    public Discount apply(Cart cart) {
        BigDecimal discount = BigDecimal.ZERO;
        for (Item item : cart.getItems().values()) {
            if (item.getCategoryId().equals(TARGET_CATEGORY_ID)) {
                BigDecimal itemDiscount = item.getPrice().getValue().multiply(DISCOUNT_RATE)
                        .multiply(new BigDecimal(item.getQuantity().getValue()));
                discount = discount.add(itemDiscount);
            }
        }
        return BigDecimal.ZERO.compareTo(discount) < 0 ?
                new Discount(discount, PROMOTION_ID) : Discount.ZERO_DISCOUNT;
    }
}
