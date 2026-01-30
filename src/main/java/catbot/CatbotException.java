package catbot;

/**
 * Signals an error caused by invalid user input or processing.
 */
public class CatbotException extends Exception {
    /**
     * Creates a new exception with the provided message.
     *
     * @param message Error message.
     */
    public CatbotException(String message) {
        super(message);
    }
}
