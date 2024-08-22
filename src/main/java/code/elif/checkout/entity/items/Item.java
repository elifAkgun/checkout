package code.elif.checkout.entity.items;

import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents an abstract base class for an item in the checkout system.
 * This class holds common properties such as item ID, category ID, seller ID,
 * price, and quantity, and provides basic functionality for getting and setting these properties.
 * <p>
 * Subclasses should extend this class and may provide additional properties and behavior specific to the item type.
 * </p>
 *
 * @see Price
 * @see Quantity
 * @since 1.0
 */
@ToString
@EqualsAndHashCode
public abstract class Item {
    protected Integer itemId;
    protected Integer categoryId;
    protected Integer sellerId;
    protected Price price;
    protected Quantity quantity;

    /**
     * Constructs an Item with the specified properties.
     *
     * @param itemId the ID of the item
     * @param categoryId the category ID of the item
     * @param sellerId the seller ID of the item
     * @param price the price of the item
     * @param quantity the quantity of the item
     */
    protected Item(Integer itemId, Integer categoryId, Integer sellerId, Price price, Quantity quantity) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Returns the ID of the item.
     *
     * @return the item ID
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * Returns the category ID of the item.
     *
     * @return the category ID
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Returns the seller ID of the item.
     *
     * @return the seller ID
     */
    public Integer getSellerId() {
        return sellerId;
    }

    /**
     * Returns the price of the item.
     *
     * @return the price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * Returns the quantity of the item.
     *
     * @return the quantity
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the item.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }
}
