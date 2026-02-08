package catbot;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        System.out.println(this.ui.showWelcome());
    }

    /**
     * Adds a todo task.
     *
     * @param description Task description.
     * @throws CatbotException If the task cannot be created.
     */
    public String addTodo(String description) throws CatbotException {
        Task t = new Todo(description);
        taskList.addTask(t);
        return ui.showAddedTask(t, taskList);
    }

    /**
     * Adds a deadline task.
     *
     * @param description Task description.
     * @param by          Due date input.
     */
    public String addDeadline(String description, String by) {
        Task t = new Deadline(description, by);
        taskList.addTask(t);
        return ui.showAddedTask(t, taskList);
    }

    /**
     * Adds an event task.
     *
     * @param description Task description.
     * @param from        Start date input.
     * @param to          End date input.
     */
    public String addEvent(String description, String from, String to) {
        Task t = new Event(description, from, to);
        taskList.addTask(t);
        return ui.showAddedTask(t, taskList);
    }

    /**
     * Starts the Catbot command loop.
     */
    public static void main(String[] args) {
        Catbot catbot = new Catbot();
        catbot.initialize();
        String input;
        while (true) {
            try {
                input = catbot.sc.nextLine();
                Command tokens = catbot.parser.parseCommand(input);
                CommandEnum cmd = tokens.getCommandEnum();
                switch (cmd) {
                case TODO:
                    System.out.println(catbot.addTodo(tokens.getArgs().get(0)));
                    break;
                case DEADLINE:
                    System.out.println(catbot.addDeadline(tokens.getArgs().get(0), tokens.getArgs().get(1)));
                    break;
                case EVENT:
                    System.out.println(catbot.addEvent(tokens.getArgs().get(0), tokens.getArgs().get(1), tokens.getArgs().get(2)));
                    break;
                case LIST:
                    System.out.println(catbot.taskList.list());
                    break;
                case MARK:
                    System.out.println(catbot.taskList.mark(tokens.getTaskIndex() - 1));
                    break;
                case UNMARK:
                    System.out.println(catbot.taskList.unmark(tokens.getTaskIndex() - 1));
                    break;
                case DELETE:
                    System.out.println(catbot.taskList.delete(tokens.getTaskIndex() - 1));
                    break;
                case FIND:
                    System.out.println(catbot.taskList.find(tokens.getArgs().get(0)));
                    break;
                case BYE:
                    try {
                        catbot.storage.saveTasks(catbot.taskList);
                    } catch (IOException e) {
                        System.out.println(catbot.ui.showSaveError(catbot.taskList));
                    }
                    System.out.println(catbot.ui.showGoodbye());
                    catbot.sc.close();
                    return;
                default:
                    throw new CatbotException("I'm sorry, I don't understand that command.");
                }
            } catch (CatbotException e) {
                System.out.println(catbot.ui.showError(e.getMessage()));
            }
        }
    }

    /**
     * Generates a response for the user's chat message.
     * 
     * @param input User input string.
     * @return Response string from Catbot.
     */
    public String getResponse(String input) {
        try {
            Command tokens = this.parser.parseCommand(input);
            CommandEnum cmd = tokens.getCommandEnum();
            switch (cmd) {
            case TODO:
                return this.addTodo(tokens.getArgs().get(0));
            case DEADLINE:
                return this.addDeadline(tokens.getArgs().get(0), tokens.getArgs().get(1));
            case EVENT:
                return this.addEvent(tokens.getArgs().get(0), tokens.getArgs().get(1), tokens.getArgs().get(2));
            case LIST:
                return this.taskList.list();
            case MARK:
                return this.taskList.mark(tokens.getTaskIndex() - 1);
            case UNMARK:
                return this.taskList.unmark(tokens.getTaskIndex() - 1);
            case DELETE:
                return this.taskList.delete(tokens.getTaskIndex() - 1);
            case FIND:
                return this.taskList.find(tokens.getArgs().get(0));
            case BYE:
                try {
                    this.storage.saveTasks(this.taskList);
                } catch (IOException e) {
                    return this.ui.showSaveError(this.taskList);
                }
                return this.ui.showGoodbye();
            default:
                throw new CatbotException("I'm sorry, I don't understand that command.");
            }
        } catch (CatbotException e) {
            return this.ui.showError(e.getMessage());
        }
    }
}
