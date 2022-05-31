package ggapf;

public class NodeAndWeightPair implements Comparable<NodeAndWeightPair> {
    private int node;
    private double weight;

    public NodeAndWeightPair() {
        this.node = -1;
        this.weight = 0.0;
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