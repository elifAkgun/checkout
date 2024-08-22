package code.elif.checkout.commands;

import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisplayCartCommandTest {

    @Mock
    private CartService cartService;

    @Mock
    private Cart cart;

    @InjectMocks
    private DisplayCartCommand displayCartCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        displayCartCommand = new DisplayCartCommand(cartService);
    }

    @Test
    void whenExecute_thenReturnCartDetails() {
        // Given
        when(cartService.getCart()).thenReturn(cart);
        when(cart.toString()).thenReturn("Cart details");

        //  When
        Result result = displayCartCommand.execute();

        //  Then
        assertTrue(result.isSuccess());
        assertEquals("Cart details", result.getMessage());
        verify(cartService, times(1)).getCart();
    }

    @Test
    void whenExecuteAndExceptionThrown_thenReturnErrorMessage() {
        // Given
        when(cartService.getCart()).thenThrow(new RuntimeException("Error retrieving cart"));

        //  When
        Result result = displayCartCommand.execute();

        //  Then
        assertFalse(result.isSuccess());
        assertEquals("Error retrieving cart", result.getMessage());
        verify(cartService, times(1)).getCart();
    }
}
