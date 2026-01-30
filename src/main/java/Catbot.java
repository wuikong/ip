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
        while (true) {
            String[] tokens = sc.nextLine().split(" ", 2);
            String[] parts;
            Command cmd = null;
            try {
                cmd = Command.valueOf(tokens[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("I'm sorry, I don't understand that command.");
                continue;
            }
            switch(cmd) {
                case TODO:
                    Catbot.todo(tokens[1]);
                    break;
                case DEADLINE:
                    parts = tokens[1].split(" /by ");
                    if (parts.length == 2) {
                        Catbot.deadline(parts[0], parts[1]);
                    } else {
                        System.out.println("Invalid deadline format. Use: deadline <description> /by <" + DateTimeUtil.INPUT_PATTERN + ">");
                    }
                    break;
                case EVENT:
                    parts = tokens[1].split(" /from | /to "); 
                    if (parts.length == 3) {
                        Catbot.event(parts[0], parts[1], parts[2]);
                    } else {
                        System.out.println("Invalid event format. Use: event <description> /from <" + DateTimeUtil.INPUT_PATTERN + "> /to <" + DateTimeUtil.INPUT_PATTERN + ">");
                    }
                    break;
                case LIST:
                    taskList.list();
                    break;
                case MARK:
                    taskList.mark(Integer.parseInt(tokens[1]) - 1);
                    break;
                case UNMARK:
                    taskList.unmark(Integer.parseInt(tokens[1]) - 1);
                    break;
                case DELETE:
                    taskList.delete(Integer.parseInt(tokens[1]) - 1);
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
