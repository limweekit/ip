package gui;

import java.io.IOException;
import app.YapGPT;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {
    private final YapGPT yapgpt = new YapGPT("data/yapgpt.txt");

    /**
     * Initializes and displays the primary stage of the YapGPT JavaFX application.
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            MainWindow controller = fxmlLoader.getController();
            controller.setYapGPT(yapgpt);

            stage.setScene(new Scene(ap));
            stage.setTitle("YapGPT");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
