package gui;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * A chat bubble containing an avatar and a text label.
 * Built from DialogBox.fxml (fx:root pattern).
 */
public class DialogBox extends HBox {
    @FXML private Label dialog;
    @FXML private ImageView displayPicture;

    /**
     * Constructs a DialogBox by loading its FXML and setting text + avatar.
     * */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DialogBox.fxml", e);
        }

        dialog.setText(text);
        displayPicture.setImage(img);
        if (!getStyleClass().contains("dialog")) {
            getStyleClass().add("dialog");
        }
    }

    /**
     * Flips the dialog box so the avatar is on the left and text on the right.
     * */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Factory for a user (right-aligned) bubble.
     * */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.getStyleClass().remove("dialog-bot");
        if (!db.dialog.getStyleClass().contains("bubble-user")) {
            db.dialog.getStyleClass().add("bubble-user");
        }
        db.dialog.getStyleClass().remove("bubble-bot");
        return db;
    }

    /**
     * Factory for a bot (left-aligned) bubble.
     * */
    public static DialogBox getDukeDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        if (!db.getStyleClass().contains("dialog-bot")) {
            db.getStyleClass().add("dialog-bot");
        }
        db.dialog.getStyleClass().remove("bubble-user");
        if (!db.dialog.getStyleClass().contains("bubble-bot")) {
            db.dialog.getStyleClass().add("bubble-bot");
        }
        return db;
    }
}
