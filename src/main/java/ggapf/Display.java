package ggapf;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
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
    private static final Color SELECTED_NODE = Color.RED;
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
        edgeLength = nodeRadius * 4;
        edgeThickness = nodeRadius / 2;
        spaceBetweenNodesCentres = (2 * nodeRadius + edgeLength);

        double nodeCenterX = spaceBetweenNodesCentres;
        double nodeCenterY = spaceBetweenNodesCentres;

        int currentNodeNumber = 0;
        Double edgeWeight;
        String edgeId;

        // draw nodes with edges - note that graph is bidirectional
        for(int i = 0; i < graph.getRows() - 1; i++) {
            for(int j = 0; j < graph.getColumns() - 1; j++) {
                // draw node
                nodes.add(addNode(nodeCenterX, nodeCenterY, currentNodeNumber));
                // draw right edge if exists
                edgeWeight = graph.getEdgeWeight(currentNodeNumber, currentNodeNumber + 1);
                if(edgeWeight != null) {
                    edgeId = generateEdgeId(currentNodeNumber, currentNodeNumber + 1);
                    edges.add(addRightEdge(nodeCenterX + nodeRadius, nodeCenterY, edgeWeight, edgeId));
                }
                // draw bottom edge if exists
                edgeWeight = graph.getEdgeWeight(currentNodeNumber, currentNodeNumber + graph.getColumns());
                if(edgeWeight != null) {
                    edgeId = generateEdgeId(currentNodeNumber, currentNodeNumber + graph.getColumns());
                    edges.add(addBottomEdge(nodeCenterX, nodeCenterY + nodeRadius, edgeWeight, edgeId));
                }

                currentNodeNumber++;
                nodeCenterX += spaceBetweenNodesCentres;
            }

            // last node and its bottom edge
            nodes.add(addNode(nodeCenterX, nodeCenterY, currentNodeNumber));
            edgeWeight = graph.getEdgeWeight(currentNodeNumber, currentNodeNumber + graph.getColumns());
            if(edgeWeight != null) {
                edgeId = generateEdgeId(currentNodeNumber, currentNodeNumber + graph.getColumns());
                edges.add(addBottomEdge(nodeCenterX, nodeCenterY + nodeRadius, edgeWeight, edgeId));
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
                edgeId = generateEdgeId(currentNodeNumber, currentNodeNumber + 1);
                edges.add(addRightEdge(nodeCenterX + nodeRadius, nodeCenterY, edgeWeight, edgeId));
            }

            nodeCenterX += spaceBetweenNodesCentres;
            currentNodeNumber++;
        }

        graphPane.getChildren().addAll(edges);
        graphPane.getChildren().addAll(nodes);
    }

    public void drawPathThrough(ArrayList<Integer> nodesOnPath) {
        if(nodesOnPath == null) return;
        
        int from;
        int to;
        String edgeId;

        for(int i = 0; i < nodesOnPath.size() - 1; i++) {
            from = nodesOnPath.get(i);
            to = nodesOnPath.get(i + 1);
            edgeId = generateEdgeId(from, to);
            if(!boldEdge(edgeId)) {
                // try in different way
                edgeId = generateEdgeId(to, from);
                boldEdge(edgeId);
            }
        }
    }

    private Boolean boldEdge(String edgeId) {
        Node wanted = graphPane.lookup("#" + edgeId);
        if(wanted != null) {
            Line edge = (Line)wanted;
            edge.setStroke(Color.WHITE);
            edge.setStrokeWidth(edgeThickness * 2);
            return true;
        }

        return false;
    }

    public void clearDisplay() {
        if(graphPane.getChildren().size() > 0) {
            graphPane.getChildren().clear();
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

    public void clearSelection(int nodeNumber) {
        if(nodes.size() >= nodeNumber)
            nodes.get(nodeNumber).setFill(NODE_FILL);
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
        double maxRadiusFromHeight = canvasWrapperHeight / ((rows + 1) * 6.0);
        double maxRadiusFromWidth = canvasWrapperWidth / ((columns + 1) * 6.0);

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
                if(event.getButton() == MouseButton.PRIMARY) 
                    Main.setStartNodeForDijkstra(nodeNumber);
                else if(event.getButton() == MouseButton.SECONDARY)
                    Main.setEndNodeForDijkstra(nodeNumber);

                nodes.get(nodeNumber).setFill(SELECTED_NODE);

                event.consume();
            }
        });

        return node;
    }

    private Line addRightEdge(double x, double y, double weight, String edgeId) {
        Line edge = new Line(x, y, x + edgeLength, y);
        edge.setId(edgeId);
        edge.setStrokeWidth(edgeThickness);
        edge.setStroke(getColorForWeight(weight));
        edge.toBack();
        return edge;
    }

    private Line addBottomEdge(double x, double y, double weight, String edgeId) {
        Line edge = new Line(x, y, x, y + edgeLength);
        edge.setId(edgeId);
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

    private String generateEdgeId(int from, int to) {
        return new String(from + "to" + to);
    }
}
