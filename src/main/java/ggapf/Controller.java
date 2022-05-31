package ggapf;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class Controller implements Initializable {

    private Display display;

    @FXML
    private HBox canvasWrapper;

    // Open & Save bar
    @FXML
    private TextField sourceFileField;
    private File selectedSourceFile;
    @FXML
    private TextField resultFileField;
    private File selectedResultFile;

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

    // Informations sidebar
    @FXML
    private Label connectedInfo;
    @FXML
    private Label shortestPathInfo;

    @FXML
    private void sourceFileFieldAction(MouseEvent event) {
        selectedSourceFile = Main.chooseFile();
        if(selectedSourceFile != null)
            sourceFileField.setText(selectedSourceFile.getName());
        else
            sourceFileField.setText("source_file");
    }

    @FXML
    private void resultFileFieldAction(MouseEvent event) {
        selectedResultFile = Main.chooseFile();
        if(selectedResultFile != null)
            resultFileField.setText(selectedResultFile.getName());
        else
            resultFileField.setText("result_file");
    }

    @FXML
    private void openButtonAction(ActionEvent event) {
        if(selectedSourceFile == null)
            Main.showPopup("Please choose the coorect file at first");
        else 
            Main.open(selectedSourceFile);
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        if(selectedResultFile == null)
            Main.showPopup("Please choose the coorect file at first");
        else 
            Main.save(selectedSourceFile);
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
        System.out.println("Main controller initialize...");
    }

    public Display initDisplay() {
        display = new Display(canvasWrapper);
        return display;
    }
}
