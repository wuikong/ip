package catbot.command;

/**
 * Supported command keywords for Catbot.
 */
public enum CommandEnum {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    FIND,
    NULL;

    /**
     * Parse command type from user input.
     *
     * @param input User input string.
     * @return Command enum value.
     */
    public static CommandEnum getCommandEnum(String input) {
        input = input.trim();
        if (input.isEmpty()) {
            return NULL;
        }

        try {
            return CommandEnum.valueOf(input.split(" ")[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return NULL;
        }
    }
}
