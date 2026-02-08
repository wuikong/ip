package catbot;

import java.util.ArrayList;

/**
 * Represents a command issued by the user.
 */
public class Command {
    private CommandEnum commandEnum;
    private ArrayList<String> args;
    private int taskIndex;

    /**
     * Creates a Command instance.
     *
     * @param commandEnum Command type.
     */
    public Command(CommandEnum commandEnum) {
        this.commandEnum = commandEnum;
        this.args = new ArrayList<>();
        this.taskIndex = -1;
    }

    /**
     * Creates a Command instance with arguments.
     *
     * @param commandEnum Command type.
     * @param args        Command arguments.
     */
    public Command(CommandEnum commandEnum, ArrayList<String> args) {
        this(commandEnum);
        this.args = args;
    }

    /**
     * Creates a Command instance with a task index.
     *
     * @param commandEnum Command type.
     * @param taskIndex   Task index argument.
     */
    public Command(CommandEnum commandEnum, int taskIndex) {
        this(commandEnum);
        this.taskIndex = taskIndex;
    }

    public CommandEnum getCommandEnum() {
        return this.commandEnum;
    }

    public ArrayList<String> getArgs() {
        return this.args;
    }

    public int getTaskIndex() {
        return this.taskIndex;
    }
}
