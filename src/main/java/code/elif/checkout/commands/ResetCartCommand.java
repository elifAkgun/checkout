package code.elif.checkout.commands;

import code.elif.checkout.service.CartService;

public class ResetCartCommand extends Command {
    private final CartService cartService;

    public ResetCartCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public Result execute() {
        try {
            cartService.resetCart();
        } catch (Exception ex) {
            return Result.builder()
                    .message(ex.getMessage())
                    .success(false)
                    .build();
        }
        return Result.builder()
                .message("Cart reset successfully!")
                .success(true)
                .build();
    }
}
