package ggapf;

public class EdgesFromNode {
    private Edge[] edges = new Edge[4];

    public Edge getEdge( int edgeNumber ) {
        return this.edges[edgeNumber];
    }
}
