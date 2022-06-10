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
 * JavaFX App
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

    public static void showPopup(String message) {
        popup.show(message);
    }
    
    public static void hidePopup() {
        popup.hide();
    }

    public static File chooseFile() {
        File initialDirectory = new File(System.getProperty("user.dir") + "/data");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open source File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialDirectory(initialDirectory);

        File selectedFile = fileChooser.showOpenDialog(stage);

        return selectedFile; 
    }

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
            display.drawGraph(graph);
            controller.setHeatMapRange(graph.getMinWeight(), graph.getMaxWeight());
        }
    }

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

    public static void generate(int rows, int columns, double minWeight, double maxWeight, int subgraphs) {
        graph = Generator.generate(rows, columns, minWeight, maxWeight, subgraphs);
        Main.resetFooter();
        display.drawGraph(graph);
        controller.setHeatMapRange(graph.getMinWeight(), graph.getMaxWeight());
    }

    public static Boolean BFS() {
        if(graph == null) {
            Main.showPopup("Load or generate a graph at first!");
            return null;
        }

        if(graph.isGraphConnected(0)) 
            return true;
        else return 
            false;
    }

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

    public static void redrawGraph() {
        if(graph != null)
            display.drawGraph(graph);
    }

    public static void setStartNodeForDijkstra(int nodeNumber) {
        controller.setStartNodeInfo(nodeNumber);
    }

    public static void setEndNodeForDijkstra(int nodeNumber) {
        controller.setEndNodeInfo(nodeNumber);
    }

    public static void clearSelectionForDijkstra(int nodeNumber) {
        display.clearSelection(nodeNumber);
    }

    public static void resetFooter() {
        controller.resetFooter();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        loader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}