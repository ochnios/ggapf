package ggapf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;
    private static FXMLLoader loader;
    private static Controller controller;
    private static Popup popup;
    private static Display display;
    private static Reader reader;
    private static Graph graph;

    @Override
    public void start(Stage stage) throws IOException {
        // primary stage setup
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
        
        display.drawGraph(10, 20);

        // popup window setup
        popup = new Popup(scene);
    }

    public static void open(String pathname) {
        try {
            reader = new Reader(pathname);
            graph = reader.readGraph();
        } catch ( IOException e) {
            showPopup("Something went wrong while reading the file");
        }

        if(graph == null) {
            showPopup("Can't read from given file!");
        } else {
            showPopup("File opened");
        }
    }

    public static void showPopup(String message) {
        popup.show(message);
    }
    
    public static void hidePopup() {
        popup.hide();
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

    public static Boolean DFS() {
        if(graph == null) {
            Main.showPopup("Load or generate a graph at first!");
            return null;
        }

        graph.getShortestPath().setShortestPath(Dijkstra.findShortestPath(graph, 0, 1));
        System.out.println("SHORTEST PATH: " + graph.getShortestPath().getPathLength());
        return true;
    }


    private static Parent loadFXML(String fxml) throws IOException {
        loader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}