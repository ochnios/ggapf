package ggapf;

public class Edge {
    double weight;
    int toNode;

    public Edge() {
        this.weight = -1.0;
        this.toNode = -1;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setToNode(int toNode) {
        this.toNode = toNode;
    }
}
