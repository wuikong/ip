package catbot.gui;

import java.io.IOException;

import catbot.Catbot;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Catbot using FXML.
 */
public class Main extends Application {

    private Catbot catbot = new Catbot();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setDuke(catbot); // inject the Catbot instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
