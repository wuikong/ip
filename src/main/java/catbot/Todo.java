package catbot;

/**
 * Represents a simple todo task.
 */
public class Todo extends Task {
    /**
     * Creates a todo task.
     *
     * @param description Task description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    /**
     * Returns a user-friendly string representation.
     *
     * @return Display string.
     */
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    /**
     * Returns a string representation for storage.
     *
     * @return Storage string.
     */
    public String toDataString() {
        return "T | " + super.toDataString();
    }
}
