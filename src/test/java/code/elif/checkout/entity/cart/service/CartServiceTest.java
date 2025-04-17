package code.elif.checkout.entity.cart.service;

import code.elif.checkout.dto.ItemDto;
import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.items.DefaultItem;
import code.elif.checkout.entity.items.Item;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.exception.CartException;
import code.elif.checkout.exception.DefaultItemException;
import code.elif.checkout.service.CartService;
import code.elif.checkout.service.PromotionService;
import code.elif.checkout.valueobjects.Discount;
import code.elif.checkout.valueobjects.Price;
import code.elif.checkout.valueobjects.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private CartService cartService;

    private Cart cart;

    @BeforeEach
    public void setup() {
        try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            cart = spy(new Cart());  // Using spy to verify interactions with Cart
            cartService = new CartService(cart, promotionService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void givenNewItem_whenAddItem_thenItemShouldBeAddedToCart() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(1001)
                .sellerId(123)
                .price(new BigDecimal("50.00"))
                .quantity(1)
                .itemType(ItemType.DEFAULT)
                .build();

        Discount totalPriceDiscount = new Discount(BigDecimal.valueOf(100), 2);  // Assume 10% discount for the total price
        when(promotionService.getBestPromotion(cart)).thenReturn(totalPriceDiscount);

        // When
        cartService.addItem(itemDto);

        // Then
        verify(cart, times(1)).addItem(any(Item.class));
        verify(promotionService, times(1)).getBestPromotion(cart);
    }

    @Test
    void givenInvalidItem_whenAddItem_thenItemShouldNotBeAddedToCart() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(1001)
                .sellerId(123)
                .price(new BigDecimal("50.00"))
                .quantity(11)
                .itemType(ItemType.DEFAULT)
                .build();

        // When
        DefaultItemException exception = assertThrows(DefaultItemException.class, () -> cartService.addItem(itemDto));

        // Then
        assertEquals(DefaultItem.MAXIMUM_QUANTITY_FOR_DEFAULT_ITEM_IS_10, exception.getMessage());
        verify(cart, times(0)).addItem(any(Item.class));
        verify(promotionService, times(0)).getBestPromotion(cart);
    }

    @Test
    void givenExistingItem_whenRemoveItem_thenItemShouldBeRemovedFromCart() {
        // Given
        int itemId = 1;
        DefaultItem item = new DefaultItem(itemId, 1001, 123, new Price(BigDecimal.valueOf(50)), new Quantity(1));
        cart.addItem(item);

        Discount totalPriceDiscount = new Discount(BigDecimal.valueOf(100), 2);  // Assume 10% discount for the total price
        when(promotionService.getBestPromotion(cart)).thenReturn(totalPriceDiscount);

        // When
        cartService.removeItem(itemId);

        // Then
        verify(cart, times(1)).removeItem(itemId);
        verify(promotionService, times(1)).getBestPromotion(cart);
    }

    @Test
    void givenNonExistingItem_whenRemoveItem_thenShouldThrowException() {
        // Given
        int itemId = 1;
        doThrow(new CartException("There is not any item in the cart with ItemId: " + itemId))
                .when(cart).removeItem(itemId);

        // When & Then
        CartException exception = assertThrows(CartException.class, () -> cartService.removeItem(itemId));
        assertEquals("There is not any item in the cart with ItemId: " + itemId, exception.getMessage());
        verify(promotionService, times(0)).getBestPromotion(cart);
    }

    @Test
    void givenItemsInCart_whenResetCart_thenCartShouldBeEmpty() {
        // When
        cartService.resetCart();

        // Then
        verify(cart, times(1)).reset();
        verify(promotionService, times(0)).getBestPromotion(cart);
    }

    @Test
    void whenGetCart_thenCartShouldBeReturned() {
        // When
        Cart result = cartService.getCart();

        // Then
        assertEquals(cart, result);
    }

    @Test
    void givenMultiplePromotions_whenGetBestPromotion_thenReturnCategoryDiscount() {
        // Given
        DefaultItem item = new DefaultItem(1, 1001, 123, new Price(BigDecimal.valueOf(100)), new Quantity(1));

        Discount categoryDiscount = new Discount(BigDecimal.valueOf(20), 1);  // Assume 20% discount for the category
        when(promotionService.getBestPromotion(cart)).thenReturn(categoryDiscount);

        //  When
        cartService.addItem(ItemDto.builder()
                .itemId(item.getItemId())
                .categoryId(item.getCategoryId())
                .sellerId(item.getSellerId())
                .price(item.getPrice().getValue())
                .quantity(item.getQuantity().getValue())
                .itemType(ItemType.DEFAULT)
                .build());

        //  Then
        verify(promotionService, times(1)).getBestPromotion(cart);
        assertEquals(BigDecimal.valueOf(80).setScale(2, RoundingMode.HALF_DOWN), cart.getTotalAmount());
        assertEquals(categoryDiscount.getValue(), cart.getTotalDiscount());
        assertEquals(categoryDiscount.getPromotionId(), cart.getAppliedPromotionId());

    }

    @Test
    void givenMultiplePromotions_whengetBestPromotion_thenTotalPriceDiscountApplied() {
        // Given
        DefaultItem item = new DefaultItem(1, 1001, 123, new Price(BigDecimal.valueOf(1000)), new Quantity(1));

        Discount totalPriceDiscount = new Discount(BigDecimal.valueOf(100), 2);  // Assume 10% discount for the total price
        when(promotionService.getBestPromotion(cart)).thenReturn(totalPriceDiscount);

        //  When
        cartService.addItem(ItemDto.builder()
                .itemId(item.getItemId())
                .categoryId(item.getCategoryId())
                .sellerId(item.getSellerId())
                .price(item.getPrice().getValue())
                .quantity(item.getQuantity().getValue())
                .itemType(ItemType.DEFAULT)
                .build());

        //  Then
        verify(promotionService, times(1)).getBestPromotion(cart);
        assertEquals(BigDecimal.valueOf(900).setScale(2, RoundingMode.HALF_DOWN), cart.getTotalAmount());
        assertEquals(totalPriceDiscount.getValue(), cart.getTotalDiscount());
        assertEquals(totalPriceDiscount.getPromotionId(), cart.getAppliedPromotionId());
    }

    @Test
    void givenMultiplePromotions_whengetBestPromotion_thenSameSellerDiscountApplied() {
        // Given
        DefaultItem item = new DefaultItem(1, 1001, 123, new Price(BigDecimal.valueOf(100)), new Quantity(1));

        Discount sameSellerDiscount = new Discount(BigDecimal.valueOf(15), 3);  // Assume 15% discount for the same seller
        when(promotionService.getBestPromotion(cart)).thenReturn(sameSellerDiscount);

        //  When
        cartService.addItem(ItemDto.builder()
                .itemId(item.getItemId())
                .categoryId(item.getCategoryId())
                .sellerId(item.getSellerId())
                .price(item.getPrice().getValue())
                .quantity(item.getQuantity().getValue())
                .itemType(ItemType.DEFAULT)
                .build());

        //  Then
        verify(promotionService, times(1)).getBestPromotion(cart);
        assertEquals(BigDecimal.valueOf(85).setScale(2, RoundingMode.HALF_DOWN), cart.getTotalAmount());
        assertEquals(sameSellerDiscount.getValue(), cart.getTotalDiscount());
        assertEquals(sameSellerDiscount.getPromotionId(), cart.getAppliedPromotionId());

    }
}
