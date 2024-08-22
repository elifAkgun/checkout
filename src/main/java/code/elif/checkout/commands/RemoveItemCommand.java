package code.elif.checkout.commands;

import code.elif.checkout.commands.payloads.RemoveItemPayload;
import code.elif.checkout.entity.cart.service.CartService;

public class RemoveItemCommand extends Command {
    private final CartService cartService;
    private final RemoveItemPayload payload;

    public RemoveItemCommand(RemoveItemPayload payload, CartService cartService) {
        this.cartService = cartService;
        this.payload = payload;
    }

    @Override
    public Result execute() {
        try {
            cartService.removeItem(payload.getItemId());
            return Result.builder()
                    .message("Item removed from the cart successfully!")
                    .success(true)
                    .build();
        } catch (Exception ex) {
            return Result.builder()
                    .message(ex.getMessage())
                    .success(false)
                    .build();
        }
    }
}

