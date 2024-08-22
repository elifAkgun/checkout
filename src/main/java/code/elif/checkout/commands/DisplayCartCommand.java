package code.elif.checkout.commands;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.service.CartService;

public class DisplayCartCommand extends Command {
    private final CartService cartService;

    public DisplayCartCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public Result execute() {
        try {
            Cart cart = cartService.getCart();
            return Result.builder()
                    .message(cart.toString())
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
