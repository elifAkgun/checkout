package code.elif.checkout.commands;

import code.elif.checkout.entity.cart.service.CartService;
import code.elif.checkout.commands.payloads.AddVasItemPayload;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.dto.ItemDto;


public class AddVasItemToItemCommand extends Command {
    private final AddVasItemPayload payload;
    private final CartService cartService;

    public AddVasItemToItemCommand(AddVasItemPayload payload, CartService cartService) {
        this.payload = payload;
        this.cartService = cartService;
    }

    @Override
    public Result execute() {
        try {
            ItemDto vasItemDto = ItemDto.builder()
                    .parentItemId(payload.getItemId())
                    .itemId(payload.getVasItemId())
                    .sellerId(payload.getVasSellerId())
                    .price(payload.getPrice())
                    .categoryId(payload.getVasCategoryId())
                    .quantity(payload.getQuantity())
                    .itemType(ItemType.VAS)
                    .build();

            cartService.addItem(vasItemDto);
        } catch (Exception ex) {
            return Result.builder()
                    .message(ex.getMessage())
                    .success(false)
                    .build();
        }
        return Result.builder()
                .message("VAS item added to the cart successfully!")
                .success(true)
                .build();
    }
}
