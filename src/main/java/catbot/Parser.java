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
    private static final String UPDATE_ERROR_MSG = "Please provide the updated task details after the task number.";
    private static final String ILLEGAL_CHAR_MSG = "Input contains illegal characters. Please avoid using '|'.";

    /**
     * Parses user input into a todo command.
     *
     * @param input User input string.
     * @return Command instance for todo.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseTodoInput(String input) throws CatbotException {
        if (input.contains(" \\| ")) {
            throw new CatbotException(ILLEGAL_CHAR_MSG);
        }
        String description = input.substring(CommandEnum.TODO.name().length()).trim();
        if (description.isEmpty()) {
            throw new CatbotException(TODO_ERROR_MSG);
        }
        return new Command(CommandEnum.TODO, new ArrayList<>(Arrays.asList(description)));
    }

    /**
     * Parses user input into a deadline command.
     *
     * @param input User input string.
     * @return Command instance for deadline.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseDeadlineInput(String input) throws CatbotException {
        if (input.contains(" \\| ")) {
            throw new CatbotException(ILLEGAL_CHAR_MSG);
        }
        String[] tokens = input.split(" /by ");
        if (tokens.length != 2) {
            throw new CatbotException(DEADLINE_ERROR_MSG);
        }
        String description = tokens[0].substring(CommandEnum.DEADLINE.name().length()).trim();
        String by = tokens[1].trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new CatbotException(DEADLINE_ERROR_MSG);
        }
        return new Command(CommandEnum.DEADLINE, new ArrayList<>(Arrays.asList(description, by)));
    }

    /**
     * Parses user input into an event command.
     *
     * @param input User input string.
     * @return Command instance for event.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseEventInput(String input) throws CatbotException {
        if (input.contains(" \\| ")) {
            throw new CatbotException(ILLEGAL_CHAR_MSG);
        }
        String fromFlag = " /from ";
        String toFlag = " /to ";

        int fromIdx = input.indexOf(fromFlag);
        int toIdx = input.indexOf(toFlag);
        if (fromIdx == -1 || toIdx == -1) {
            throw new CatbotException(EVENT_ERROR_MSG);
        }

        String description;
        String from;
        String to;
        if (fromIdx < toIdx) {
            // Order is: description /from from /to to
            description = input.substring(CommandEnum.EVENT.name().length(), fromIdx).trim();
            from = input.substring(fromIdx + fromFlag.length(), toIdx).trim();
            to = input.substring(toIdx + toFlag.length()).trim();
        } else {
            // Order is: description /to to /from from
            description = input.substring(CommandEnum.EVENT.name().length(), toIdx).trim();
            to = input.substring(toIdx + toFlag.length(), fromIdx).trim();
            from = input.substring(fromIdx + fromFlag.length()).trim();
        }

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new CatbotException(EVENT_ERROR_MSG);
        }
        return new Command(CommandEnum.EVENT, new ArrayList<>(Arrays.asList(description, from, to)));
    }

    /**
     * Parses commands that require a numeric task index.
     *
     * @param cmdIdx CommandEnum value.
     * @param input  User input string.
     * @return Command instance.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseNumericInput(CommandEnum cmdIdx, String input) throws CatbotException {
        int cmdIdxLength = cmdIdx.name().length();
        try {
            return new Command(cmdIdx, Integer.parseInt(input.substring(cmdIdxLength).trim()));
        } catch (NumberFormatException e) {
            throw new CatbotException(TASK_IDX_ERROR_MSG);
        }
    }

    /**
     * Parses user input into a find command.
     *
     * @param input User input string.
     * @return Command instance for find.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseFindInput(String input) throws CatbotException {
        String target = input.substring(CommandEnum.FIND.name().length()).trim();
        if (target.isEmpty()) {
            throw new CatbotException(FIND_ERROR_MSG);
        }
        return new Command(CommandEnum.FIND, new ArrayList<>(Arrays.asList(target)));
    }

    /**
     * Parses user input into an update command.
     *
     * @param input User input string.
     * @return Command instance for update.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseUpdateInput(String input) throws CatbotException {
        String updateInput = input.substring(CommandEnum.UPDATE.name().length()).trim();
        String idxStr = updateInput.split(" ")[0];
        int idx;
        try {
            idx = Integer.parseInt(idxStr);
        } catch (NumberFormatException e) {
            throw new CatbotException(TASK_IDX_ERROR_MSG);
        }
        String restOfInput = updateInput.substring(idxStr.length()).trim();
        if (restOfInput.isEmpty()) {
            throw new CatbotException(UPDATE_ERROR_MSG);
        }
        return new Command(CommandEnum.UPDATE, new ArrayList<>(Arrays.asList(restOfInput)), idx);
    }

    /**
     * Parses user input into a Command instance.
     *
     * @param input User input string.
     * @return Command instance.
     * @throws CatbotException If the input is invalid.
     */
    public Command parseCommand(String input) throws CatbotException {
        CommandEnum cmdIdx = CommandEnum.getCommandEnum(input);
        switch (cmdIdx) {
        case TODO:
            return this.parseTodoInput(input);
        case DEADLINE:
            return this.parseDeadlineInput(input);
        case EVENT:
            return this.parseEventInput(input);
        case MARK: // fallthrough
        case UNMARK: // fallthrough
        case DELETE:
            return this.parseNumericInput(cmdIdx, input);
        case FIND:
            return this.parseFindInput(input);
        case BYE: // fallthrough
        case LIST:
            return new Command(cmdIdx);
        case UPDATE:
            return this.parseUpdateInput(input);
        default:
            throw new CatbotException(
                "I'm sorry, I don't understand that command.\n"
                + "Valid commands: todo, deadline, event, list, mark, unmark, delete, find, update, bye."
            );
        }
    }

    /**
     * Parses a data file line into a Task instance.
     *
     * @param dataLine Line from the data file representing a task.
     * @return Task instance.
     * @throws CatbotException If the data line is malformed.
     */
    public Task parseDataFileTask(String dataLine) throws CatbotException {
        assert dataLine != null : "Data line cannot be null";
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
