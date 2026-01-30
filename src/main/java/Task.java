public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        String checkbox = "[" + (isDone ? "X" : " ") + "] ";
        return checkbox + description;
    }

    public String toDataString() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
