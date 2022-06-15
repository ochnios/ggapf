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

    /**
     * constructor which sets pathLength as infinity
     * and path as null (there is no path, no connections)
     */
    public ShortestPath(){
        this.pathLength = INFINITY; // infinity by default
        this.path = null;
    }

    /**
     * gets path length
     * @return path length
     */
    public double getPathLength() {
        return this.pathLength;
    }

    /**
     * gets path
     * @return array with the path
     */
    public ArrayList<Integer> getPath() {
        return this.path;
    }

    /**
     * sets path length
     * @param pathLength to be set
     */
    public void setPathLength(double pathLength) {
        this.pathLength = pathLength;
    }

    
    /**
     * sets path
     * @param path to be set
     */
    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }
}