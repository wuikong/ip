import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

    public static TaskList loadTasks(String filename) throws FileNotFoundException {
        taskList = new TaskList();
        Scanner sc = new Scanner(new File(filename));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                System.out.println("Malformed line in data file: " + line);
                continue;
            }
            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];
            Task t = null;
            switch (type) {
                case "T":
                    t = new Todo(description);
                    break;
                case "D":
                    if (parts.length < 4) {
                        System.out.println("Malformed deadline in data file: " + line);
                        break;
                    }
                    t = new Deadline(description, parts[3]);
                    break;
                case "E":
                    if (parts.length < 5) {
                        System.out.println("Malformed event in data file: " + line);
                        break;
                    }
                    t = new Event(description, parts[3], parts[4]);
                    break;
                default:
                    System.out.println("Unknown task type in data file: " + type);
            }
            if (t != null) {
                if (isDone) {
                    t.mark();
                }
                taskList.addTask(t);
            }
        }
        sc.close();
        return taskList;
    }

    public static void saveTasks(String filename, TaskList taskList) throws IOException {
        File file = new File(filename);
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }
        
        FileWriter writer = new FileWriter(filename);
        writer.write(taskList.toString() + "\n");
        writer.close();
    }

    public static void main(String[] args) {
        try {
            taskList = Catbot.loadTasks(DATA_FILE);
        } catch (FileNotFoundException e) {
            // File not found, start with empty task list
        } catch (Exception e) {
            System.out.println("Could not load tasks from file. Starting with an empty task list.");
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
                        Catbot.saveTasks(DATA_FILE, taskList);
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
