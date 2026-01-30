package catbot;

public class Ui {
    public void showWelcome() {
        System.out.println("Hello! I'm Catbot\nWhat can I do for you?");
    }
    
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void showLoadError() {
        System.out.println("Could not load tasks from file. Starting with an empty task list.");
    }

    public void showSaveError(String tasks) {
        System.out.println("Could not save tasks to file. Please save your tasks manually.");
        System.out.println(tasks);
    }

    public void showError(String message) {
        System.out.println(message);
    }
}
