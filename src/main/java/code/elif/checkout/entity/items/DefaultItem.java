package code.elif.checkout.entity.items;

import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import code.elif.checkout.exception.DefaultItemException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DefaultItem extends Item {

    public static final String MAXIMUM_QUANTITY_FOR_DEFAULT_ITEM_IS_10 = "Maximum quantity for DefaultItem is 10";
    private final List<VasItem> vasItems;

    public DefaultItem(Integer itemId, Integer categoryId, Integer sellerId, Price price, Quantity quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        if (quantity.getValue() > 10) {
            throw new DefaultItemException(MAXIMUM_QUANTITY_FOR_DEFAULT_ITEM_IS_10);
        }
        vasItems = new ArrayList<>();
    }

    public void addVasItem(VasItem vasItem) {
        vasItems.add(vasItem);
    }

    public List<VasItem> getVasItems() {
        return vasItems;
    }

}
