package mug;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
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

    private Mug mug;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/Pot.png"));
    private final Image mugImage = new Image(this.getClass().getResourceAsStream("/images/Mug.png"));

    /**
     * Prints welcome messages.
     */
    private static String welcome() {
        return "Hello! I'm Mug\n"
                + "What can Mug do for you ?_?";
    }

    /**
     * Initials the Mug main window.
     */
    @FXML
    public void initialize() {
        DialogBox welcome = DialogBox.getMugDialog(MainWindow.welcome(), mugImage);
        dialogContainer.getChildren().add(welcome);
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setMug(Mug mug) {
        this.mug = mug;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containingMug's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = mug.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMugDialog(response, mugImage)
        );
        userInput.clear();
        if (input.toUpperCase().equals("BYE")) {
            userInput.setPromptText("Text field has been disabled. Hope to see you soon!!");
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
