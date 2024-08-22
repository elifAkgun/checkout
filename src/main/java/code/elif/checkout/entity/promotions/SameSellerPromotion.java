package code.elif.checkout.entity.promotions;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.items.Item;
import code.elif.checkout.entity.items.VasItem;
import code.elif.checkout.valueobjects.Discount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SameSellerPromotion implements Promotion {
    private static final int PROMOTION_ID = 9909;
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    @Override
    public Discount apply(Cart cart) {
        Map<Integer, Item> items = cart.getItems();
        Map<Integer, BigDecimal> sellerTotalAmounts = new HashMap<>();
        Map<Integer, Integer> sellerItemCount = new HashMap<>();

        // Iterate through items and calculate total amount for each seller
        for (Item item : items.values()) {
            if (!(item instanceof VasItem)) {
                int sellerId = item.getSellerId();
                BigDecimal itemTotalPrice = item.getPrice().getValue().multiply(new BigDecimal(item.getQuantity().getValue()));

                // Increase the item count for the seller
                sellerItemCount.put(sellerId, sellerItemCount.getOrDefault(sellerId, 0) + 1);

                // Update total amount for the seller
                sellerTotalAmounts.put(sellerId, sellerTotalAmounts.getOrDefault(sellerId, BigDecimal.ZERO).add(itemTotalPrice));
            }
        }

        BigDecimal totalDiscount = BigDecimal.ZERO;
        boolean appliedDiscount = false;

        // Iterate through seller total amounts and apply discount if there are multiple items from the same seller
        for (Map.Entry<Integer, BigDecimal> entry : sellerTotalAmounts.entrySet()) {
            int sellerId = entry.getKey();
            BigDecimal sellerTotalAmount = entry.getValue();
            if (sellerTotalAmount.compareTo(BigDecimal.ZERO) > 0 && sellerItemCount.get(sellerId) > 1) {
                BigDecimal discountForSeller = sellerTotalAmount.multiply(DISCOUNT_RATE);
                totalDiscount = totalDiscount.add(discountForSeller);
                appliedDiscount = true;
            }
        }

        // If discount is applied, return the total discount, otherwise return zero discount
        if (appliedDiscount) {
            return new Discount(totalDiscount, PROMOTION_ID);
        } else {
            return new Discount(BigDecimal.ZERO, null);
        }
    }
}
