package code.elif.checkout.entity.items;

import code.elif.checkout.exception.VasItemException;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class VasItem extends Item {
    public static final int VAS_ITEM_MAX_QUANTITY = 3;
    private static final int VAS_CATEGORY_ID = 3242;
    private static final int VAS_SELLER_ID = 5003;
    private final Integer parentItemId;

    public VasItem(Integer parentItemId, Integer vasItemId, Integer vasCategoryId, Integer vasSellerId, Price price, Quantity quantity) {
        super(vasItemId, vasCategoryId, vasSellerId, price, quantity);
        this.parentItemId = parentItemId;
        if (categoryId != VAS_CATEGORY_ID) {
            throw new VasItemException("Category ID for VasItem must be " + VAS_CATEGORY_ID);
        }
        if (sellerId != VAS_SELLER_ID) {
            throw new VasItemException("Seller ID for VasItem must be " + VAS_SELLER_ID);
        }
        if (quantity.getValue() > VAS_ITEM_MAX_QUANTITY) {
            throw new VasItemException("Maximum quantity for VasItem is 3");
        }
    }
}
