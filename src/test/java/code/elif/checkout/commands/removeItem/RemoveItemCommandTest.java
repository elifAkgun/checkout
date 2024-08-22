package code.elif.checkout.commands.removeItem;

import code.elif.checkout.commands.RemoveItemCommand;
import code.elif.checkout.commands.Result;
import code.elif.checkout.commands.payloads.RemoveItemPayload;
import code.elif.checkout.entity.cart.service.CartService;
import code.elif.checkout.exception.CartException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemoveItemCommandTest {

    @Test
    void givenValidRemoveItemPayload_whenExecute_thenItemRemovedSuccessfully() {
        // Given
        CartService cartServiceMock = Mockito.mock(CartService.class);
        RemoveItemPayload payload = new RemoveItemPayload(1);
        RemoveItemCommand command = new RemoveItemCommand(payload, cartServiceMock);

        // When
        Result result = command.execute();

        // Then
        assertTrue(result.isSuccess());
        assertEquals("Item removed from the cart successfully!", result.getMessage());

        // Verify
        verify(cartServiceMock, times(1)).removeItem(payload.getItemId());
    }

    @Test
    void givenValidRemoveItemPayload_whenItemNotFound_thenResultShouldIndicateFailure() {
        // Given
        CartService cartServiceMock = Mockito.mock(CartService.class);
        RemoveItemPayload payload = new RemoveItemPayload(1);

        doThrow(new RuntimeException("Item with ID 1 not found in the cart."))
                .when(cartServiceMock)
                .removeItem(any());

        RemoveItemCommand command = new RemoveItemCommand(payload, cartServiceMock);

        // When
        Result result = command.execute();

        // Then
        assertFalse(result.isSuccess());
        assertEquals("Item with ID 1 not found in the cart.", result.getMessage());

        // Verify
        verify(cartServiceMock, times(1)).removeItem(payload.getItemId());
    }

    @Test
    void givenCartException_whenExecute_thenResultShouldIndicateFailure() {
        // Given
        CartService cartServiceMock = Mockito.mock(CartService.class);
        RemoveItemPayload payload = new RemoveItemPayload(1);
        Mockito.doThrow(new CartException("Cart exception occurred")).when(cartServiceMock).removeItem(payload.getItemId());

        RemoveItemCommand command = new RemoveItemCommand(payload, cartServiceMock);

        // When
        Result result = command.execute();

        // Then
        assertFalse(result.isSuccess());
        assertEquals("Cart exception occurred", result.getMessage());

        // Verify
        verify(cartServiceMock, times(1)).removeItem(payload.getItemId());
    }
}
