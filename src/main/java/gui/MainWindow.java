package gui;

import app.YapGPT;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI window.
 */
public class MainWindow {

    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;

    private YapGPT yapgpt;

    private final Image userImage = new Image(
            MainWindow.class.getResourceAsStream("/images/DaUser.png"));
    private final Image dukeImage = new Image(
            MainWindow.class.getResourceAsStream("/images/DaDuke.png"));

    @FXML
    private void initialize() {
        // Auto-scroll to bottom when content grows
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the YapGPT instance.
     */
    public void setYapGPT(YapGPT y) {
        this.yapgpt = y;
        String welcome = "Beep-Boop! Hello, I'm YapGPT, your favourite chatbot. What can I do for you?";
        DialogBox bot = DialogBox.getDukeDialog(welcome, dukeImage);
        dialogContainer.getChildren().add(bot);
        fadeIn(bot);
    }

    /**
     * Give focus to the input on startup.
     */
    public void requestFocusOnInput() {
        if (userInput != null) {
            userInput.requestFocus();
        }
    }

    /**
     * Handles sending user input and displaying YapGPT's response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.isBlank()) {
            return;
        }

        String response = yapgpt.getResponse(input);

        DialogBox user = DialogBox.getUserDialog(input, userImage);
        DialogBox bot  = DialogBox.getDukeDialog(response, dukeImage);

        dialogContainer.getChildren().addAll(user, bot);
        fadeIn(user);
        fadeIn(bot);

        boolean isExit = input.trim().equalsIgnoreCase("bye");

        userInput.clear();

        if (isExit) {
            userInput.setDisable(true);

            javafx.animation.PauseTransition delay =
                    new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            delay.setOnFinished(e -> javafx.application.Platform.exit());
            delay.play();
        }
    }

    /**
     * Small fade-in for new messages.
     */
    private void fadeIn(Node n) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), n);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
