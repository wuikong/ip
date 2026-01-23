import java.util.Scanner;
import java.util.ArrayList;

public class Catbot {
    private static ArrayList<String> taskList = new ArrayList<>();

    private static void list() {
        if (taskList.isEmpty()) {
            System.out.println("No tasks in the list.");
        } else {
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println((i + 1) + ". " + taskList.get(i));
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello! I'm Catbot\nWhat can I do for you?");
        while (true) {
            String input = sc.nextLine();
            if (input.equals("list")) {
                Catbot.list();
            } else if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else {
                System.out.println(input);
            }
        }
        sc.close();
    }
}
