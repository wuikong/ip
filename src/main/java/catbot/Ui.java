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
        return "Hello! I'm Catbot 😸\nWhat can I do for you?";
    }

    /**
     * Shows the goodbye message.
     */
    public String showGoodbye() {
        return "Bye. Hope to see you again soon! 😸";
    }

    /**
     * Shows a message when a task is added.
     *
     * @param task     The task that was added.
     * @param taskList The current task list.
     */
    public String showAddedTask(Task task, TaskList taskList) {
        return "Got it. I've added this task:\n" + task.toString()
                + "\nNow you have " + taskList.getSize() + " tasks in the list.";
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
