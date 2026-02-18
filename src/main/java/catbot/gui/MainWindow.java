package catbot.gui;

import catbot.Catbot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Catbot catbot;
    private boolean shouldCloseOnNextInput = false;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.jpeg"));
    private Image catbotImage = new Image(this.getClass().getResourceAsStream("/images/Catbot.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Catbot instance */
    public void setCatbot(Catbot c) {
        catbot = c;
        showWelcomeMessage();
    }

    /**
     * Displays the welcome message when the application starts.
     */
    private void showWelcomeMessage() {
        dialogContainer.getChildren().add(DialogBox.getCatbotDialog(catbot.showWelcome(), catbotImage));
        dialogContainer.getChildren().add(DialogBox.getCatbotDialog(catbot.getResponse("list"), catbotImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * Catbot's reply and then appends them to the dialog container. Clears the
     * user input after processing.
     */
    @FXML
    private void handleUserInput() {
        assert catbot != null : "Catbot instance has not been set";
        String input = userInput.getText();

        // Close the application if we're waiting to close after bye command
        if (shouldCloseOnNextInput) {
            userInput.clear();
            Platform.exit();
            return;
        }

        String response = catbot.getResponse(input);

        // Set flag to close on next input if the user sends bye command
        if (response.equals(catbot.getGoodbyeMessage())) {
            shouldCloseOnNextInput = true;
            response += "\nPress Enter to exit...";
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getCatbotDialog(response, catbotImage)
        );
        userInput.clear();
    }
}
