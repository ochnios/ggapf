package ggapf;

import java.util.Map;
import java.util.TreeMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

public class Graph {
	private TreeMap<Integer, TreeMap<Integer, Double>> nodes; // adjacency list
	private int rows;
	private int columns;
	private Queue<Integer> queue; 
	private ArrayList<Integer> seenNodes;

	static final int SEEN_NODE = 1;
	static final int UNSEEN_NODE = 0;
	static final int DEFAULT_NODE = -1;
	static final double DEFAULT_WEIGHT = -1.0;
	static final boolean CONNECTED_GRAPH = true;
	static final boolean NOT_CONNECTED_GRAPH = false;

	public Graph(int rows, int columns) {
		this.nodes = new TreeMap<Integer, TreeMap<Integer, Double>>();
		this.queue = new LinkedList<Integer>();
		this.seenNodes = new ArrayList<Integer>(rows * columns);
		this.rows = rows;
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public int getNumberOfNodes() {
		return nodes.size();
	}

	public void addEdge(int from, int to, double weight) {
		if (!(nodes.containsKey(from)))
			nodes.put(from, new TreeMap<Integer, Double>()); // add node

		if (nodes.get(from).containsKey(to))
			System.out.println("Edge from " + from + " to " + to + " already exists!");
		else
			nodes.get(from).put(to, weight);
	}

	public Double getEdgeWeight(int from, int to) {
		if (nodes.containsKey(from)) {
			if (nodes.get(from).containsKey(to)) {
				return nodes.get(from).get(to);
			}
		}

		return null;
	}

	public TreeMap<Integer, Double> getEdges(int from) {
		if (nodes.containsKey(from)) {
			return nodes.get(from);
		}

		return null;
	}

	public void print() {
		System.out.println(rows + " " + columns);
		for (Map.Entry<Integer, TreeMap<Integer, Double>> row : nodes.entrySet()) {
			//System.out.println(row);
			for (Map.Entry<Integer, Double> edge : row.getValue().entrySet()) {
				System.out.print(edge.getKey() + ":" + edge.getValue() + " ");
			}
			System.out.print("\n");
		}
	}

	public boolean isGraphConnected(int startingNode) {

		queue.add(startingNode);

		int nodesAmount = getNumberOfNodes();
		int currentNode;

		for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(0);
		}

		while( this.queue.peek() != null ) { 
			
			currentNode = queue.poll();
			seenNodes.set(currentNode, 1);
			lookForTheSurroundingNodes(currentNode);

		}

		boolean connectedGraph = lookIfEveryNodeHasBeenVisited();

		System.out.println("IS GRAPH CONNECTED?: " + connectedGraph);

		return connectedGraph;
	}

	public boolean lookIfEveryNodeHasBeenVisited() {

		int nodesAmount = getNumberOfNodes();

		for(int i = 0; i < nodesAmount; i++)
			if(seenNodes.get(i) == UNSEEN_NODE)
				return NOT_CONNECTED_GRAPH;

		return CONNECTED_GRAPH;
	}

	public void lookForTheSurroundingNodes (int currentNode) {
		
		//nodes.get(currentNode) = getEdges(currentNode)
		for (Map.Entry<Integer, Double> edge : getEdges(currentNode).entrySet()) {

			if((edge.getKey() != DEFAULT_NODE) && (seenNodes.get(edge.getKey()) == UNSEEN_NODE)) {
				queue.add(edge.getKey());
				seenNodes.set(edge.getKey(), SEEN_NODE);
				//System.out.println("Added: [" + edge.getKey() + "] to the queue.");
			}

		}

	}

	// TreeMap has removing by key (remove(Object key))
	// we can add more methods as needed - double getMaxWeight(int from), int
	// getMaxWeightNode(int from), getMin...
}