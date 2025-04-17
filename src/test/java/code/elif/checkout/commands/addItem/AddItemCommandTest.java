package code.elif.checkout.commands.addItem;

import code.elif.checkout.commands.AddItemCommand;
import code.elif.checkout.commands.Result;
import code.elif.checkout.commands.payloads.AddItemPayload;
import code.elif.checkout.dto.ItemDto;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddItemCommandTest {

    private AddItemCommand command;
    private CartService cartServiceMock;
    private AddItemPayload payload;

    @BeforeEach
    void setUp() {
        cartServiceMock = mock(CartService.class);
        payload = new AddItemPayload(1, 2, 3, BigDecimal.TEN, 4);
        command = new AddItemCommand(payload, cartServiceMock);
    }

    @Test
    void givenValidPayload_whenExecute_thenItemAddedToCartSuccessfully() {
        // Given
        ItemDto expectedItemDto = ItemDto.builder()
                .itemId(payload.getItemId())
                .categoryId(payload.getCategoryId())
                .sellerId(payload.getSellerId())
                .price(payload.getPrice())
                .quantity(payload.getQuantity())
                .itemType(ItemType.DEFAULT)
                .build();

        //  When
        Result result = command.execute();

        //  Then
        assertTrue(result.isSuccess());
        assertEquals("Item added to the cart successfully!", result.getMessage());
        verify(cartServiceMock, times(1)).addItem(expectedItemDto);
    }

    @Test
    void givenInvalidPayload_whenExecute_thenResultShouldIndicateFailure() {
        // Given
        doThrow(new RuntimeException("Invalid payload")).when(cartServiceMock).addItem(any());

        //  When
        Result result = command.execute();

        //  Then
        assertFalse(result.isSuccess());
        assertEquals("Invalid payload", result.getMessage());
    }
}
