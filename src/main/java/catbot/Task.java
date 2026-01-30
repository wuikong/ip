package catbot;

/**
 * Represents a task with a description and completion state.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description Task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    @Override
    /**
     * Returns a user-friendly string representation.
     *
     * @return Display string.
     */
    public String toString() {
        String checkbox = "[" + (isDone ? "X" : " ") + "] ";
        return checkbox + description;
    }

    /**
     * Returns a string representation for storage.
     *
     * @return Storage string.
     */
    public String toDataString() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
