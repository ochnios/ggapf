package ggapf;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 * The PopupController class responsible for handling the Popup window GUI.
 * GUI elements are defined by FXML injection.
 */
public class PopupController implements Initializable {
    @FXML
    private Label message;
    @FXML
    private Button okButton;

    @FXML
    /**
     * Hides popup when OK button is pressed
     * @param event
     */
    private void okButtonController(ActionEvent event) {
        Main.hidePopup();
    }

    /**
     * Sets the popup window message
     * @param messageText message to be set
     */
    public void setMessage(String messageText) {
        message.setText(messageText);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //System.out.println("Popup initializing...");
    }
}
