package ggapf;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Popup {
    private Stage stage;
    private Parent root;
    private FXMLLoader loader;
    private PopupController popupController;

    public Popup(Scene rootScene) throws IOException {
        loader = new FXMLLoader(getClass().getResource("popup.fxml"));
        root = loader.load();
        popupController = loader.getController();

        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(rootScene.getWindow());
    }

    public void show(String message) {
        popupController.setMessage(message);
        stage.showAndWait();
    }

    public void hide() {
        stage.close();
    }


}
