package code.elif.checkout.utils;

import code.elif.checkout.commands.AddItemCommand;
import code.elif.checkout.commands.AddVasItemToItemCommand;
import code.elif.checkout.commands.Command;
import code.elif.checkout.service.CartService;
import code.elif.checkout.exception.CommandException;
import com.fasterxml.jackson.core.JsonProcessingException;
import code.elif.checkout.commands.RemoveItemCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CommandParserTest {

    @Test
    void givenValidJsonString_whenParseCommand_thenReturnCorrectCommand() throws JsonProcessingException {
        // Given
        CartService cartServiceMock = mock(CartService.class);
        CommandParser parser = new CommandParser(cartServiceMock);
        String jsonString = "{\"command\":\"addItem\",\"payload\":{\"itemId\":1,\"categoryId\":1,\"sellerId\":1,\"price\":10.0,\"quantity\":1}}";

        //  When
        Command command = parser.parseCommand(jsonString);

        //  Then
        assertTrue(command instanceof AddItemCommand);
    }

    @Test
    void givenAddVasItemJsonString_whenParseCommand_thenReturnAddVasItemCommand() throws JsonProcessingException {
        // Given
        CartService cartServiceMock = mock(CartService.class);
        CommandParser parser = new CommandParser(cartServiceMock);
        String jsonString = "{\"command\":\"addVasItemToItem\",\"payload\":{\"itemId\":1,\"vasItemId\":2,\"vasSellerId\":1,\"vasCategoryId\":1,\"price\":10.0}}";

        //  When
        Command command = parser.parseCommand(jsonString);

        //  Then
        assertTrue(command instanceof AddVasItemToItemCommand);
    }

    @Test
    void givenRemoveItemJsonString_whenParseCommand_thenReturnRemoveItemCommand() throws JsonProcessingException {
        // Given
        CartService cartServiceMock = mock(CartService.class);
        CommandParser parser = new CommandParser(cartServiceMock);
        String jsonString = "{\"command\":\"removeItem\",\"payload\":{\"itemId\":1}}";

        //  When
        Command command = parser.parseCommand(jsonString);

        //  Then
        assertTrue(command instanceof RemoveItemCommand);
    }

    @Test
    void givenInvalidJsonString_whenParseCommand_thenThrowException() {
        // Given
        CartService cartServiceMock = mock(CartService.class);
        CommandParser parser = new CommandParser(cartServiceMock);
        String jsonString = "{\"command\":\"unknownCommand\",\"payload\":{}}";

        //  When & Assert
        assertThrows(CommandException.class, () -> parser.parseCommand(jsonString));
    }
}
