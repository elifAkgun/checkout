package code.elif.checkout.entity.promotions;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.valueobjects.Discount;

/**
 * Represents a promotion that can be applied to a shopping cart.
 * <p>
 * Implementations of this interface define specific promotion rules
 * and the logic to calculate the discount based on the contents of the cart.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * public class CategoryPromotion implements Promotion {
 *     {@literal @}Override
 *     public Discount apply(Cart cart) {
 *         // Promotion logic to calculate discount
 *         return new Discount(...);
 *     }
 * }
 * </pre>
 *
 * @see Discount
 * @see Cart
 * @since 1.0
 */
public interface Promotion {

    /**
     * Applies the promotion to the given shopping cart.
     * <p>
     * This method calculates the discount based on the specific promotion rules
     * and the contents of the cart.
     * </p>
     *
     * @param cart the shopping cart to which the promotion will be applied
     * @return the discount resulting from applying the promotion
     */
    Discount apply(Cart cart);
}
