package code.elif.checkout.service;

import code.elif.checkout.dto.ItemDto;
import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.items.Item;
import code.elif.checkout.entity.items.ItemFactory;
import code.elif.checkout.valueobjects.Discount;

public class CartService {
    private final Cart cart;
    private final PromotionService promotionService;

    public CartService(Cart cart, PromotionService promotionService) {
        this.cart = cart;
        this.promotionService = promotionService;
    }

    public void addItem(ItemDto itemDto) {
        Item item = ItemFactory.createItem(itemDto);
        cart.addItem(item);
        applyBestPromotion();
    }

    public void removeItem(Integer itemId) {
        cart.removeItem(itemId);
        applyBestPromotion();
    }

    public void resetCart() {
        cart.reset();
    }

    public Cart getCart() {
        return cart;
    }

    private void applyBestPromotion() {
        Discount bestDiscount = promotionService.getBestPromotion(cart);
        cart.applyPromotion(bestDiscount);
    }
}
