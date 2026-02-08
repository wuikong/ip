package catbot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a due date.
 */
public class Deadline extends Task {
    private String by;
    private LocalDateTime byDateTime;

    /**
     * Creates a deadline task.
     *
     * @param description Task description.
     * @param by Due date input.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        try {
            this.byDateTime = LocalDateTime.parse(by, DateTimeUtil.INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            this.byDateTime = null;
        }
    }

    @Override
    public String toString() {
        if (byDateTime == null) {
            return "[D]" + super.toString() + " (by: " + by + ")";
        }
        return "[D]" + super.toString() + " (by: " + byDateTime.format(DateTimeUtil.OUTPUT_FORMAT) + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + super.toDataString() + " | " + by;
    }
}
