package catbot;

import java.util.ArrayList;

/**
 * Manages a list of tasks and related operations.
 */
public class TaskList {
    private ArrayList<Task> taskList;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Creates a task list from an existing list of tasks.
     *
     * @param tasks Initial tasks to store.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.taskList = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param t Task to add.
     */
    public void addTask(Task t) {
        this.taskList.add(t);
        System.out.println("Got it. I've added this task:\n" + t.toString());
        System.out.println("Now you have " + this.taskList.size() + " tasks in the list.");
    }

    /**
     * Prints all tasks in the list.
     */
    public void list() {
        if (this.taskList.isEmpty()) {
            System.out.println("No tasks in the list.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < this.taskList.size(); i++) {
                System.out.println((i + 1) + ". " + this.taskList.get(i).toString());
            }
        }
    }

    /**
     * Marks the task at the given index as completed.
     *
     * @param index Zero-based task index.
     */
    public void mark(int index) {
        if (index >= 0 && index < this.taskList.size()) {
            Task t = this.taskList.get(index);
            t.mark();
            System.out.println("Nice! I've marked this task as done:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    /**
     * Marks the task at the given index as not completed.
     *
     * @param index Zero-based task index.
     */
    public void unmark(int index) {
        if (index >= 0 && index < this.taskList.size()) {
            Task t = this.taskList.get(index);
            t.unmark();
            System.out.println("OK, I've marked this task as not done yet:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    /**
     * Removes the task at the given index.
     *
     * @param index Zero-based task index.
     */
    public void delete(int index) {
        if (index >= 0 && index < this.taskList.size()) {
            Task t = this.taskList.remove(index);
            System.out.println("Noted. I've removed this task:\n" + t.toString());
            System.out.println("Now you have " + this.taskList.size() + " tasks in the list.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    /**
     * Finds and lists tasks that contain the given keyword.
     *
     * @param keyword Keyword to search for in task descriptions.
     */
    public void find(String keyword) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task t : this.taskList) {
            if (t.isInDescription(keyword)) {
                foundTasks.add(t);
            }
        }
        if (foundTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < foundTasks.size(); i++) {
                System.out.println((i + 1) + ". " + foundTasks.get(i).toString());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Task t : taskList) {
            sb.append(t.toDataString()).append("\n");
        }
        return sb.toString().trim();
    }
}
