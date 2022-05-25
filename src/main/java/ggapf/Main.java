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
    private static Popup popup;
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

        // popup window setup
        popup = new Popup(scene);

        try {
            String pathname =  "test.txt";
            Reader reader = new Reader(pathname);
            Graph graph = reader.readGraph();
            graph.print();
            graph.isGraphConnected(0);
        } catch (Exception ex) {
            System.out.println("->ERROR_CODE: MAIN_READER_SET_UP");
            ex.printStackTrace();
        }
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

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}