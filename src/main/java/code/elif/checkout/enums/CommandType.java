package code.elif.checkout.enums;

import code.elif.checkout.exception.CommandException;

public enum CommandType {
    ADD_ITEM("addItem"),
    ADD_VAS_ITEM_TO_ITEM("addVasItemToItem"),
    REMOVE_ITEM("removeItem"),
    RESET_CART("resetCart"),
    DISPLAY_CART("displayCart");

    private final String commandName;

    CommandType(String commandName) {
        this.commandName = commandName;
    }

    public static CommandType fromString(String name) {
        for (CommandType type : CommandType.values()) {
            if (type.commandName.equals(name)) {
                return type;
            }
        }
        throw new CommandException("Unknown command: " + name);
    }
}