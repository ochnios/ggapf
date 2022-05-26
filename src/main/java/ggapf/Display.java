package ggapf;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Display {
    private HBox canvasWrapper;
    private PannableCanvas canvas;
    private NodeGestures nodeGestures;
    private SceneGestures sceneGestures;

    private double canvasWrapperWidth;
    private double canvasWrapperHeight;
    private double canvasWrapperLayoutX;
    private double canvasWrapperLayoutY;

    private Pane graph;
    private ArrayList<Circle> nodes;
    private ArrayList<Line> edges;
    private double nodeRadius;
    private double edgeLength;
    private double edgeThickness;
    private Color nodeFill;

    public Display(HBox canvasWrapper) {
        this.canvasWrapper = canvasWrapper;
        canvasWrapperWidth = canvasWrapper.getWidth();
        canvasWrapperHeight = canvasWrapper.getHeight();
        canvasWrapperLayoutX = canvasWrapper.getLayoutX();
        canvasWrapperLayoutY = canvasWrapper.getLayoutY();

        canvas = new PannableCanvas(canvasWrapperWidth, canvasWrapperHeight);
        graph = new Pane();
        nodeGestures = new NodeGestures(canvas);
        sceneGestures = new SceneGestures(canvas);

        canvas.setLayoutX(canvasWrapperLayoutX);
        canvas.setLayoutY(canvasWrapperLayoutY);
        canvas.getChildren().add(graph);
        canvasWrapper.getChildren().add(canvas);

        addEventFilters();
        hideOverflow();

        nodes = new ArrayList<Circle>();
        edges = new ArrayList<Line>();

        nodeFill = Color.web("#efefef");
    }

    /*
     * drawGraph DEMO
     * edges should have appropriate colors
     * not all of nodes may have connetions
     * drawGraph should be redesigned to read info from Graph object and draw results
     * nodes should contain info about their number
     */
    public void drawGraph(int rows, int columns) {
        nodeRadius = calculateNodeRadius(rows, columns);
        edgeLength = nodeRadius * 2;
        edgeThickness = nodeRadius / 5;

        double spaceBetweenNodesCentres = (2 * nodeRadius + edgeLength);
        double nodeCenterX = spaceBetweenNodesCentres;
        double nodeCenterY = spaceBetweenNodesCentres;
        
        for(int i = 0; i < rows - 1; i++) {
            for(int j = 0; j < columns - 1; j++) {
                nodes.add(drawNode(nodeCenterX, nodeCenterY));
                edges.add(drawRightEdge(nodeCenterX + nodeRadius, nodeCenterY));
                edges.add(drawBottomEdge(nodeCenterX, nodeCenterY + nodeRadius));
                nodeCenterX += spaceBetweenNodesCentres;
            }
            nodes.add(drawNode(nodeCenterX, nodeCenterY)); // in last column node without right edge
            edges.add(drawBottomEdge(nodeCenterX, nodeCenterY + nodeRadius));
            nodeCenterY += spaceBetweenNodesCentres;
            nodeCenterX = spaceBetweenNodesCentres;
        }

        // in last row nodes without bottom edge
        for(int j = 0; j < columns - 1; j++) {
            nodes.add(drawNode(nodeCenterX, nodeCenterY));
            edges.add(drawRightEdge(nodeCenterX + nodeRadius, nodeCenterY));
            nodeCenterX += spaceBetweenNodesCentres;
        }
        nodes.add(drawNode(nodeCenterX, nodeCenterY));

        graph.getChildren().addAll(nodes);
        graph.getChildren().addAll(edges);
    }

    public void removeGraph() {
        graph.getChildren().removeAll(nodes);
    }

    private void addEventFilters() {
        canvasWrapper.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvasWrapper.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvasWrapper.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        graph.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        graph.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
    }

    private void hideOverflow() {
        Rectangle clip = new Rectangle(canvasWrapperWidth, canvasWrapperHeight);

        clip.setLayoutX(canvasWrapperLayoutX);
        clip.setLayoutY(canvasWrapperLayoutY);
        canvasWrapper.setClip(clip);
    }

    private double calculateNodeRadius(int rows, int columns) {
        double maxRadiusFromHeight = canvasWrapperHeight / ((rows + 1) * 4.0);
        double maxRadiusFromWidth = canvasWrapperWidth / ((columns + 1) * 4.0);

        if(maxRadiusFromHeight < maxRadiusFromWidth) 
            return maxRadiusFromHeight;
        else 
            return maxRadiusFromWidth;
    }


    private Circle drawNode(double x, double y){
        Circle node = new Circle(x, y, nodeRadius, nodeFill);
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Node clicked!");
                event.consume();
            }
        });

        return node;
    }

    private Line drawRightEdge(double x, double y) {
        Line edge = new Line(x, y, x + edgeLength, y);
        edge.setStrokeWidth(edgeThickness);
        edge.setStroke(nodeFill);
        edge.toBack();
        return edge;
    }

    private Line drawBottomEdge(double x, double y) {
        Line edge = new Line(x, y, x, y + edgeLength);
        edge.setStrokeWidth(edgeThickness);
        edge.setStroke(nodeFill);
        edge.toBack();
        return edge;
    }
}
