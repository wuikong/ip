package catbot;

/**
 * Supported command keywords for Catbot.
 */
public enum Command {
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
     * Parse command from user input.
     *
     * @param input User input string.
     * @return Command enum value.
     */
    public static Command parseCommand(String input) {
        input = input.trim();
        if (input.isEmpty()) {
            return NULL;
        }

        try {
            return Command.valueOf(input.split(" ")[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return NULL;
        }
    }
}
