package ggapf;

import java.util.Map;
import java.util.TreeMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Stores graph
 */
public class Graph {
	static final int SEEN_NODE = 1;
	static final int UNSEEN_NODE = 0;
	static final int DEFAULT_NODE = -1;
	static final double DEFAULT_WEIGHT = -1.0;
	static final boolean CONNECTED_GRAPH = true;
	static final boolean NOT_CONNECTED_GRAPH = false;
	
	private TreeMap<Integer, TreeMap<Integer, Double>> nodes; // adjacency list
	private int rows;
	private int columns;
	private double minWeight = Double.MAX_VALUE;
	private double maxWeight = Double.MIN_VALUE;
	private Queue<Integer> queue; 
	private ArrayList<Integer> seenNodes;
	private ShortestPath shortestPath;

	/**
	 * Graph constructor - creates graph with given parameters
	 * @param rows number of rows
	 * @param columns number of columns
	 */
	public Graph(int rows, int columns) {
		this.nodes = new TreeMap<Integer, TreeMap<Integer, Double>>();
		this.queue = new LinkedList<Integer>();
		this.seenNodes = new ArrayList<Integer>(rows * columns);
		this.rows = rows;
		this.columns = columns;
		this.shortestPath = new ShortestPath();
	}

	/**
	 * @return number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @return number of columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @return number of nodes
	 */
	public int getNumberOfNodes() {
		return nodes.size();
	}

	/**
	 * @return the shortest path calculated by Dijkstra
	 * @see ShortestPath
	 * @see Dijkstra
	 */
	public ShortestPath getShortestPath() {
		return this.shortestPath;
	}

	/**
	 * Sets graphs shortest path
	 * @param shortestPath the shortest path in the graph
	 * @see ShortestPath
	 */
	public void setShortestPath(ShortestPath shortestPath) {
		this.shortestPath = shortestPath;
	}

	/**
	 * Adds edge to graph
	 * @param from start node of the edge
	 * @param to end node of the edge
	 * @param weight weight of the edge
	 */
	public void addEdge(int from, int to, double weight) {
		if(weight >= 0.0) {
			if(weight < minWeight) minWeight = weight;
			else if(weight > maxWeight) maxWeight = weight;
		}
		
		if (!(nodes.containsKey(from)))
			nodes.put(from, new TreeMap<Integer, Double>()); // add node

		if (nodes.get(from).containsKey(to))
			System.out.println("Edge from " + from + " to " + to + " already exists!");
		else
			nodes.get(from).put(to, weight);
	}

	/**
	 * Gets edge weight
	 * @param from start node of the edge
	 * @param to end node of the edge
	 * @return weith of the edge or null if edge doesn't exist
	 */
	public Double getEdgeWeight(int from, int to) {
		if (nodes.containsKey(from)) {
			if (nodes.get(from).containsKey(to)) {
				return nodes.get(from).get(to);
			}
		}

		return null;
	}

	/**
	 * Gets all edges outgoing from specific node
	 * @param from number of node
	 * @return TreeMap with edges outgoing from the given node or null when node doesn't exist
	 */
	public TreeMap<Integer, Double> getEdges(int from) {
		if (nodes.containsKey(from)) {
			return nodes.get(from);
		}

		return null;
	}

	/**
	 * Gets the entire adjacency list
	 * @return TreeMap that represents the entire adjacency list
	 */
	public TreeMap<Integer, TreeMap<Integer, Double>> getNodes() {
		return this.nodes;
	}

	/**
	 * Prints adjacency list on System.out
	 */
	public void print() {
		System.out.println(rows + " " + columns);
		for (Map.Entry<Integer, TreeMap<Integer, Double>> row : nodes.entrySet()) {
			for (Map.Entry<Integer, Double> edge : row.getValue().entrySet()) {
				if(edge.getKey() == Graph.DEFAULT_NODE) break;
				System.out.print(edge.getKey() + ":" + edge.getValue() + " ");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Sets minimal weight in the graph
	 * @param weight weight to be set
	 */
	public void setMinWeight(double weight) {
		this.minWeight = weight;
	}

	/**
	 * Sets maximal weight in the graph
	 * @param weight weight to be set
	 */
	public void setMaxWeight(double weight) {
		this.maxWeight = weight;
	}

	/**
	 * Gets minimal weight in the graph
	 * @return minimal weight in the graph
	 */
	public double getMinWeight() {
		return minWeight;
	}

	/**
	 * Gets maximal weight in the graph
	 * @return maximal weight in the graph
	 */
	public double getMaxWeight() {
		return maxWeight;
	}

	/**
	 * Necessary funcionality which uses BFS algorithm
	 * to define if the graph is connected
	 * @param startingNode node from which we are starting BFS
	 * @return info about graph being connected or not
	 */
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
		
		for (Map.Entry<Integer, Double> edge : getEdges(currentNode).entrySet()) {

			if((edge.getKey() != DEFAULT_NODE) && (seenNodes.get(edge.getKey()) == UNSEEN_NODE)) {
				queue.add(edge.getKey());
				seenNodes.set(edge.getKey(), SEEN_NODE);
			}

		}

	}
}