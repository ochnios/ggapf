package ggapf;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

public class Controller implements Initializable {
    private Display display;

    @FXML
    Pane canvasPane;

    @FXML
    private void startButtonAction() {
        display.removeGraph(); // TEMP
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        display = new Display(canvasPane);
        display.drawGraph(10, 10);
    }
}
