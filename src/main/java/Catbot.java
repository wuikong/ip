import java.util.Scanner;
import java.util.ArrayList;

public class Catbot {
    private static ArrayList<Task> taskList = new ArrayList<>();

    public static void addTask(String task) {
        Task t = new Task(task);
        taskList.add(t);
        System.out.println("added: " + t.toString());
    }

    public static void list() {
        if (taskList.isEmpty()) {
            System.out.println("No tasks in the list.");
        } else {
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println((i + 1) + ". " + taskList.get(i).toString());
            }
        }
    }

    public static void mark(int index) {
        if (index >= 0 && index < taskList.size()) {
            Task t = taskList.get(index);
            t.mark();
            System.out.println("Nice! I've marked this task as done:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public static void unmark(int index) {
        if (index >= 0 && index < taskList.size()) {
            Task t = taskList.get(index);
            t.unmark();
            System.out.println("OK, I've marked this task as not done yet:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello! I'm Catbot\nWhat can I do for you?");
        while (true) {
            String input = sc.nextLine();
            if (input.equals("list")) {
                Catbot.list();
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                Catbot.mark(index);
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                Catbot.unmark(index);
            } else if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else {
                Catbot.addTask(input);
            }
        }
        sc.close();
    }
}
