package catbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import catbot.task.Deadline;
import catbot.task.Event;
import catbot.task.Task;
import catbot.task.Todo;

/**
 * Handles loading and saving tasks to persistent storage.
 */
public class Storage {
    private String filePath;

    /**
     * Creates a storage handler using the provided file path.
     *
     * @param filePath Path to the data file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return List of tasks loaded from storage.
     * @throws FileNotFoundException If the data file cannot be found.
     */
    public ArrayList<Task> loadTasks() throws FileNotFoundException {
        ArrayList<Task> taskList = new ArrayList<>();
        Scanner sc = new Scanner(new File(this.filePath));
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
        return taskList;
    }

    /**
     * Saves tasks to the data file.
     *
     * @param taskList Task list to save.
     * @throws IOException If an IO error occurs while saving.
     */
    public void saveTasks(TaskList taskList) throws IOException {
        File file = new File(this.filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }

        FileWriter writer = new FileWriter(this.filePath);
        writer.write(taskList.toString() + "\n");
        writer.close();
    }
}
