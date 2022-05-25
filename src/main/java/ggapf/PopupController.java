package ggapf;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PopupController implements Initializable {
    @FXML
    private Label message;
    @FXML
    private Button okButton;

    @FXML
    private void okButtonController(ActionEvent event) {
        Main.hidePopup();
    }

    public void setMessage(String messageText) {
        message.setText(messageText);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Initializing!");
    }
}
