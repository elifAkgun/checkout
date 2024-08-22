package code.elif.checkout.commands;

import code.elif.checkout.service.CartService;
import code.elif.checkout.commands.payloads.AddItemPayload;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.dto.ItemDto;

import static code.elif.checkout.enums.ItemType.DEFAULT;
import static code.elif.checkout.enums.ItemType.DIGITAL;


public class AddItemCommand extends Command {
    private final AddItemPayload payload;
    private final CartService cartService;

    public AddItemCommand(AddItemPayload payload, CartService cartService) {
        this.payload = payload;
        this.cartService = cartService;
    }

    @Override
    public Result execute() {
        try {
            payload.validate();

            ItemDto itemDto = ItemDto.builder()
                    .itemId(payload.getItemId())
                    .sellerId(payload.getSellerId())
                    .price(payload.getPrice())
                    .categoryId(payload.getCategoryId())
                    .quantity(payload.getQuantity())
                    .itemType(getItemType())
                    .build();

            cartService.addItem(itemDto);
        } catch (Exception ex) {
            return Result.builder()
                    .message(ex.getMessage())
                    .success(false)
                    .build();
        }
        return Result.builder()
                .message("Item added to the cart successfully!")
                .success(true)
                .build();
    }

    private ItemType getItemType() {
        if (payload.getCategoryId() == 7889)
            return DIGITAL;
        else {
            return DEFAULT;
        }
    }
}

