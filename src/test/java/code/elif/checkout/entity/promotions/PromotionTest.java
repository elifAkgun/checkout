package code.elif.checkout.entity.promotions;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.cart.service.CartService;
import code.elif.checkout.entity.cart.service.PromotionService;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.valueobjects.Discount;
import code.elif.checkout.dto.ItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionTest {

    @Mock
    private Cart cartMock;

    @Mock
    private PromotionService promotionServiceMock;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenCartWithCategoryPromotionItems_whenApplyCategoryPromotion_thenAppliesDiscount() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(3003)
                .sellerId(123)
                .price(new BigDecimal("100.00"))
                .quantity(1)
                .itemType(ItemType.DEFAULT)
                .build();


        // Mocking promotion service to apply a 5% discount
        when(promotionServiceMock.getBestPromotion(cartMock))
                .thenAnswer(invocation -> {
                    Cart cart = invocation.getArgument(0);
                    when(cart.getTotalAmount())
                            .thenReturn(new BigDecimal("95.00")); // 5% discount applied
                    return new Discount(BigDecimal.valueOf(100), 2);
                });

        // When
        cartService.addItem(itemDto);

        // Then
        assertEquals(new BigDecimal("95.00"), cartService.getCart().getTotalAmount());
        verify(promotionServiceMock).getBestPromotion(cartMock);
    }

    @Test
    void givenCartWithSameSellerItems_whenApplySameSellerPromotion_thenAppliesDiscount() {
        // Given
        ItemDto itemDto1 = ItemDto.builder()
                .itemId(1)
                .categoryId(1001)
                .sellerId(123)
                .price(new BigDecimal("100.00"))
                .quantity(1)
                .itemType(ItemType.DEFAULT)
                .build();

        ItemDto itemDto2 = ItemDto.builder()
                .itemId(2)
                .categoryId(1001)
                .sellerId(123)
                .price(new BigDecimal("200.00"))
                .quantity(1)
                .itemType(ItemType.DEFAULT)
                .build();

        // Mocking promotion service to apply a 10% discount
        when(promotionServiceMock.getBestPromotion(cartMock))
                .thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            when(cart.getTotalAmount()).thenReturn(new BigDecimal("270.00")); // 10% discount applied
            return new Discount(BigDecimal.valueOf(30), 2);
        });

        // When
        cartService.addItem(itemDto1);
        cartService.addItem(itemDto2);

        // Then
        assertEquals(new BigDecimal("270.00"), cartService.getCart().getTotalAmount());
        verify(promotionServiceMock, times(2)).getBestPromotion(cartMock);
    }

    @Test
    void givenCartWithMultiplePromotions_whenApplyPromotions_thenAppliesMostAdvantageousPromotion() {
        // Given
        ItemDto itemDto = ItemDto.builder()
                .itemId(1)
                .categoryId(3003)
                .sellerId(123)
                .price(new BigDecimal("600.00"))
                .quantity(1)
                .itemType(ItemType.DEFAULT)
                .build();

        // Mocking promotion service to apply a discount for price range 500-5000 TL
        when(promotionServiceMock.getBestPromotion(cartMock))
                .thenAnswer(invocation -> {
                    Cart cart = invocation.getArgument(0);
                    when(cart.getTotalAmount()).thenReturn(new BigDecimal("350.00")); // 250 TL discount applied
                    return new Discount(BigDecimal.valueOf(250), 2);
                });

        // When
        cartService.addItem(itemDto);

        // Then
        assertEquals(new BigDecimal("350.00"), cartService.getCart().getTotalAmount());
        verify(promotionServiceMock).getBestPromotion(cartMock);
    }
}