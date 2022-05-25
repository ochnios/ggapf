package ggapf;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class Controller implements Initializable {

    // Pane for displaying graph
    @FXML
    private Pane canvasPane;
    private Display display;

    // Open & Save bar
    @FXML
    private TextField sourceFilenameField;
    @FXML
    private TextField resultFilenameField;

    // Generator bar
    @FXML
    private TextField rowsField;
    @FXML
    private TextField columnsField;
    @FXML
    private TextField minWeightField;
    @FXML
    private TextField maxWeightField;
    @FXML
    private TextField subgraphsField;

    // Option bar
    // IMPORTANT: Radio buttons doesnt work properly - you can choose all of them at once
    @FXML
    private RadioButton bfsOption;
    @FXML
    private RadioButton dijkstraOption;
    @FXML
    private RadioButton splitOption;
    @FXML
    private TextField splitSubgraphsField;

    // Informations side bar
    @FXML
    private Label connectedInfo;
    @FXML
    private Label shortestPathInfo;

    @FXML
    private void openButtonAction(ActionEvent event) {
        System.out.println("OPEN");
        String pathname = sourceFilenameField.getText();
        Main.open(pathname);
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        Main.showPopup("SAVE");
    }

    @FXML
    private void generateButtonAction(ActionEvent event) {
        Main.showPopup("GENERATE");
    }

    @FXML
    private void startButtonAction(ActionEvent event) {
        // IMPORTANT: Radio buttons doesnt work properly - you can choose all of them at once
        if(bfsOption.isSelected()) {
            Boolean isConnected = Main.BFS();
            if(isConnected == null) {
                connectedInfo.setText("-");
            } else if(isConnected == true) {
                connectedInfo.setText("YES");
            } else {
                connectedInfo.setText("NO");
            }

        } else if(dijkstraOption.isSelected()) {
            Main.showPopup("DIJKSTRA...");

        } else if(splitOption.isSelected()) {
            Main.showPopup("SPLITTER...");
        } else {
            Main.showPopup("You should choose action at first!");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        display = new Display(canvasPane);
        display.drawGraph(10, 10);        
    }
}
