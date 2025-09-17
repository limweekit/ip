package gui;

import java.io.IOException;
import java.util.Objects;

import app.YapGPT;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Entry point for the YapGPT JavaFX GUI (FXML-based).
 */
public class Main extends Application {
    private final YapGPT yapgpt = new YapGPT("data/yapgpt.txt");

    /**
     * Initializes and displays the primary stage of the YapGPT JavaFX application.
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set. Applications may create
     *              other stages if needed, but they will not be primary stages.
     */
    @Override
    public void start(Stage stage) {
        try {
            // Load FXML
            FXMLLoader fxmlLoader = new FXMLLoader(
                    Objects.requireNonNull(
                            Main.class.getResource("/view/MainWindow.fxml"),
                            "Missing /view/MainWindow.fxml on classpath"));
            AnchorPane root = fxmlLoader.load();

            // Inject YapGPT into controller
            MainWindow controller = fxmlLoader.getController();
            controller.setYapGPT(yapgpt);

            // Scene + stylesheet
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            Main.class.getResource("/view/style.css"),
                            "Missing /view/style.css on classpath"
                    ).toExternalForm()
            );

            // Stage setup
            stage.setTitle("YapGPT");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMinWidth(400);
            stage.setMinHeight(600);
            stage.centerOnScreen();
            stage.show();

            controller.requestFocusOnInput();

        } catch (IOException e) {
            throw new RuntimeException("Failed to start YapGPT.", e);
        }
    }
}
