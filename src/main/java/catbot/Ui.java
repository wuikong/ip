package catbot;

/**
 * Handles user-facing messages.
 */
public class Ui {
    /**
     * Shows the welcome message.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Catbot\nWhat can I do for you?");
    }

    /**
     * Shows the goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Shows an error when loading tasks fails.
     */
    public void showLoadError() {
        System.out.println("Could not load tasks from file. Starting with an empty task list.");
    }

    /**
     * Shows an error when saving tasks fails.
     *
     * @param tasks Current tasks to display.
     */
    public void showSaveError(String tasks) {
        System.out.println("Could not save tasks to file. Please save your tasks manually.");
        System.out.println(tasks);
    }

    /**
     * Shows an error message to the user.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println(message);
    }
}
