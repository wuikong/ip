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
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toDataString() {
        return "T | " + super.toDataString();
    }
}
