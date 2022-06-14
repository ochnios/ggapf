package ggapf;

import java.util.ArrayList;

/**
 * class which contains shortestPath length and actuall shortestPath found
 * using Dijkstra algorithm
 */
public class ShortestPath {
    private double pathLength;
    private ArrayList<Integer> path;

    static final double INFINITY = Double.MAX_VALUE;

    public ShortestPath(){
        this.pathLength = INFINITY; // infinity by default
        this.path = null;
    }

    public double getPathLength() {
        return this.pathLength;
    }

    public ArrayList<Integer> getPath() {
        return this.path;
    }

    public void setPathLength(double pathLength) {
        this.pathLength = pathLength;
    }

    public void setShortestPath(ShortestPath shortestPath) {
        this.pathLength = shortestPath.getPathLength();
        this.path = shortestPath.getPath();
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }
}