package catbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import catbot.command.Command;
import catbot.command.CommandEnum;
import catbot.task.DateTimeUtil;

public class ParserTest {
    @Test
    public void parse_todoWithDescription_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand("todo read book");

        assertEquals(CommandEnum.TODO, result.getCommandEnum());
        assertEquals(List.of("read book"), result.getArgs());
    }

    @Test
    public void parse_deadlineWithValidFormat_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand("deadline submit report /by 2024-10-01 1800");

        assertEquals(CommandEnum.DEADLINE, result.getCommandEnum());
        assertEquals(List.of("submit report", "2024-10-01 1800"), result.getArgs());
    }

    @Test
    public void parse_eventWithValidFormat_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand("event project meeting /from 2024-01-01 0900 /to 2024-01-01 1100");

        assertEquals(CommandEnum.EVENT, result.getCommandEnum());
        assertEquals(
                List.of("project meeting", "2024-01-01 0900", "2024-01-01 1100"),
                result.getArgs());
    }

    @Test
    public void parse_unknownCommand_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parseCommand("foo bar"));

        assertEquals(
            "I'm sorry, I don't understand that command. ^;w;^\n"
                + "Valid commands: todo, deadline, event, list, mark, unmark, delete, find, update, bye.",
            error.getMessage());
    }

    @Test
    public void parse_todoWithoutDescription_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parseCommand("todo"));

        assertEquals("The description of a todo cannot be empty.", error.getMessage());
    }

    @Test
    public void parse_deadlineMissingBy_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(
                CatbotException.class, () -> parser.parseCommand("deadline submit report"));

        assertEquals(
                "Invalid deadline format. Use: deadline <description> /by <" + DateTimeUtil.INPUT_PATTERN + ">",
                error.getMessage());
    }

    @Test
    public void parse_markWithNonNumericIndex_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(
                CatbotException.class, () -> parser.parseCommand("mark two"));

        assertEquals("Task number must be a valid integer.", error.getMessage());
    }

    @Test
    public void parse_findWithKeyword_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand("find book");

        assertEquals(CommandEnum.FIND, result.getCommandEnum());
        assertEquals(List.of("book"), result.getArgs());
    }

    @Test
    public void parse_findWithKeywordContainingSpaces_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand("find read book");

        assertEquals(CommandEnum.FIND, result.getCommandEnum());
        assertEquals(List.of("read book"), result.getArgs());
    }

    @Test
    public void parse_findWithoutKeyword_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parseCommand("find"));

        assertEquals("Please provide a keyword to search for.", error.getMessage());
    }

    @Test
    public void parse_findWithEmptyKeyword_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(CatbotException.class, () -> parser.parseCommand("find   "));

        assertEquals("Please provide a keyword to search for.", error.getMessage());
    }

    @Test
    public void parse_updateWithTodoDescription_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand("update 1 todo read magazine");

        assertEquals(CommandEnum.UPDATE, result.getCommandEnum());
        assertEquals(List.of("todo read magazine"), result.getArgs());
        assertEquals(1, result.getTaskIndex());
    }

    @Test
    public void parse_updateWithDeadlineDescription_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand("update 2 deadline submit report /by 2024-10-01 1800");

        assertEquals(CommandEnum.UPDATE, result.getCommandEnum());
        assertEquals(List.of("deadline submit report /by 2024-10-01 1800"), result.getArgs());
        assertEquals(2, result.getTaskIndex());
    }

    @Test
    public void parse_updateWithEventDescription_returnsTokens() throws Exception {
        Parser parser = new Parser();
        Command result = parser.parseCommand(
                "update 3 event project meeting /from 2024-01-01 0900 /to 2024-01-01 1100");

        assertEquals(CommandEnum.UPDATE, result.getCommandEnum());
        assertEquals(List.of("event project meeting /from 2024-01-01 0900 /to 2024-01-01 1100"), result.getArgs());
        assertEquals(3, result.getTaskIndex());
    }

    @Test
    public void parse_updateWithNonNumericIndex_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(
                CatbotException.class, () -> parser.parseCommand("update abc todo read magazine"));

        assertEquals("Task number must be a valid integer.", error.getMessage());
    }

    @Test
    public void parse_updateWithoutDescription_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(
                CatbotException.class, () -> parser.parseCommand("update 1"));

        assertEquals("Please provide the updated task details after the task number.", error.getMessage());
    }

    @Test
    public void parse_updateWithOnlyIndex_throwsCatbotException() {
        Parser parser = new Parser();

        CatbotException error = assertThrows(
                CatbotException.class, () -> parser.parseCommand("update 1   "));

        assertEquals("Please provide the updated task details after the task number.", error.getMessage());
    }
}
