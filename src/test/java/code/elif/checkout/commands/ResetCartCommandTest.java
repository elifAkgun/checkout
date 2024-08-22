package code.elif.checkout.commands;

import code.elif.checkout.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResetCartCommandTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private ResetCartCommand resetCartCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resetCartCommand = new ResetCartCommand(cartService);
    }

    @Test
    void whenExecute_thenCartIsResetSuccessfully() {
        //  When
        Result result = resetCartCommand.execute();

        //  Then
        assertTrue(result.isSuccess());
        assertEquals("Cart reset successfully!", result.getMessage());
        verify(cartService, times(1)).resetCart();
    }

    @Test
    void whenExecuteAndExceptionThrown_thenReturnErrorMessage() {
        // Given
        doThrow(new RuntimeException("Error resetting cart")).when(cartService).resetCart();

        //  When
        Result result = resetCartCommand.execute();

        //  Then
        assertFalse(result.isSuccess());
        assertEquals("Error resetting cart", result.getMessage());
        verify(cartService, times(1)).resetCart();
    }
}
