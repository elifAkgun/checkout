package code.elif.checkout.utils;

import code.elif.checkout.commands.*;
import code.elif.checkout.commands.payloads.AbstractPayload;
import code.elif.checkout.commands.payloads.AddItemPayload;
import code.elif.checkout.commands.payloads.AddVasItemPayload;
import code.elif.checkout.commands.payloads.RemoveItemPayload;
import code.elif.checkout.service.CartService;
import code.elif.checkout.enums.CommandType;
import code.elif.checkout.exception.CommandException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public class CommandParser {

    public static final String PAYLOAD = "payload";
    public static final String COMMAND = "command";
    public static final String COMMAND_IS_NOT_VALID = "Command is not valid";
    public static final String UNKNOWN_COMMAND = "Unknown command: ";
    private final CartService cartService;

    public CommandParser(CartService cartService) {
        this.cartService = cartService;
    }

    public Command parseCommand(String jsonString) throws JsonProcessingException {
        if (jsonString.isBlank()) {
            throw new CommandException(COMMAND_IS_NOT_VALID);
        }

        JSONObject jsonObject = new JSONObject(jsonString);
        String commandName = jsonObject.getString(COMMAND);
        CommandType commandType = CommandType.fromString(commandName);

        switch (commandType) {
            case ADD_ITEM -> {
                AddItemPayload addItemPayload = getPayload(jsonString, AddItemPayload.class);
                return new AddItemCommand(addItemPayload, cartService);
            }
            case ADD_VAS_ITEM_TO_ITEM -> {
                AddVasItemPayload addVasItemPayload = getPayload(jsonString, AddVasItemPayload.class);
                return new AddVasItemToItemCommand(addVasItemPayload, cartService);
            }
            case REMOVE_ITEM -> {
                RemoveItemPayload removeItemPayload = getPayload(jsonString, RemoveItemPayload.class);
                return new RemoveItemCommand(removeItemPayload, cartService);
            }
            case RESET_CART -> {
                return new ResetCartCommand(cartService);
            }
            case DISPLAY_CART -> {
                return new DisplayCartCommand(cartService);
            }
            default -> throw new CommandException(UNKNOWN_COMMAND + commandName);
        }
    }

    private static <T extends AbstractPayload> T getPayload(String jsonString, Class<T> valueType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);
        JsonNode payloadNode = rootNode.path(PAYLOAD);
        return mapper.readValue(payloadNode.toString(), valueType);
    }
}


