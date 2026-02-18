package catbot;

import catbot.task.Task;

/**
 * Handles user-facing messages.
 */
public class Ui {
    /**
     * Shows the welcome message.
     */
    public String showWelcome() {
        return "Hello! I'm Catbot\nWhat can I do for you? ^'w'^";
    }

    /**
     * Shows the goodbye message.
     */
    public String showGoodbye() {
        return "Bye. Hope to see you again soon! ^'w'^";
    }

    /**
     * Shows a message when a task is added.
     *
     * @param task     The task that was added.
     * @param taskList The current task list.
     */
    public String showAddedTask(Task task, TaskList taskList) {
        return "Got it. I've added this task:\n" + task.toString()
                + "\nNow you have " + taskList.getSize() + " tasks in the list. ^'w'^";
    }

    /**
     * Shows a message when a task is marked as done.
     *
     * @param task The task that was marked.
     */
    public String showMarkedTask(Task task) {
        return "Nice! I've marked this task as done:\n" + task.toString();
    }

    /**
     * Shows a message when a task is unmarked.
     *
     * @param task The task that was unmarked.
     */
    public String showUnmarkedTask(Task task) {
        return "OK, I've marked this task as not done yet:\n" + task.toString();
    }

    /**
     * Shows a message when a task is deleted.
     *
     * @param task     The task that was deleted.
     * @param taskList The current task list.
     */
    public String showDeletedTask(Task task, TaskList taskList) {
        return "Noted. I've removed this task:\n" + task.toString()
                + "\nNow you have " + taskList.getSize() + " tasks in the list. ^'w'^";
    }

    /**
     * Shows a message when a task is updated.
     *
     * @param task The updated task.
     */
    public String showUpdatedTask(Task task) {
        return "Got it. I've updated the task:\n" + task.toString();
    }

    /**
     * Shows an error when loading tasks fails.
     */
    public String showLoadError() {
        return "Could not load tasks from file. Starting with an empty task list.";
    }

    /**
     * Shows an error when saving tasks fails.
     *
     * @param tasks Current tasks to display.
     */
    public String showSaveError(TaskList tasks) {
        return "Could not save tasks to file. Please save your tasks manually.\n" + tasks.toString();
    }

    /**
     * Shows an error message to the user.
     *
     * @param message Error message to display.
     */
    public String showError(String message) {
        return message;
    }
}
