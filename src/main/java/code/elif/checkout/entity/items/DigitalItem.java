package code.elif.checkout.entity.items;

import code.elif.checkout.exception.CategoryException;
import code.elif.checkout.exception.QuantityException;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;


public class DigitalItem extends Item {
    private static final int MAX_QUANTITY = 5;
    private static final int DIGITAL_CATEGORY_ID = 7889;

    public DigitalItem(Integer itemId, Integer categoryId, Integer sellerId, Price price, Quantity quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        if (categoryId != DIGITAL_CATEGORY_ID) {
            throw new CategoryException("Category ID for DigitalItem must be " + DIGITAL_CATEGORY_ID);
        }
        if (quantity.getValue() > MAX_QUANTITY) {
            throw new QuantityException("Maximum quantity for DigitalItem is " + MAX_QUANTITY);
        }
    }
}
