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
    private static final Color NODE_FILL = Color.web("#efefef");
    private static final double BLUE_HUE = Color.BLUE.getHue();
    private static final double RED_HUE = Color.RED.getHue();

    private HBox canvasWrapper;
    private PannableCanvas canvas;
    private NodeGestures nodeGestures;
    private SceneGestures sceneGestures;

    private double canvasWrapperWidth;
    private double canvasWrapperHeight;
    private double canvasWrapperLayoutX;
    private double canvasWrapperLayoutY;

    private Pane graphPane;
    private ArrayList<Circle> nodes;
    private ArrayList<Line> edges;
    private double nodeRadius;
    private double edgeLength;
    private double edgeThickness;
    private double spaceBetweenNodesCentres;

    private double min_weight;
    private double max_weight;

    public Display(HBox canvasWrapper) {
        this.canvasWrapper = canvasWrapper;
        
        canvasWrapperWidth = canvasWrapper.getWidth();
        canvasWrapperHeight = canvasWrapper.getHeight();
        canvasWrapperLayoutX = canvasWrapper.getLayoutX();
        canvasWrapperLayoutY = canvasWrapper.getLayoutY();

        canvas = new PannableCanvas(canvasWrapperWidth, canvasWrapperHeight);
        graphPane = new Pane();
        nodeGestures = new NodeGestures(canvas);
        sceneGestures = new SceneGestures(canvas);

        canvas.setLayoutX(canvasWrapperLayoutX);
        canvas.setLayoutY(canvasWrapperLayoutY);
        canvas.getChildren().add(graphPane);
        canvasWrapper.getChildren().add(canvas);

        addEventFilters();
        hideOverflow();
    }

    public void drawGraph(Graph graph) {
        clearDisplay();

        min_weight = graph.getMinWeight();
        max_weight = graph.getMaxWeight();
        nodeRadius = calculateNodeRadius(graph.getRows(), graph.getColumns());
        edgeLength = nodeRadius * 2;
        edgeThickness = nodeRadius / 2;
        spaceBetweenNodesCentres = (2 * nodeRadius + edgeLength);

        double nodeCenterX = spaceBetweenNodesCentres;
        double nodeCenterY = spaceBetweenNodesCentres;

        int currentNodeNumber = 0;
        Double edgeWeight;

        // draw nodes with edges - note that graph is bidirectional
        for(int i = 0; i < graph.getRows() - 1; i++) {
            for(int j = 0; j < graph.getColumns() - 1; j++) {
                // draw node
                nodes.add(addNode(nodeCenterX, nodeCenterY, currentNodeNumber));
                // draw right edge if exists
                edgeWeight = graph.getEdgeWeight(currentNodeNumber, currentNodeNumber + 1);
                if(edgeWeight != null) {
                    edges.add(addRightEdge(nodeCenterX + nodeRadius, nodeCenterY, edgeWeight));
                }
                // draw bottom edge if exists
                edgeWeight = graph.getEdgeWeight(currentNodeNumber, currentNodeNumber + graph.getColumns());
                if(edgeWeight != null) {
                    edges.add(addBottomEdge(nodeCenterX, nodeCenterY + nodeRadius, edgeWeight));
                }

                currentNodeNumber++;
                nodeCenterX += spaceBetweenNodesCentres;
            }

            // last node and its bottom edge
            nodes.add(addNode(nodeCenterX, nodeCenterY, currentNodeNumber));
            edgeWeight = graph.getEdgeWeight(currentNodeNumber, currentNodeNumber + graph.getColumns());
            if(edgeWeight != null) {
                edges.add(addBottomEdge(nodeCenterX, nodeCenterY + nodeRadius, edgeWeight));
            }
            
            currentNodeNumber++;
            nodeCenterY += spaceBetweenNodesCentres;
            nodeCenterX = spaceBetweenNodesCentres;
        }

        // draw nodes in last row - without bottom edge
        for(int i = 0; i < graph.getColumns(); i++) {
            nodes.add(addNode(nodeCenterX, nodeCenterY, currentNodeNumber));
            
            // draw right edge if exists
            edgeWeight = graph.getEdgeWeight(currentNodeNumber, currentNodeNumber + 1);
            if(edgeWeight != null) {
                edges.add(addRightEdge(nodeCenterX + nodeRadius, nodeCenterY, edgeWeight));
            }

            nodeCenterX += spaceBetweenNodesCentres;
            currentNodeNumber++;
        }

        graphPane.getChildren().addAll(nodes);
        graphPane.getChildren().addAll(edges);
    }

    public void clearDisplay() {
        if(graphPane.getChildren().size() > 0) {
            graphPane.getChildren().removeAll(nodes);
            graphPane.getChildren().removeAll(edges);
        }

        canvas.setScale(1.0);
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        graphPane.setTranslateX(0);
        graphPane.setTranslateY(0);

        nodes = new ArrayList<Circle>();
        edges = new ArrayList<Line>();

        System.gc();
    }

    private void addEventFilters() {
        canvasWrapper.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvasWrapper.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvasWrapper.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        graphPane.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        graphPane.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
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


    private Circle addNode(double x, double y, int nodeNumber){
        Circle node = new Circle(x, y, nodeRadius, NODE_FILL);
        node.setId(Integer.toString(nodeNumber));
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("Node " + node.getId() + " clicked!");
                event.consume();
            }
        });

        return node;
    }

    private Line addRightEdge(double x, double y, double weight) {
        Line edge = new Line(x, y, x + edgeLength, y);
        edge.setStrokeWidth(edgeThickness);
        edge.setStroke(getColorForWeight(weight));
        edge.toBack();
        return edge;
    }

    private Line addBottomEdge(double x, double y, double weight) {
        Line edge = new Line(x, y, x, y + edgeLength);
        edge.setStrokeWidth(edgeThickness);
        edge.setStroke(getColorForWeight(weight));
        edge.toBack();
        
        return edge;
    }

    private Color getColorForWeight(double weight) {
        if (weight <  min_weight || weight > max_weight) {
            return Color.BLACK ;
        }
        double hue = BLUE_HUE + (RED_HUE - BLUE_HUE) * (weight - min_weight) / (max_weight - min_weight) ;
        return Color.hsb(hue, 1.0, 1.0);
    }
}
