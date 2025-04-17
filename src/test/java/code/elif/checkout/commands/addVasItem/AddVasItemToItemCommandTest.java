package code.elif.checkout.commands.addVasItem;

import code.elif.checkout.commands.AddVasItemToItemCommand;
import code.elif.checkout.commands.Result;
import code.elif.checkout.commands.payloads.AddVasItemPayload;
import code.elif.checkout.dto.ItemDto;
import code.elif.checkout.enums.ItemType;
import code.elif.checkout.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddVasItemToItemCommandTest {

    private AddVasItemToItemCommand command;
    private CartService cartServiceMock;
    private AddVasItemPayload payload;

    @BeforeEach
    void setUp() {
        cartServiceMock = mock(CartService.class);
        payload = new AddVasItemPayload(1, 2, 3, 4, BigDecimal.TEN, 5);
        command = new AddVasItemToItemCommand(payload, cartServiceMock);
    }

    @Test
    void givenValidPayload_whenExecute_thenItemAddedToCartSuccessfully() {
        // Given
        ItemDto expectedVasItemDto = ItemDto.builder()
                .parentItemId(payload.getItemId())
                .itemId(payload.getVasItemId())
                .sellerId(payload.getVasSellerId())
                .price(payload.getPrice())
                .categoryId(payload.getVasCategoryId())
                .quantity(payload.getQuantity())
                .itemType(ItemType.VAS)
                .build();

        //  When
        Result result = command.execute();

        //  Then
        assertEquals("VAS item added to the cart successfully!", result.getMessage());
        assertTrue(result.isSuccess());
        verify(cartServiceMock, times(1)).addItem(expectedVasItemDto);
    }

    @Test
    void givenExceptionThrown_whenExecute_thenResultShouldIndicateFailure() {
        // Given
        doThrow(new RuntimeException("Failed to add item")).when(cartServiceMock).addItem(any());

        //  When
        Result result = command.execute();

        //  Then
        assertEquals("Failed to add item", result.getMessage());
        assertFalse(result.isSuccess());
    }
}
