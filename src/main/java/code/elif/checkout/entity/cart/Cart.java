package code.elif.checkout.entity.cart;

import code.elif.checkout.entity.items.DefaultItem;
import code.elif.checkout.entity.items.DigitalItem;
import code.elif.checkout.entity.items.Item;
import code.elif.checkout.entity.items.VasItem;
import code.elif.checkout.exception.CartException;
import code.elif.checkout.valueobjects.Discount;
import code.elif.checkout.valueobjects.Quantity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static code.elif.checkout.constants.CategoryConstants.*;

/**
 * Represents a shopping cart which holds items and calculates the total amount,
 * total discount, and manages item addition, removal, and validation.
 * <p>
 * The cart supports different item types such as DefaultItem, DigitalItem, and VasItem,
 * and imposes various business rules and constraints on them.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * Cart cart = new Cart();
 * Item item = new DefaultItem(1, 1001, 1, new Price(new BigDecimal("100.00")), new Quantity(1));
 * cart.addItem(item);
 * </pre>
 *
 * @see DefaultItem
 * @see DigitalItem
 * @see VasItem
 * @see Discount
 * @since 1.0
 */
@EqualsAndHashCode
@ToString
public class Cart {

    private final Map<Integer, Item> items;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal totalDiscount = BigDecimal.ZERO;
    private Integer appliedPromotionId;

    /**
     * Constructs an empty cart.
     */
    public Cart() {
        this.items = new HashMap<>();
    }

    /**
     * Adds an item to the cart with various validations based on item type.
     *
     * @param item the item to be added to the cart
     * @throws CartException if any validation fails
     */
    public void addItem(Item item) {
        if (item instanceof VasItem vasItem) {
            validateAndAddVasItem(vasItem);
        } else if (item instanceof DigitalItem digitalItem) {
            validateDigitalItem(digitalItem);
        }
        addItemToCart(item);
    }

    private void validateAndAddVasItem(VasItem vasItem) {
        Item parentItem = items.get(vasItem.getParentItemId());
        if (!(parentItem instanceof DefaultItem defaultParentItem)) {
            throw new CartException("VasItems can only be added with parent item.");
        } else {
            if (parentItem.getCategoryId() != FURNITURE_CATEGORY_ID && parentItem.getCategoryId() != ELECTRONICS_CATEGORY_ID) {
                throw new CartException("VasItem can only be added to a DefaultItem in the Furniture (1001) or Electronics (3004) categories.");
            }
            if (defaultParentItem.getVasItems().size() >= 3) {
                throw new CartException("A maximum of 3 VasItems can be added to a DefaultItem.");
            }
            if (vasItem.getPrice().getValue().compareTo(parentItem.getPrice().getValue()) > 0) {
                throw new CartException("The price of the VasItem added to the DefaultItem cannot be higher than the DefaultItem's price.");
            }
            defaultParentItem.addVasItem(vasItem);
        }
    }

    private void validateDigitalItem(DigitalItem digitalItem) {
        if (digitalItem.getCategoryId() != DIGITAL_ITEM_CATEGORY) {
            throw new CartException("DigitalItems can only have CategoryID 7889.");
        }
        if (digitalItem.getQuantity().getValue() > 5
                || (digitalItem.getQuantity().getValue() + getTotalDigitalItemCount()) > 5) {
            throw new CartException("The maximum quantity of a DigitalItem that can be added is 5.");
        }
    }

    private void addItemToCart(Item item) {
        if (items.containsKey(item.getItemId())) {
            Item existingItem = items.get(item.getItemId());
            int newQuantity = existingItem.getQuantity().getValue() + item.getQuantity().getValue();
            if (newQuantity > 10) {
                throw new CartException("The maximum quantity of an item that can be added is 10.");
            }
            existingItem.setQuantity(new Quantity(newQuantity));
        } else {
            if (getTotalQuantity() + item.getQuantity().getValue() > 30) {
                throw new CartException("The total number of products cannot exceed 30.");
            }

            BigDecimal totalPriceWithNewItem = totalAmount.add(item.getPrice().getValue());
            if (totalPriceWithNewItem.compareTo(BigDecimal.valueOf(500000)) > 0) {
                throw new CartException("The total amount of the cart cannot exceed 500,000 TL.");
            }

            totalAmount = totalAmount.add(item.getPrice().getValue());
            items.put(item.getItemId(), item);
        }
    }

    /**
     * Removes an item from the cart by its item ID.
     *
     * @param itemId the ID of the item to be removed
     * @throws CartException if the item is not found in the cart
     */
    public void removeItem(Integer itemId) {
        if (!items.containsKey(itemId)) {
            throw new CartException("There is not any item in the cart with ItemId: " + itemId);
        } else {
            Item item = items.remove(itemId);
            totalAmount = totalAmount.subtract(item.getPrice().getValue());

            if (item instanceof DefaultItem defaultItem) {
                List<VasItem> vasItems = defaultItem.getVasItems();
                for (VasItem vasItem : vasItems) {
                    totalAmount = totalAmount.subtract(vasItem.getPrice().getValue());
                    items.remove(vasItem.getItemId());
                }
            }
        }
    }

    /**
     * Resets the cart, clearing all items and resetting total amount and discount.
     */
    public void reset() {
        items.clear();
        totalAmount = BigDecimal.ZERO;
        totalDiscount = BigDecimal.ZERO;
        appliedPromotionId = null;
    }

    /**
     * Returns the items in the cart.
     *
     * @return a map of items in the cart
     */
    public Map<Integer, Item> getItems() {
        return items;
    }

    /**
     * Returns the total quantity of all items in the cart.
     *
     * @return the total quantity of items
     */
    public int getTotalQuantity() {
        return items.values().stream().mapToInt(item -> item.getQuantity().getValue()).sum();
    }

    /**
     * Returns the total amount of the cart after applying discounts.
     *
     * @return the total amount
     */
    public BigDecimal getTotalAmount() {
        return items.values().stream()
                .map(item -> item.getPrice().getValue().multiply(new BigDecimal(item.getQuantity().getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(totalDiscount);
    }

    /**
     * Returns the ID of the applied promotion.
     *
     * @return the applied promotion ID
     */
    public Integer getAppliedPromotionId() {
        return appliedPromotionId;
    }

    /**
     * Returns the total discount applied to the cart.
     *
     * @return the total discount
     */
    public BigDecimal getTotalDiscount() {
        return totalDiscount.setScale(2, RoundingMode.HALF_DOWN);
    }

    private Integer getTotalDigitalItemCount() {
        return this.items.values().stream()
                .mapToInt(item -> item.getQuantity().getValue())
                .sum();
    }

    /**
     * Applies a discount to the cart.
     *
     * @param discount the discount to be applied
     */
    public void applyPromotion(Discount discount) {
        this.totalDiscount = discount.getValue();
        this.appliedPromotionId = discount.getPromotionId();
    }
}
