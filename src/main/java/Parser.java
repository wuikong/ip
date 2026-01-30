import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    public ArrayList<String> parse(String input) throws CatbotException {
        ArrayList<String> tokenList = new ArrayList<>();
        String[] tokens = input.split(" ", 2);
        tokens[0] = tokens[0].toUpperCase();
        Command cmd = null;
        try {
            cmd = Command.valueOf(tokens[0]);
        } catch (IllegalArgumentException e) {
            throw new CatbotException("I'm sorry, I don't understand that command.");
        }
        tokenList.add(tokens[0]);
        switch(cmd) {
        case TODO:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException("The description of a todo cannot be empty.");
            }
            tokenList.add(tokens[1]);
            break;
        case DEADLINE:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException("Invalid deadline format. Use: deadline <description> /by <" + DateTimeUtil.INPUT_PATTERN + ">");
            }
            tokens = tokens[1].split(" /by ");
            if (tokens.length != 2) {
                throw new CatbotException("Invalid deadline format. Use: deadline <description> /by <" + DateTimeUtil.INPUT_PATTERN + ">");
            }
            tokenList.addAll(Arrays.asList(tokens));
            break;
        case EVENT:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException("Invalid event format. Use: event <description> /from <" + DateTimeUtil.INPUT_PATTERN + "> /to <" + DateTimeUtil.INPUT_PATTERN + ">");
            }
            tokens = tokens[1].split(" /from | /to "); 
            if (tokens.length != 3) {
                throw new CatbotException("Invalid event format. Use: event <description> /from <" + DateTimeUtil.INPUT_PATTERN + "> /to <" + DateTimeUtil.INPUT_PATTERN + ">");
            }
            tokenList.addAll(Arrays.asList(tokens));
            break;
        case MARK:
        case UNMARK:
        case DELETE:
            if (tokens.length < 2 || tokens[1].trim().isEmpty()) {
                throw new CatbotException("Please provide a valid task number.");
            }
            try {
                Integer.parseInt(tokens[1]);
            } catch (NumberFormatException e) {
                throw new CatbotException("Please provide a valid task number.");
            }
            tokenList.add(tokens[1]);
            break;
        default:
            break;
        }
        return tokenList;
    }
}
