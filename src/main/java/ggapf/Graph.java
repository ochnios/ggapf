package ggapf;

import java.util.Map;
import java.util.TreeMap;

public class Graph {
	private TreeMap<Integer, TreeMap<Integer, Double>> nodes; // adjacency list
	private int rows;
    private int columns;

	public Graph(int rows, int columns) {
		this.nodes = new TreeMap<Integer, TreeMap<Integer, Double>>();
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
        if(!(nodes.containsKey(from)))
			nodes.put(from, new TreeMap<Integer, Double> ()); // add node

        if(nodes.get(from).containsKey(to))
            System.out.println("Edge from " + from + " to " + to + " already exists!");
        else 
		    nodes.get(from).put(to, weight);
    }

	public Double getEdgeWeight(int from, int to) {
		if(nodes.containsKey(from)) {
			if(nodes.get(from).containsKey(to)) {
				return nodes.get(from).get(to);
			}
		}

		return null;
	}

	public TreeMap<Integer, Double> getEdges(int from) {
		if(nodes.containsKey(from)) {
			return nodes.get(from);
		}

		return null;
	}

    public void print() {
        System.out.println(rows + " " + columns);
        for(Map.Entry<Integer, TreeMap<Integer, Double>> row : nodes.entrySet()) {
            for(Map.Entry<Integer, Double> edge : row.getValue().entrySet()) {
                System.out.print(edge.getKey() + ":" + edge.getValue() + " ");
            }
            System.out.print("\n");
        }
    }

	// TreeMap has removing by key (remove(Object key))
    // we can add more methods as needed - double getMaxWeight(int from), int getMaxWeightNode(int from), getMin...
}