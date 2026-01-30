package catbot;

import java.time.format.DateTimeFormatter;

/**
 * Provides shared datetime formatting utilities.
 */
public class DateTimeUtil {
    public static final String INPUT_PATTERN = "yyyy-MM-dd HHmm";
    public static final String OUTPUT_PATTERN = "MMM d yyyy HHmm";
    public static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern(INPUT_PATTERN);
    public static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern(OUTPUT_PATTERN);
}
