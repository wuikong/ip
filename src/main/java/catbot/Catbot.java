package catbot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import catbot.command.Command;
import catbot.command.CommandEnum;
import catbot.task.Deadline;
import catbot.task.Event;
import catbot.task.Task;
import catbot.task.Todo;

/**
 * Runs the Catbot application and dispatches user commands.
 */
public class Catbot {
    private static final String DATA_FILE = "data/catbot_data.txt";

    private Parser parser;
    private Scanner sc = new Scanner(System.in);
    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    /**
     * Constructs a Catbot instance.
     */
    public Catbot() {
        this(DATA_FILE);
    }

    /**
     * Constructs a Catbot instance with a custom data file path.
     *
     * @param dataFilePath Path to the data file.
     */
    public Catbot(String dataFilePath) {
        this.parser = new Parser();
        this.storage = new Storage(dataFilePath);
        this.taskList = new TaskList();
        this.ui = new Ui();
    }

    /**
     * Initializes Catbot by loading tasks and showing welcome message.
     */
    public void initialize() {
        try {
            this.taskList = this.storage.loadTasks(this.parser);
        } catch (FileNotFoundException e) {
            this.taskList = new TaskList();
        } catch (Exception e) {
            System.out.println(this.ui.showLoadError());
            this.taskList = new TaskList();
        }
    }

    public String showWelcome() {
        return this.ui.showWelcome();
    }

    /**
     * Creates a task based on a task command.
     *
     * @param cmd Command containing task details.
     * @return Task instance created from the command.
     * @throws CatbotException If the command is invalid for task creation.
     */
    public Task makeTask(Command cmd) throws CatbotException {
        switch (cmd.getCommandEnum()) {
        case TODO:
            return new Todo(cmd.getArgs().get(0));
        case DEADLINE:
            return new Deadline(cmd.getArgs().get(0), cmd.getArgs().get(1));
        case EVENT:
            return new Event(cmd.getArgs().get(0),
                    cmd.getArgs().get(1),
                    cmd.getArgs().get(2));
        default:
            throw new CatbotException("Invalid task command.");
        }
    }

    /**
     * Adds a task to the task list.
     *
     * @param t Task to add.
     * @return Confirmation message.
     */
    public String addTask(Task t) {
        taskList.addTask(t);
        return ui.showAddedTask(t, taskList);
    }

    /**
     * Updates a task in the task list.
     *
     * @param index   One-based index of the task to update.
     * @param cmdString Command string containing the updated task details.
     * @return Confirmation message or error message if update fails.
      */
    public String updateTask(int index, String cmdString) {
        assert(this.parser != null) : "Catbot must be initialised";
        index--; // Convert to zero-based index
        try {
            Command taskCommand = this.parser.parseCommand(cmdString);
            Task t = this.makeTask(taskCommand);
            return this.taskList.update(index, t);
        } catch (CatbotException e) {
            return this.ui.showError(e.getMessage());
        }
    }

    /**
     * Exits Catbot, saving tasks to file.
     *
     * @return Goodbye message or save error message.
     */
    public String saveAndQuit() {
        try {
            this.storage.saveTasks(this.taskList);
        } catch (IOException e) {
            return this.ui.showSaveError(this.taskList);
        }
        return this.ui.showGoodbye();
    }

    /**
     * Starts the Catbot command loop.
     */
    public static void main(String... args) {
        Catbot catbot = new Catbot();
        catbot.initialize();
        String input;
        String response = "";

        while (!response.equals(catbot.ui.showGoodbye())) {
            input = catbot.sc.nextLine();
            response = catbot.getResponse(input);
            System.out.println(response);
        }
    }

    /**
     * Gets the goodbye message.
     *
     * @return Goodbye message string.
     */
    public String getGoodbyeMessage() {
        return ui.showGoodbye();
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input User input string.
     * @return Response string from Catbot.
     */
    public String getResponse(String input) {
        assert this.parser != null : "Catbot must be initialised";
        try {
            Command cmd = this.parser.parseCommand(input);
            CommandEnum cmdIdx = cmd.getCommandEnum();
            ArrayList<String> argsList = cmd.getArgs();
            switch (cmdIdx) {
            case TODO: // fallthrough
            case DEADLINE: // fallthrough
            case EVENT:
                return this.addTask(this.makeTask(cmd));
            case LIST:
                return this.taskList.list();
            case MARK:
                return this.taskList.mark(cmd.getTaskIndex() - 1);
            case UNMARK:
                return this.taskList.unmark(cmd.getTaskIndex() - 1);
            case DELETE:
                return this.taskList.delete(cmd.getTaskIndex() - 1);
            case FIND:
                return this.taskList.find(argsList.get(0));
            case UPDATE:
                return this.updateTask(cmd.getTaskIndex(), argsList.get(0));
            case BYE:
                return this.saveAndQuit();
            default:
                throw new CatbotException("I'm sorry, I don't understand that command.");
            }
        } catch (CatbotException e) {
            return this.ui.showError(e.getMessage());
        }
    }
}
