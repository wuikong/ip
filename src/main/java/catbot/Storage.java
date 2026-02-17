package catbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import catbot.task.Task;

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
        assert filePath != null : "File path cannot be null";
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return List of tasks loaded from storage.
     * @throws FileNotFoundException If the data file cannot be found.
     */
    public TaskList loadTasks(Parser parser) throws FileNotFoundException {
        assert parser != null : "Parser cannot be null";
        TaskList taskList = new TaskList();
        Scanner sc = new Scanner(new File(this.filePath));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            try {
                Task t = parser.parseDataFileTask(line);
                taskList.addTask(t);
            } catch (CatbotException e) {
                System.out.println("Error parsing task from data file: " + e.getMessage());
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
        assert taskList != null : "Task list cannot be null";
        File file = new File(this.filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }

        FileWriter writer = new FileWriter(this.filePath);
        writer.write(taskList.toString());
        writer.close();
    }
}
