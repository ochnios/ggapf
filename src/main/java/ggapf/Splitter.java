package ggapf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Splitter {

    static final int MAXIMUM_AMOUNT_OF_NEIGHBOURS = 4;
    static final boolean EDGE_NODE = true;
    static final boolean NOT_EDGE_NODE = false;


    public static Graph split(Graph graph, int subgraphs) {
        if(subgraphs <= 1) return graph;

        System.out.println("Splitting into " + subgraphs + " subgraphs...");
        
        int nodesAmount = graph.getNumberOfNodes();
        int startingNode;
        int endNode;
        ArrayList<Integer> seenNodes = new ArrayList<Integer>(nodesAmount);
        ArrayList<Integer> previousNodes = new ArrayList<Integer>(nodesAmount);
        ShortestPath shortestPath = new ShortestPath();

        //setting arrays
        for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(0);
            previousNodes.add(0);
		}

        for(int i = 1; i < subgraphs; i++) {

            for(int j = 0; j < nodesAmount; j++)
                seenNodes.set(j, Graph.UNSEEN_NODE);
            
            for(int j = 0; j < nodesAmount; j++)
                previousNodes.set(j, Graph.DEFAULT_NODE);

            startingNode = randomNode(nodesAmount);

            while( !isOnTheEdge(graph, startingNode) )
                startingNode = randomNode(nodesAmount);

            lookForTheSubgraph(graph, startingNode, seenNodes, previousNodes);

            do {
                endNode = randomNode(nodesAmount);
            } while( !(isOnTheEdge(graph, endNode)) && (seenNodes.get(endNode) == Graph.SEEN_NODE));

            shortestPath = Dijkstra.findShortestPath(graph, startingNode, endNode);

            splitter(graph, shortestPath, seenNodes, previousNodes);
            
        }

        


        return graph;
    }

    public static void splitter(Graph graph, ShortestPath shortestPath, ArrayList<Integer> seenNodes, ArrayList<Integer> previousNodes) {

    }

    public static void lookForTheSubgraph(Graph graph, int startingNode, ArrayList<Integer> seenNodes, ArrayList<Integer> previousNodes) {

		int nodesAmount = graph.getNumberOfNodes();
		int currentNode;
        Queue<Integer> queue = new LinkedList<Integer>(); 
		queue.add(startingNode);


		for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(0);
		}

		while( queue.peek() != null ) { 
			
			currentNode = queue.poll();
			seenNodes.set(currentNode, 1);
            for (Map.Entry<Integer, Double> edge : graph.getEdges(currentNode).entrySet()) {

                if((edge.getKey() != Graph.DEFAULT_NODE) && (seenNodes.get(edge.getKey()) == Graph.UNSEEN_NODE)) {
                    queue.add(edge.getKey());
                    seenNodes.set(edge.getKey(), Graph.SEEN_NODE);
                }
    
            }

		}

	}

    public static boolean isOnTheEdge(Graph graph, int node) {
        int connectionAmount = 0; //it's gona define if node is on the edge by the number of connections

        for (Map.Entry<Integer, Double> edge : graph.getEdges(node).entrySet())
			if(edge.getKey() != Graph.DEFAULT_NODE) 
                connectionAmount++;
			
        if(connectionAmount != MAXIMUM_AMOUNT_OF_NEIGHBOURS)
            return EDGE_NODE;

        return NOT_EDGE_NODE;
        
    }

    public static int randomNode(int nodesAmount) {
        return (int) Math.floor(Math.random()*(nodesAmount+1));
    }

}
