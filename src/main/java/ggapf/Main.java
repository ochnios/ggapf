package ggapf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.IOException;

/**
 * JavaFX App - Main class
 */
public class Main extends Application {

    private static Stage stage;
    private static Scene scene;
    private static FXMLLoader loader;
    private static Controller controller;
    private static Popup popup;
    private static Display display;
    private static Graph graph;

    @Override
    public void start(Stage stage) throws IOException {
        // primary stage setup
        Main.stage = stage;
        String fxmlFileName = new String("main");
        scene = new Scene(loadFXML(fxmlFileName));

        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.setTitle("GGAPF - Graph Generator and Path Finder");
        stage.show();
        stage.setResizable(false);

        controller = loader.getController();
        display = controller.initDisplay();

        // popup window setup
        popup = new Popup(scene);
    }

    /**
     * Shows popup window with given message
     * @param message message to be shown
     */
    public static void showPopup(String message) {
        popup.show(message);
    }
    
    /**
     * Hides popup window
     */
    public static void hidePopup() {
        popup.hide();
    }

    /**
     * Opens FileChooser window
     * @return the file selected by user
     */
    public static File chooseFile() {
        File initialDirectory = new File(System.getProperty("user.dir") + "/data");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open source File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialDirectory(initialDirectory);

        File selectedFile = fileChooser.showOpenDialog(stage);

        return selectedFile; 
    }

    /**
     * Tries to open the file selected by user.
     * Performs graph drawing.
     * @param selectedFile the file to be opened
     * @see Display
     */
    public static void open(File selectedFile) {
        try {
            Reader reader = new Reader(selectedFile);
            graph = reader.readGraph();
        } catch (IOException e) {
            showPopup("Something went wrong while reading the file");
        }

        if(graph == null) {
            showPopup("Can't read from given file!");
        } else {
            Main.resetFooter();
            Main.redrawGraph();
        }
    }

    /**
     * Tries to save graph to the file selected by user
     * @param selectedFile file to be saved
     */
    public static void save(File selectedFile) {
        if(graph == null) {
            showPopup("Load or generate a graph at first!");
            return;
        }

        try {
            Writer writer = new Writer(selectedFile, graph);
            if(writer.saveGraph()) {
                showPopup("Graph saved to the choosen file");
            }
        } catch (IOException e) {
            showPopup("Something went wrong while saving the file");
        }
    }

    /**
     * Runs graph generator with the given parameters.
     * After graph generation performs graph display.
     * @param rows rows number
     * @param columns columns number
     * @param minWeight minimal edge weight
     * @param maxWeight maximal edge weight
     * @param subgraphs number of subgraphs
     * @see Display
     */
    public static void generate(int rows, int columns, double minWeight, double maxWeight, int subgraphs) {
        graph = Generator.generate(rows, columns, minWeight, maxWeight, subgraphs);
        Main.resetFooter();
        Main.redrawGraph();
    }

    /**
     * Runs BFS algorithm on the graph
     * @return true when graph is connected, false in the other case
     * @see Graph
     */
    public static Boolean BFS() {
        if(graph == null) {
            Main.showPopup("Load or generate a graph at first!");
            return null;
        }

        if(graph.isGraphConnected(0)) 
            return true;
        else 
            return false;
    }

    /**
     * Runs Dijkstra algorithm on the graph.
     * Performs shortest path drawing.
     * @param startNode start node
     * @param endNode end node
     * @return ShortestPath object
     * @see Dijkstra
     * @see ShortestPath
     * @see Display
     */
    public static ShortestPath dijkstra(int startNode, int endNode) {
        if(graph == null) {
            Main.showPopup("Load or generate a graph at first!");
            return null;
        }
        
        ShortestPath shortestPath = Dijkstra.findShortestPath(graph, startNode, endNode);
        graph.setShortestPath(shortestPath);

        display.drawPathThrough(shortestPath.getPath());

        return shortestPath;
    }

    /**
     * Runs splitter on the graph
     * @param subgraphs number of subgraphs to be splitted
     * @see Splitter
     */
    public static void split(int subgraphs) {
        if(graph == null) {
            Main.showPopup("Load or generate a graph at first!");
            return;
        }

        graph = Splitter.split(graph, subgraphs);
    }
    /**
     * Redraws graph on the screen
     */
    public static void redrawGraph() {
        if(graph != null) {
            display.drawGraph(graph);
            controller.setHeatMapRange(graph.getMinWeight(), graph.getMaxWeight());
        }
    }

    /**
     * Sets start node for the Dijkstra algorithm
     * @param nodeNumber start node number
     */
    public static void setStartNodeForDijkstra(int nodeNumber) {
        controller.setStartNodeInfo(nodeNumber);
    }

    /**
     * Sets end node for the Dijkstra algorithm
     * @param nodeNumber end node number
     */
    public static void setEndNodeForDijkstra(int nodeNumber) {
        controller.setEndNodeInfo(nodeNumber);
    }

    /**
     * Clears selection
     * @param nodeNumber selected node number
     */
    public static void clearSelectionForDijkstra(int nodeNumber) {
        display.clearSelection(nodeNumber);
    }

    /**
     * Reset values in footer to defaults
     */
    public static void resetFooter() {
        controller.resetFooter();
    }

    /**
     * Loads fxml
     * @param fxml name of the FXML file
     * @return parent node
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        loader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    /**
     * The entry point to the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

}