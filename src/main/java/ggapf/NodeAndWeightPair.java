package ggapf;

/**
 * Class which contains node and the weigh of the edge which goes to the node
 */
public class NodeAndWeightPair implements Comparable<NodeAndWeightPair> {
    private int node;
    private double weight;

    /**
     * creates NodeAndWeightPair object which contains
     * node and the weight which goes to the node
     * @param node
     * @param weight
     */
    public NodeAndWeightPair(int node, double weight) {
        this.node = node;
        this.weight = weight;
    }

    /**
     * creates default NodeAndWeightPair object
     * - is no connection
     * to the created node
     */
    public NodeAndWeightPair() {
        this(Graph.DEFAULT_NODE, 0.0);
    }

    /**
     * gets node
     * @return node
     */
    public int getNode() {
        return this.node;
    }

    /**
     * gets weight
     * @return weight
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * sets node
     * @param node
     */
    public void setNode(int node) {
        this.node = node;
    }

    /**
     * sets weight
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * compares two weights of
     * the two given nodes
     */
    public int compareTo(NodeAndWeightPair nawp) {
        return this.getWeight() > nawp.getWeight() ? 1 : -1;
    }
}