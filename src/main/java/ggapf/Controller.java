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

/**
 * The main Controller class responsible for handling the GUI.
 * GUI elements are defined by FXML injection.
 */
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
    /**
     * Runs FileChooser window when the source_file field is clicked.
     * @param event
     */
    private void sourceFileFieldAction(MouseEvent event) {
        selectedSourceFile = Main.chooseFile();
        if(selectedSourceFile != null) {
            sourceFileField.setText(selectedSourceFile.getName());
            Main.open(selectedSourceFile);
        } else
            sourceFileField.setText("source_file");
    }

    @FXML
    /**
     * Runs FileChooser window when the result_file field is clicked.
     * @param event
     */
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
    /**
     * Tries to (re)open selected source_file.
     * On fail shows error message in a popup window.
     * @param event
     * @see Popup
     */
    private void openButtonAction(ActionEvent event) {
        if(selectedSourceFile == null)
            Main.showPopup("Please choose the coorect file at first");
        else 
            Main.open(selectedSourceFile);
    }

    @FXML
    /**
     * Tries to save loaded or generated graph to the result_file.
     * On fail shows error message in a popup window.
     * @param event
     * @see Popup
     */
    private void saveButtonAction(ActionEvent event) {
        if(selectedResultFile == null)
            Main.showPopup("Please choose the coorect file at first");
        else {
            File resultFileTemp = selectedResultFile;
            Main.save(resultFileTemp);
        }
    }

    @FXML
    /**
     * Generates graph with parameters defined by user in GUI fields.
     * On fail shows error message in a popup window.
     * @param event
     * @see Popup
     */
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
    /**
     * Perfoms an action selected in the GUI - runs Dijkstra, BFS or Split
     * When the user did not select an action, shows popup window
     * @param event
     * @see Popup
     * @see Dijkstra
     * @see Splitter
     */
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
    /*
     * Resets informations in footer and redraws graph
     */
    private void resetButtonAction(ActionEvent event) {
        Main.resetFooter();
        Main.redrawGraph();
    }

    /**
     * Resets given infoToReset Label which contain number of selected the start or the end node
     * @param infoToReset the Label to be reset
     */
    private void chosenNodeReset(Label infoToReset) {
        String nodeToReset = infoToReset.getText();
        if(!nodeToReset.equals(INFO_PLACEHOLDER)) {
            infoToReset.setText(INFO_PLACEHOLDER);
            Main.clearSelectionForDijkstra(Integer.parseInt(nodeToReset));
        }
    }

    /**
     * Initializes a Display object - the place where graph will be displayed
     * @return The initializated display object 
     */
    public Display initDisplay() {
        display = new Display(canvasWrapper);
        return display;
    }

    /**
     * Resets informations about graph in footer
     */
    public void resetFooter() {
        connectedInfo.setText(INFO_PLACEHOLDER);
        shortestPathInfo.setText(INFO_PLACEHOLDER);
        startNodeInfo.setText(INFO_PLACEHOLDER);
        endNodeInfo.setText(INFO_PLACEHOLDER);
    }

    /**
     * Sets information about start node number in the footer
     * @param nodeNumber start node number
     */
    public void setStartNodeInfo(int nodeNumber) {
        chosenNodeReset(startNodeInfo);
        startNodeInfo.setText(Integer.toString(nodeNumber));
    }

    /**
     * Sets the information about start node number in the footer
     * @param nodeNumber end node number
     */
    public void setEndNodeInfo(int nodeNumber) {
        chosenNodeReset(endNodeInfo);
        endNodeInfo.setText(Integer.toString(nodeNumber));
    }

    /**
     * Sets the boundary values of edges weights on the heatmap
     * @param min minimum weight in the graph
     * @param max maximium weight in the graph
     */
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
