package catbot;

import java.util.ArrayList;
import java.util.stream.Stream;

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
        assert tasks != null : "Initial task list cannot be null";
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
        assert t != null : "Task to add cannot be null";
        this.taskList.add(t);
    }

    /**
     * Prints all tasks in the list.
     *
     * @return Formatted string of all tasks.
     */
    public String list() {
        if (this.taskList.isEmpty()) {
            return "No tasks in the list.";
        }
        return "Here are the tasks in your list:"
                + Stream.iterate(0, n -> n + 1)
                .limit(this.taskList.size())
                .reduce("", (acc, i) ->
                        acc + "\n" + (i + 1) + ". "
                        + this.taskList.get(i).toString(), String::concat);
    }

    /**
     * Marks the task at the given index as completed.
     *
     * @param index Zero-based task index.
     * @return Confirmation message.
     */
    public String mark(int index) {
        if (index >= 0 && index < this.getSize()) {
            Task t = this.taskList.get(index);
            t.mark();
            return "Nice! I've marked this task as done:\n" + t.toString();
        }
        return "Invalid task number.";
    }

    /**
     * Marks the task at the given index as not completed.
     *
     * @param index Zero-based task index.
     * @return Confirmation message.
     */
    public String unmark(int index) {
        if (index >= 0 && index < this.getSize()) {
            Task t = this.taskList.get(index);
            t.unmark();
            return "OK, I've marked this task as not done yet:\n" + t.toString();
        }
        return "Invalid task number.";
    }

    /**
     * Removes the task at the given index.
     *
     * @param index Zero-based task index.
     * @return Confirmation message.
     */
    public String delete(int index) {
        if (index >= 0 && index < this.getSize()) {
            Task t = this.taskList.remove(index);
            return "Noted. I've removed this task:\n" + t.toString()
                    + "\nNow you have " + this.getSize() + " tasks in the list.";
        }
        return "Invalid task number.";
    }

    /**
     * Finds and lists tasks that contain the given keyword.
     *
     * @param keyword Keyword to search for in task descriptions.
     * @return Matching tasks as a formatted string.
     */
    public String find(String keyword) {
        String foundTasks = Stream.iterate(0, n -> n + 1)
                .limit(this.taskList.size())
                .filter(i -> this.taskList.get(i).isInDescription(keyword))
                .reduce("", (acc, i) ->
                        acc + "\n" + (i + 1) + ". "
                        + this.taskList.get(i).toString(),
                        String::concat);
        if (foundTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        return "Here are the matching tasks in your list:" + foundTasks;
    }

    /**
     * Updates the task at the given index with a new task.
     *
     * @param index   Zero-based task index.
     * @param newTask New task to replace the old one.
     * @return Confirmation message.
     */
    public String update(int index, Task newTask) {
        if (index >= 0 && index < this.getSize()) {
            this.taskList.set(index, newTask);
            return "Got it. I've updated the task:\n" + newTask.toString();
        }
        return "Invalid task number.";
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
