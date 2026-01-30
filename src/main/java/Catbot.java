import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Catbot {
    private static TaskList taskList;
    private final static String DATA_FILE = "data/catbot_data.txt";

    public static void todo(String description) {
        if (description.trim().isEmpty()) {
            System.out.println("The description of a todo cannot be empty.");
            return;
        }
        Task t = new Todo(description);
        taskList.addTask(t);
    }

    public static void deadline(String description, String by) {
        Task t = new Deadline(description, by);
        taskList.addTask(t);
    }

    public static void event(String description, String from, String to) {
        Task t = new Event(description, from, to);
        taskList.addTask(t);
    }

    public static void main(String[] args) {
        Storage storage = new Storage(DATA_FILE);
        try {
            taskList = new TaskList(storage.loadTasks());
        } catch (FileNotFoundException e) {
            // File not found, start with empty task list
        } catch (Exception e) {
            System.out.println("Could not load tasks from file. Starting with an empty task list.");
            taskList = new TaskList();
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello! I'm Catbot\nWhat can I do for you?");
        Parser parser = new Parser();
        while (true) {
            ArrayList<String> tokens = parser.parse(sc.nextLine());
            if (tokens == null) {
                continue;
            }
            Command cmd = Command.valueOf(tokens.get(0));
            switch(cmd) {
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
                case BYE:
                    try {
                        storage.saveTasks(taskList);
                    } catch (IOException e) {
                        System.out.println("Could not save tasks to file. Please save your tasks manually.");
                        System.out.println(taskList.toString());
                    }
                    System.out.println("Bye. Hope to see you again soon!");
                    sc.close();
                    return;
            }
        }
    }
}
