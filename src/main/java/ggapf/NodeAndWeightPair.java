package ggapf;

/**
 * Class which contains node and the weigh of the edge which goes to the node
 */
public class NodeAndWeightPair implements Comparable<NodeAndWeightPair> {
    private int node;
    private double weight;

    public NodeAndWeightPair(int node, double weight) {
        this.node = node;
        this.weight = weight;
    }

    public NodeAndWeightPair() {
        this(Graph.DEFAULT_NODE, 0.0);
    }

    public int getNode() {
        return this.node;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int compareTo(NodeAndWeightPair nawp) {
        return this.getWeight() > nawp.getWeight() ? 1 : -1;
    }
}