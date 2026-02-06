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
    private static TaskList taskList;
    private static final String DATA_FILE = "data/catbot_data.txt";

    /**
     * Adds a todo task.
     *
     * @param description Task description.
     * @throws CatbotException If the task cannot be created.
     */
    public static void todo(String description) throws CatbotException {
        Task t = new Todo(description);
        taskList.addTask(t);
    }

    /**
     * Adds a deadline task.
     *
     * @param description Task description.
     * @param by          Due date input.
     */
    public static void deadline(String description, String by) {
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
    public static void event(String description, String from, String to) {
        Task t = new Event(description, from, to);
        taskList.addTask(t);
    }

    /**
     * Starts the Catbot command loop.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(DATA_FILE);
        try {
            taskList = new TaskList(storage.loadTasks());
        } catch (FileNotFoundException e) {
            // File not found, start with empty task list
        } catch (Exception e) {
            ui.showLoadError();
            taskList = new TaskList();
        }
        Scanner sc = new Scanner(System.in);
        ui.showWelcome();
        Parser parser = new Parser();
        while (true) {
            try {
                ArrayList<String> tokens = parser.parse(sc.nextLine());
                Command cmd = Command.valueOf(tokens.get(0));
                switch (cmd) {
                case TODO:
                    Catbot.todo(tokens.get(1));
                    break;
                case DEADLINE:
                    Catbot.deadline(tokens.get(1), tokens.get(2));
                    break;
                case EVENT:
                    Catbot.event(tokens.get(1), tokens.get(2), tokens.get(3));
                    break;
                case LIST:
                    taskList.list();
                    break;
                case MARK:
                    taskList.mark(Integer.parseInt(tokens.get(1)) - 1);
                    break;
                case UNMARK:
                    taskList.unmark(Integer.parseInt(tokens.get(1)) - 1);
                    break;
                case DELETE:
                    taskList.delete(Integer.parseInt(tokens.get(1)) - 1);
                    break;
                case FIND:
                    taskList.find(tokens.get(1));
                    break;
                case BYE:
                    try {
                        storage.saveTasks(taskList);
                    } catch (IOException e) {
                        ui.showSaveError(taskList.toString());
                    }
                    ui.showGoodbye();
                    sc.close();
                    return;
                default:
                    throw new CatbotException("I'm sorry, I don't understand that command.");
                }
            } catch (CatbotException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
