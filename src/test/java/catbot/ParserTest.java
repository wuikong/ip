package catbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void parse_todoWithDescription_returnsTokens() throws Exception {
        Parser parser = new Parser();
        List<String> result = parser.parse("todo read book");

        assertEquals(List.of("TODO", "read book"), result);
    }

    @Test
    public void parse_deadlineWithValidFormat_returnsTokens() throws Exception {
        Parser parser = new Parser();
        List<String> result = parser.parse("deadline submit report /by 2024-10-01 1800");

        assertEquals(List.of("DEADLINE", "submit report", "2024-10-01 1800"), result);
    }

    @Test
    public void parse_eventWithValidFormat_returnsTokens() throws Exception {
        Parser parser = new Parser();
        List<String> result = parser.parse("event project meeting /from 2024-01-01 0900 /to 2024-01-01 1100");

        assertEquals(
                List.of("EVENT", "project meeting", "2024-01-01 0900", "2024-01-01 1100"),
                result);
    }

    @Test
    public void parse_unknownCommand_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parse("foo bar"));

        assertEquals("I'm sorry, I don't understand that command.", error.getMessage());
    }

    @Test
    public void parse_todoWithoutDescription_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parse("todo"));

        assertEquals("The description of a todo cannot be empty.", error.getMessage());
    }

    @Test
    public void parse_deadlineMissingBy_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parse("deadline submit report"));

        assertEquals(
                "Invalid deadline format. Use: deadline <description> /by <" + DateTimeUtil.INPUT_PATTERN + ">",
                error.getMessage());
    }

    @Test
    public void parse_markWithNonNumericIndex_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parse("mark two"));

        assertEquals("Please provide a valid task number.", error.getMessage());
    }
}
