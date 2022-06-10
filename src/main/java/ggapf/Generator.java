package ggapf;

import java.util.Random;

public class Generator {
    public static Graph generate(int rows, int columns, double minWeight, double maxWeight, int subgraphs) {
        Graph graph = new Graph(rows, columns);
        Random rand = new Random();

        int currentNode;
        double weight;

        // vertical edges
        for(int i = 0; i < rows - 1; i++) {
            for(int j = 0; j < columns; j++) {
                currentNode = (columns * i) + j;
                weight = minWeight + (maxWeight - minWeight) * rand.nextDouble();
                graph.addEdge(currentNode, currentNode + columns, weight);
                graph.addEdge(currentNode + columns, currentNode, weight);
            }
        }

        // horizontal edges
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns - 1; j++) {
                currentNode = (columns * i) + j;
                weight = minWeight + (maxWeight - minWeight) * rand.nextDouble();
                graph.addEdge(currentNode, currentNode + 1, weight);
                graph.addEdge(currentNode + 1, currentNode, weight);
            }
        }

        return graph;
    }
}
