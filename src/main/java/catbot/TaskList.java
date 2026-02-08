package catbot;

import java.util.ArrayList;

import catbot.task.Task;

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
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int getSize() {
        return this.taskList.size();
    }

    /**
     * Adds a task to the list.
     *
     * @param t Task to add.
     */
    public void addTask(Task t) {
        this.taskList.add(t);
    }

    /**
     * Prints all tasks in the list.
     */
    public String list() {
        if (this.taskList.isEmpty()) {
            return "No tasks in the list.";
        } else {
            StringBuilder sb = new StringBuilder("Here are the tasks in your list:");
            for (int i = 0; i < this.getSize(); i++) {
                sb.append("\n").append(i + 1).append(". ").append(this.taskList.get(i).toString());
            }
            return sb.toString();
        }
    }

    /**
     * Marks the task at the given index as completed.
     *
     * @param index Zero-based task index.
     */
    public String mark(int index) {
        if (index >= 0 && index < this.getSize()) {
            Task t = this.taskList.get(index);
            t.mark();
            return "Nice! I've marked this task as done:\n" + t.toString();
        } else {
            return "Invalid task number.";
        }
    }

    /**
     * Marks the task at the given index as not completed.
     *
     * @param index Zero-based task index.
     */
    public String unmark(int index) {
        if (index >= 0 && index < this.getSize()) {
            Task t = this.taskList.get(index);
            t.unmark();
            return "OK, I've marked this task as not done yet:\n" + t.toString();
        } else {
            return "Invalid task number.";
        }
    }

    /**
     * Removes the task at the given index.
     *
     * @param index Zero-based task index.
     */
    public String delete(int index) {
        if (index >= 0 && index < this.getSize()) {
            Task t = this.taskList.remove(index);
            return "Noted. I've removed this task:\n" + t.toString()
                    + "\nNow you have " + this.getSize() + " tasks in the list.";
        } else {
            return "Invalid task number.";
        }
    }

    /**
     * Finds and lists tasks that contain the given keyword.
     *
     * @param keyword Keyword to search for in task descriptions.
     */
    public String find(String keyword) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task t : this.taskList) {
            if (t.isInDescription(keyword)) {
                foundTasks.add(t);
            }
        }
        if (foundTasks.isEmpty()) {
            return "No matching tasks found.";
        } else {
            StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:");
            for (int i = 0; i < foundTasks.size(); i++) {
                sb.append("\n").append(i + 1).append(". ").append(foundTasks.get(i).toString());
            }
            return sb.toString();
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
