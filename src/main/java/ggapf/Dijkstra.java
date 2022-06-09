package ggapf;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Map;

public class Dijkstra extends ShortestPath {

    static final int SEEN_NODE = 1;
	static final int UNSEEN_NODE = 0;
	static final int PREV_UNKNOWN = -1;

    public static ShortestPath findShortestPath(Graph graph, int beginNode, int endNode){
        ShortestPath shortestPath = new ShortestPath();
        shortestPath.setShortestPath(dijkstra(graph, beginNode, endNode));
        return shortestPath;
    }

    public static ShortestPath dijkstra(Graph graph, int beginNode, int endNode) {
        
        if(beginNode < 0 || beginNode >= graph.getNumberOfNodes() || endNode < 0 || endNode >= graph.getNumberOfNodes()) {
            System.out.println("dijkstra(): given indexes out of range!");

            //dodanie wywalenia programu
        }

        ShortestPath shortestPath = new ShortestPath();

        int nodesAmount = graph.getNumberOfNodes();

        ArrayList<Double> distance = new ArrayList<Double>(nodesAmount);    // currently known shortest paths lengths
        ArrayList<Integer> seenNodes = new ArrayList<Integer>(nodesAmount); // marking examined nodes
        ArrayList<Integer> previousNode = new ArrayList<Integer>(nodesAmount);

        PriorityQueue<NodeAndWeightPair> queue = new PriorityQueue<NodeAndWeightPair>(); 

        NodeAndWeightPair currentNode = new NodeAndWeightPair();
        NodeAndWeightPair examinedNode = new NodeAndWeightPair();
        double newPathLength;

		for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(UNSEEN_NODE);
            distance.add(Double.MAX_VALUE);
            previousNode.add(PREV_UNKNOWN);
		}

        currentNode.setNode(beginNode);
        currentNode.setWeight(0.0);
        
        queue.add(currentNode);
        distance.set(beginNode, 0.0);
        previousNode.set(beginNode, beginNode);

        while( (queue.size() > 0) && (currentNode.getNode() != endNode) ) {
            currentNode = queue.poll();
            seenNodes.set(currentNode.getNode(), SEEN_NODE);
            //nodes.get(currentNode) = getEdges(currentNode)
            for (Map.Entry<Integer, Double> edge : graph.getEdges(currentNode.getNode()).entrySet()) {
    
                examinedNode.setNode(edge.getKey());
                examinedNode.setWeight(edge.getValue());
    
                if(examinedNode.getNode() == Graph.DEFAULT_NODE || seenNodes.get(examinedNode.getNode()) == Graph.SEEN_NODE)
                    continue;

                newPathLength = distance.get(currentNode.getNode()) + examinedNode.getWeight();
                
                if(distance.get(examinedNode.getNode()) > newPathLength) {
                    distance.set(examinedNode.getNode(), newPathLength);
                    previousNode.set(examinedNode.getNode(), currentNode.getNode());
                    examinedNode.setWeight(newPathLength);
                    queue.add(examinedNode);
                }
            }


        }

        if(currentNode.getNode() == endNode) {
            shortestPath.setPathLength(distance.get(currentNode.getNode()));
            shortestPath.setPath(previousNode);
        }

        System.out.println("in:" + shortestPath.getPathLength());

        return shortestPath;
    }
}