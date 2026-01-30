import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
