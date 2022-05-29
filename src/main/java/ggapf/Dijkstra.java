package ggapf;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Dijkstra extends ShortestPath {

    static final int SEEN_NODE = 1;
	static final int UNSEEN_NODE = 0;
	static final int PREV_UNKNOWN = -1;

    public static ShortestPath findShortestPath(Graph graph, int beginNode, int endNode){
        ShortestPath shortestPath = new ShortestPath();
        shortestPath.setPathLength(dijkstra(graph, beginNode, endNode));
        shortestPath.setPath(null);
        return shortestPath;
    }

    public static double dijkstra(Graph graph, int beginNode, int endNode) {

        if(beginNode < 0 || beginNode >= graph.getNumberOfNodes() || endNode < 0 || endNode >= graph.getNumberOfNodes()) {
            System.out.println("dijkstra(): given indexes out of range!");

            //dodanie wywalenia programu
        }

        double pathLength = 0.0;
        int nodesAmount = graph.getNumberOfNodes();

        int iterator;

        ArrayList<Double> distance = new ArrayList<Double>(nodesAmount);    // currently known shortest paths lengths
        ArrayList<Integer> seenNodes = new ArrayList<Integer>(nodesAmount); // marking examined nodes
        ArrayList<Integer> prev = new ArrayList<Integer>(nodesAmount);      // nodes throught which the shortest path

        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(); 

        int currentNode;
        int examinedNode;
        double newPathLength;

		for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(UNSEEN_NODE);
            distance.add(Double.MAX_VALUE);
            prev.add(PREV_UNKNOWN);
		}
        
        queue.add(beginNode);
        distance.set(beginNode, 0.0);
        prev.set(beginNode, beginNode);


       return pathLength;
    }

}