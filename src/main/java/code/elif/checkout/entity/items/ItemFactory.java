package code.elif.checkout.entity.items;

import code.elif.checkout.dto.ItemDto;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.exception.ItemFactoryException;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;

public class ItemFactory {

    private ItemFactory() {
    }

    public static Item createItem(ItemDto itemDto) {
        if (itemDto != null) {
            if (itemDto.getItemType() == ItemType.DIGITAL) {
                return new DigitalItem(itemDto.getItemId(), itemDto.getCategoryId(), itemDto.getSellerId(),
                        new Price(itemDto.getPrice()), new Quantity(itemDto.getQuantity()));
            } else if (itemDto.getItemType() == ItemType.VAS) {
                return new VasItem(itemDto.getParentItemId(), itemDto.getItemId(), itemDto.getCategoryId(),
                        itemDto.getSellerId(), new Price(itemDto.getPrice()), new Quantity(itemDto.getQuantity()));
            } else if (itemDto.getItemType() == ItemType.DEFAULT) {
                return new DefaultItem(itemDto.getItemId(), itemDto.getCategoryId(), itemDto.getSellerId(),
                        new Price(itemDto.getPrice()), new Quantity(itemDto.getQuantity()));
            }
        }
        throw new ItemFactoryException("Unsupported item type");
    }
}



