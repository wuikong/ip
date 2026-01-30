package catbot;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeadlineTest {
    @Test
    public void toString_validDateTime_formatsOutput() {
        Deadline deadline = new Deadline("submit report", "2024-10-01 1800");

        String formatted = LocalDateTime.parse("2024-10-01 1800", DateTimeUtil.INPUT_FORMAT)
                .format(DateTimeUtil.OUTPUT_FORMAT);

        assertEquals("[D][ ] submit report (by: " + formatted + ")", deadline.toString());
    }

    @Test
    public void toString_invalidDateTime_usesRawInput() {
        Deadline deadline = new Deadline("submit report", "next friday");

        assertEquals("[D][ ] submit report (by: next friday)", deadline.toString());
    }

    @Test
    public void toString_afterMark_includesDoneStatus() {
        Deadline deadline = new Deadline("submit report", "2024-10-01 1800");
        deadline.mark();

        String formatted = LocalDateTime.parse("2024-10-01 1800", DateTimeUtil.INPUT_FORMAT)
                .format(DateTimeUtil.OUTPUT_FORMAT);

        assertEquals("[D][X] submit report (by: " + formatted + ")", deadline.toString());
    }
}
