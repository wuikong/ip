package catbot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
            this.taskList = new TaskList(this.storage.loadTasks());
        } catch (FileNotFoundException e) {
            // File not found, start with empty task list
        } catch (Exception e) {
            this.ui.showLoadError();
            this.taskList = new TaskList();
        }
        this.ui.showWelcome();
    }

    /**
     * Adds a todo task.
     *
     * @param description Task description.
     * @throws CatbotException If the task cannot be created.
     */
    public void todo(String description) throws CatbotException {
        Task t = new Todo(description);
        taskList.addTask(t);
    }

    /**
     * Adds a deadline task.
     *
     * @param description Task description.
     * @param by          Due date input.
     */
    public void deadline(String description, String by) {
        Task t = new Deadline(description, by);
        taskList.addTask(t);
    }

    /**
     * Adds an event task.
     *
     * @param description Task description.
     * @param from        Start date input.
     * @param to          End date input.
     */
    public void event(String description, String from, String to) {
        Task t = new Event(description, from, to);
        taskList.addTask(t);
    }

    /**
     * Starts the Catbot command loop.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Catbot catbot = new Catbot();
        catbot.initialize();
        String input;
        while (true) {
            try {
                input = catbot.sc.nextLine();
                ArrayList<String> tokens = catbot.parser.parseInput(input);
                Command cmd = Command.valueOf(tokens.get(0));
                switch (cmd) {
                case TODO:
                    catbot.todo(tokens.get(1));
                    break;
                case DEADLINE:
                    catbot.deadline(tokens.get(1), tokens.get(2));
                    break;
                case EVENT:
                    catbot.event(tokens.get(1), tokens.get(2), tokens.get(3));
                    break;
                case LIST:
                    catbot.taskList.list();
                    break;
                case MARK:
                    catbot.taskList.mark(Integer.parseInt(tokens.get(1)) - 1);
                    break;
                case UNMARK:
                    catbot.taskList.unmark(Integer.parseInt(tokens.get(1)) - 1);
                    break;
                case DELETE:
                    catbot.taskList.delete(Integer.parseInt(tokens.get(1)) - 1);
                    break;
                case FIND:
                    catbot.taskList.find(tokens.get(1));
                    break;
                case BYE:
                    try {
                        catbot.storage.saveTasks(catbot.taskList);
                    } catch (IOException e) {
                        catbot.ui.showSaveError(catbot.taskList.toString());
                    }
                    catbot.ui.showGoodbye();
                    catbot.sc.close();
                    return;
                default:
                    throw new CatbotException("I'm sorry, I don't understand that command.");
                }
            } catch (CatbotException e) {
                catbot.ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return "Catbot heard: " + input;
    }
}
