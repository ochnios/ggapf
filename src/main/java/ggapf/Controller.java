package ggapf;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    private static final String INFO_PLACEHOLDER = "---";

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
    @FXML
    private RadioButton bfsOption;
    @FXML
    private RadioButton dijkstraOption;
    @FXML
    private RadioButton splitOption;
    @FXML
    private TextField splitSubgraphsField;

    // Informations footer
    @FXML
    private Label connectedInfo;
    @FXML
    private Label shortestPathInfo;
    @FXML
    private Label startNodeInfo;
    @FXML
    private Label endNodeInfo;

    // Heatmap
    @FXML
    private Label minWeightHeatmap;
    @FXML
    private Label maxWeightHeatmap;

    @FXML
    private void sourceFileFieldAction(MouseEvent event) {
        selectedSourceFile = Main.chooseFile();
        if(selectedSourceFile != null) {
            sourceFileField.setText(selectedSourceFile.getName());
            Main.open(selectedSourceFile);
        } else
            sourceFileField.setText("source_file");
    }

    @FXML
    private void resultFileFieldAction(MouseEvent event) {
        selectedResultFile = Main.chooseFile();
        if(selectedResultFile != null) {
            File resultFileTemp = selectedResultFile;

            resultFileField.setText(resultFileTemp.getName());
            Main.save(resultFileTemp);
        } else
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
        else {
            File resultFileTemp = selectedResultFile;
            Main.save(resultFileTemp);
        }
    }

    @FXML
    private void generateButtonAction(ActionEvent event) {
        Integer rows = Validator.parseSize(rowsField.getText());
        Integer columns = Validator.parseSize(columnsField.getText());
        Double minWeight = Validator.parseWeight(minWeightField.getText());
        Double maxWeight = Validator.parseWeight(maxWeightField.getText());
        Integer subgraphs = Validator.parseSubgraphs(subgraphsField.getText());

        String errorMessage = new String();

        if(rows == null || columns == null) {
            String possibleRangeOfSize = "[" + Validator.MIN_GRAPH_SIZE + " : " + Validator.MAX_GRAPH_SIZE + "]";
            errorMessage += ("Incorrect number of rows! Possible range: " + possibleRangeOfSize + " \n");
        }

        if(minWeight == null || maxWeight == null) {
            String possibleRangeOfWeight = "[" + Validator.MIN_WEIGHT + " : " + Validator.MAX_WEIGHT+ "]";
            errorMessage += ("Incorrect weights! Possible range: " + possibleRangeOfWeight + " \n");
        } else {
            if(minWeight >= maxWeight) {
                errorMessage += "First weight should be less than second!\n";
            }
        }

        if(subgraphs == null) {
            String possibleRangeOfSubgraphs = "[" + Validator.MIN_SUBGRAPHS + " : " + Validator.MAX_SUBGRAPHS+ "]";
            errorMessage += ("Incorrect subgraphs number!" + possibleRangeOfSubgraphs + " \n");
        }

        if(errorMessage.isEmpty()) {
            Main.generate(rows, columns, minWeight, maxWeight, subgraphs);
            Main.redrawGraph();
        } else 
            Main.showPopup(errorMessage);

    }

    @FXML
    private void startButtonAction(ActionEvent event) {
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
            String startNode = startNodeInfo.getText();
            String endNode = endNodeInfo.getText();
            if(Validator.isInteger(startNode) && Validator.isInteger((endNode))) {
                ShortestPath shortestPath = Main.dijkstra(Integer.parseInt(startNode), Integer.parseInt(endNode));
                if(shortestPath == null) return;

                if(shortestPath.getPathLength() == ShortestPath.INFINITY)
                    shortestPathInfo.setText("no exists");
                else {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator('.');
                    DecimalFormat format = new DecimalFormat("#.##", symbols);
                    shortestPathInfo.setText(format.format(shortestPath.getPathLength()));
                }
            } else {
                Main.showPopup("Please, choose the start and end node at first");
            }
            
        } else if(splitOption.isSelected()) {
            Integer subgraphs = Validator.parseSubgraphs(subgraphsField.getText());
            if(subgraphs == null) {
                String possibleRangeOfSubgraphs = "[" + Validator.MIN_SUBGRAPHS + " : " + Validator.MAX_SUBGRAPHS+ "]";
                String message = ("Incorrect subgraphs number!" + possibleRangeOfSubgraphs + " \n");
                Main.showPopup(message);
            } else {
                Main.split(subgraphs);
                Main.redrawGraph();
            }
        } else {
            Main.showPopup("You should choose action at first!");
        }
    }

    @FXML
    private void resetButtonAction(ActionEvent event) {
        Main.resetFooter();
        Main.redrawGraph();
    }

    private void chosenNodeReset(Label infoToReset) {
        String nodeToReset = infoToReset.getText();
        if(!nodeToReset.equals(INFO_PLACEHOLDER)) {
            infoToReset.setText(INFO_PLACEHOLDER);
            Main.clearSelectionForDijkstra(Integer.parseInt(nodeToReset));
        }
    }

    public Display initDisplay() {
        display = new Display(canvasWrapper);
        return display;
    }

    public void resetFooter() {
        connectedInfo.setText(INFO_PLACEHOLDER);
        shortestPathInfo.setText(INFO_PLACEHOLDER);
        startNodeInfo.setText(INFO_PLACEHOLDER);
        endNodeInfo.setText(INFO_PLACEHOLDER);
    }

    public void setStartNodeInfo(int nodeNubmer) {
        chosenNodeReset(startNodeInfo);
        startNodeInfo.setText(Integer.toString(nodeNubmer));
    }

    public void setEndNodeInfo(int nodeNubmer) {
        chosenNodeReset(endNodeInfo);
        endNodeInfo.setText(Integer.toString(nodeNubmer));
    }

    public void setHeatMapRange(double min, double max) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#.##", symbols);
        minWeightHeatmap.setText(format.format(min));
        maxWeightHeatmap.setText(format.format(max));
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Main controller initialize...");
    }
}
