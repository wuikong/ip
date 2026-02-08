package catbot;

import java.util.ArrayList;
import java.util.Arrays;

import catbot.command.Command;
import catbot.command.CommandEnum;
import catbot.task.DateTimeUtil;
import catbot.task.Deadline;
import catbot.task.Event;
import catbot.task.Task;
import catbot.task.Todo;

/**
 * Parses user input into command tokens.
 */
public class Parser {
    private static final String TODO_ERROR_MSG = "The description of a todo cannot be empty.";
    private static final String DEADLINE_ERROR_MSG = "Invalid deadline format. Use: deadline <description> /by <"
            + DateTimeUtil.INPUT_PATTERN + ">";
    private static final String EVENT_ERROR_MSG = "Invalid event format. Use: event <description> /from <"
            + DateTimeUtil.INPUT_PATTERN + "> /to <" + DateTimeUtil.INPUT_PATTERN + ">";
    private static final String TASK_IDX_ERROR_MSG = "Please provide a valid task number.";
    private static final String FIND_ERROR_MSG = "Please provide a keyword to search for.";

    /**
     * Parse command from user input.
     *
     * @param input User input string.
     * @return Command instance.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseCommand(String input) throws CatbotException {
        CommandEnum cmdIdx = CommandEnum.getCommandEnum(input);
        Command cmd;
        String[] tokens = input.split(" ", 2);
        switch (cmdIdx) {
        case TODO:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException(TODO_ERROR_MSG);
            }
            cmd = new Command(cmdIdx, new ArrayList<>(Arrays.asList(tokens[1])));
            break;

        case DEADLINE:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException(DEADLINE_ERROR_MSG);
            }
            tokens = tokens[1].split(" /by ");
            if (tokens.length != 2) {
                throw new CatbotException(DEADLINE_ERROR_MSG);
            }
            cmd = new Command(cmdIdx, new ArrayList<>(Arrays.asList(tokens)));
            break;

        case EVENT:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException(EVENT_ERROR_MSG);
            }
            String eventInput = tokens[1];
            int fromIdx = eventInput.indexOf(" /from ");
            int toIdx = eventInput.indexOf(" /to ");
            if (fromIdx == -1 || toIdx == -1) {
                throw new CatbotException(EVENT_ERROR_MSG);
            }
            String description;
            String from;
            String to;
            if (fromIdx < toIdx) {
                // Order is: description /from from /to to
                description = eventInput.substring(0, fromIdx).trim();
                from = eventInput.substring(fromIdx + 7, toIdx).trim();
                to = eventInput.substring(toIdx + 5).trim();
            } else {
                // Order is: description /to to /from from
                description = eventInput.substring(0, toIdx).trim();
                to = eventInput.substring(toIdx + 5, fromIdx).trim();
                from = eventInput.substring(fromIdx + 7).trim();
            }
            cmd = new Command(cmdIdx, new ArrayList<>(Arrays.asList(description, from, to)));
            break;

        case MARK:
        case UNMARK:
        case DELETE:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException(TASK_IDX_ERROR_MSG);
            }
            try {
                cmd = new Command(cmdIdx, Integer.parseInt(tokens[1]));
            } catch (NumberFormatException e) {
                throw new CatbotException(TASK_IDX_ERROR_MSG);
            }
            break;

        case FIND:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException(FIND_ERROR_MSG);
            }
            cmd = new Command(cmdIdx, new ArrayList<>(Arrays.asList(tokens[1])));
            break;

        default:
            cmd = new Command(cmdIdx);
        }
        return cmd;
    }

    /**
     * Parse a task from a data file line.
     *
     * @param dataLine Line from the data file representing a task.
     * @return Task instance.
     * @throws CatbotException If the data line is malformed.
     */
    public Task parseDataFileTask(String dataLine) throws CatbotException {
        String[] parts = dataLine.split(" \\| ");
        if (parts.length < 3) {
            throw new CatbotException("Malformed line in data file: " + dataLine);
        }
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        Task t;
        switch (type) {
        case "T":
            t = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new CatbotException("Malformed deadline in data file: " + dataLine);
            }
            t = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts.length < 5) {
                throw new CatbotException("Malformed event in data file: " + dataLine);
            }
            t = new Event(description, parts[3], parts[4]);
            break;
        default:
            throw new CatbotException("Unknown task type in data file: " + type);
        }
        if (isDone) {
            t.mark();
        }
        return t;
    }
}
