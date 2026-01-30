import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private String from;
    private String to;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
        try {
            this.fromDateTime = LocalDateTime.parse(from, DateTimeUtil.INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            this.fromDateTime = null;
        }
        try {
            this.toDateTime = LocalDateTime.parse(to, DateTimeUtil.INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            this.toDateTime = null;
        }
    }

    @Override
    public String toString() {
        String fromOutput = from;
        if (fromDateTime != null) {
            fromOutput = fromDateTime.format(DateTimeUtil.OUTPUT_FORMAT);
        }
        String toOutput = to;
        if (toDateTime != null) {
            toOutput = toDateTime.format(DateTimeUtil.OUTPUT_FORMAT);
        }
        return "[E]" + super.toString() + " (from: " + fromOutput + " to: " + toOutput + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + from + " | " + to;
    }
}