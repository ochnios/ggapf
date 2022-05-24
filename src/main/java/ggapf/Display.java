package ggapf;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Display {
    private Pane canvasPane;
    private ArrayList<Circle> nodes;
    private ArrayList<Line> edges;
    private double nodeRadius;
    private double edgeLength;
    private Color nodeFill;

    public Display(Pane canvasPane) {
        this.canvasPane = canvasPane;

        nodes = new ArrayList<Circle>();
        edges = new ArrayList<Line>();

        nodeRadius = 10.0;
        edgeLength = 20.0;
        nodeFill = Color.web("#efefef");
    }

    public void drawGraph(int rows, int columns) {
        double x = 0.0;
        double y = 0.0;
        double space = (nodeRadius + edgeLength);
        for(int i = 0; i < rows; i++) {
            x += space;
            y = 0.0;
            for(int j = 0; j < columns; j++) {
                y += space;
                nodes.add(drawNode(x, y));
                //edges.add(drawEdge(x, y));
            }
        }

        canvasPane.getChildren().addAll(nodes);
    }

    public void removeGraph() {
        canvasPane.getChildren().removeAll(nodes);
    }

    private Circle drawNode(double x, double y){
        return new Circle(x, y, nodeRadius, nodeFill);
    }

    // private Edge drawEdge(double x, double y) {
    //     return new Line();
    // }
}
