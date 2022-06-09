package ggapf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Map;

public class Dijkstra extends ShortestPath {

    static final int SEEN_NODE = 1;
	static final int UNSEEN_NODE = 0;
	static final int PREV_UNKNOWN = -1;

    public static ShortestPath findShortestPath(Graph graph, int beginNode, int endNode){
        return dijkstra(graph, beginNode, endNode);
    }

    public static ShortestPath dijkstra(Graph graph, int beginNode, int endNode) {
        
        if(beginNode < 0 || beginNode >= graph.getNumberOfNodes() || endNode < 0 || endNode >= graph.getNumberOfNodes()) {
            System.out.println("internal error: dijkstra(): given indexes out of range!");
            return null;
        }

        ShortestPath shortestPath = new ShortestPath();

        int nodesAmount = graph.getNumberOfNodes();

        ArrayList<Double> distance = new ArrayList<Double>(nodesAmount);    // currently known shortest paths lengths
        ArrayList<Integer> seenNodes = new ArrayList<Integer>(nodesAmount); // marking examined nodes
        ArrayList<Integer> previousNodes = new ArrayList<Integer>(nodesAmount);

        PriorityQueue<NodeAndWeightPair> queue = new PriorityQueue<NodeAndWeightPair>(); 

        NodeAndWeightPair currentNode = new NodeAndWeightPair();
        NodeAndWeightPair examinedNode = new NodeAndWeightPair();
        double newPathLength;

		for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(UNSEEN_NODE);
            distance.add(Double.MAX_VALUE);
            previousNodes.add(PREV_UNKNOWN);
		}

        currentNode.setNode(beginNode);
        currentNode.setWeight(0.0);
        
        queue.add(currentNode);
        distance.set(beginNode, 0.0);
        previousNodes.set(beginNode, beginNode);

        while( (queue.size() > 0) && (currentNode.getNode() != endNode) ) {
            currentNode = queue.poll();
            seenNodes.set(currentNode.getNode(), SEEN_NODE);

            for (Map.Entry<Integer, Double> edge : graph.getEdges(currentNode.getNode()).entrySet()) {
                examinedNode = new NodeAndWeightPair(edge.getKey(), edge.getValue());
    
                if(examinedNode.getNode() == Graph.DEFAULT_NODE || seenNodes.get(examinedNode.getNode()) == Graph.SEEN_NODE)
                    continue;

                newPathLength = distance.get(currentNode.getNode()) + examinedNode.getWeight();
                
                if(distance.get(examinedNode.getNode()) > newPathLength) {
                    distance.set(examinedNode.getNode(), newPathLength);
                    previousNodes.set(examinedNode.getNode(), currentNode.getNode());
                    examinedNode.setWeight(newPathLength);
                    queue.add(examinedNode);
                }
            }
        }

        if(currentNode.getNode() == endNode) {
            shortestPath.setPathLength(distance.get(currentNode.getNode()));
            shortestPath.setPath(backtrace(previousNodes, beginNode, endNode));
        }

        return shortestPath;
    }

    // returns list of nodes on shortest path from start to the end
    private static ArrayList<Integer> backtrace(ArrayList<Integer> previousNodes, int begin, int end) {
        ArrayList<Integer> nodesOnShortestPath = new ArrayList<Integer>();
        int currentNode = end;
        while(currentNode != begin) {
            nodesOnShortestPath.add(currentNode);
            currentNode = previousNodes.get(currentNode);
        }
        nodesOnShortestPath.add(begin);
        
        Collections.reverse(nodesOnShortestPath);
        return nodesOnShortestPath;
    }
}