package ggapf;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Popup window class
 */
public class Popup {
    private Stage stage;
    private Parent root;
    private FXMLLoader loader;
    private PopupController popupController;

    /**
     * Creates the Popup windows instance
     * @param rootScene root GUI scene
     * @throws IOException
     */
    public Popup(Scene rootScene) throws IOException {
        loader = new FXMLLoader(getClass().getResource("popup.fxml"));
        root = loader.load();
        popupController = loader.getController();

        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Message");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(rootScene.getWindow());
    }

    /**
     * Shows the popup window with the given message
     * @param message
     */
    public void show(String message) {
        popupController.setMessage(message);
        stage.showAndWait();
    }

    /**
     * Hides the popup window
     */
    public void hide() {
        stage.close();
    }


}
