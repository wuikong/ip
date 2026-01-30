package catbot;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.taskList = tasks;
    }

    public void addTask(Task t) {
        this.taskList.add(t);
        System.out.println("Got it. I've added this task:\n" + t.toString());
        System.out.println("Now you have " + this.taskList.size() + " tasks in the list.");
    }

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

    public void mark(int index) {
        if (index >= 0 && index < this.taskList.size()) {
            Task t = this.taskList.get(index);
            t.mark();
            System.out.println("Nice! I've marked this task as done:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void unmark(int index) {
        if (index >= 0 && index < this.taskList.size()) {
            Task t = this.taskList.get(index);
            t.unmark();
            System.out.println("OK, I've marked this task as not done yet:\n" + t.toString());
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public void delete(int index) {
        if (index >= 0 && index < this.taskList.size()) {
            Task t = this.taskList.remove(index);
            System.out.println("Noted. I've removed this task:\n" + t.toString());
            System.out.println("Now you have " + this.taskList.size() + " tasks in the list.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Task t : taskList) {
            sb.append(t.toDataString()).append("\n");
        }
        return sb.toString().trim();
    }
}
