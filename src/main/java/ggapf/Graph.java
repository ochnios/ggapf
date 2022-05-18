package ggapf;

public class Graph {
    private EdgesFromNode[] nodes;
    private int rows;
    private int columns;
    private int nodesAmount;

    public Graph(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.nodesAmount = rows * columns;
        this.nodes = new EdgesFromNode[nodesAmount];
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getNodesAmount() {
        return this.nodesAmount;
    }
}
