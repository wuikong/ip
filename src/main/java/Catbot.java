import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Catbot {
    private static ArrayList<Task> taskList = new ArrayList<>();
    private final static String DATA_FILE = "data/catbot_data.txt";

    public static void todo(String description) {
        if (description.trim().isEmpty()) {
            System.out.println("The description of a todo cannot be empty.");
            return;
        }
        Task t = new Todo(description);
        taskList.add(t);
        System.out.println("Got it. I've added this task:\n" + t.toString());
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    public static void deadline(String description, String by) {
        Task t = new Deadline(description, by);
        taskList.add(t);
        System.out.println("Got it. I've added this task:\n" + t.toString());
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    public static void event(String description, String from, String to) {
        Task t = new Event(description, from, to);
        taskList.add(t);
        System.out.println("Got it. I've added this task:\n" + t.toString());
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
    }

    public static void list() {
        if (taskList.isEmpty()) {
            System.out.println("No tasks in the list.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println((i + 1) + ". " + taskList.get(i).toString());
            }
        }
    }

    public static void mark(int index) {
        if (index >= 0 && index < taskList.size()) {
            Task t = taskList.get(index);
            t.mark();
            System.out.println("Nice! I've marked this task as done:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public static void unmark(int index) {
        if (index >= 0 && index < taskList.size()) {
            Task t = taskList.get(index);
            t.unmark();
            System.out.println("OK, I've marked this task as not done yet:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public static void delete(int index) {
        if (index >= 0 && index < taskList.size()) {
            Task t = taskList.remove(index);
            System.out.println("Noted. I've removed this task:\n" + t.toString());
            System.out.println("Now you have " + taskList.size() + " tasks in the list.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public static void loadTasks(String filename, ArrayList<Task> taskList) throws FileNotFoundException {
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
                taskList.add(t);
            }
        }
        sc.close();
    }

    public static void saveTasks(String filename, ArrayList<Task> taskList) throws IOException {
        File file = new File(filename);
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }
        
        FileWriter writer = new FileWriter(filename);
        for (Task t : taskList) {
            writer.write(t.toDataString() + "\n");
        }
        writer.close();
    }

    public static void main(String[] args) {
        try {
            Catbot.loadTasks(DATA_FILE, taskList);
        } catch (FileNotFoundException e) {
            // File not found, start with empty task list
        } catch (Exception e) {
            System.out.println("Could not load tasks from file. Starting with an empty task list.");
            taskList.clear();
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
                    Catbot.list();
                    break;
                case MARK:
                    Catbot.mark(Integer.parseInt(tokens[1]) - 1);
                    break;
                case UNMARK:
                    Catbot.unmark(Integer.parseInt(tokens[1]) - 1);
                    break;
                case DELETE:
                    Catbot.delete(Integer.parseInt(tokens[1]) - 1);
                    break;
                case BYE:
                    try {
                        Catbot.saveTasks(DATA_FILE, taskList);
                    } catch (IOException e) {
                        System.out.println("Could not save tasks to file. Please save your tasks manually.");
                        Catbot.list();
                    }
                    System.out.println("Bye. Hope to see you again soon!");
                    sc.close();
                    return;
            }
        }
    }
}
