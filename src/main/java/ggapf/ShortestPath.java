package ggapf;

import java.util.ArrayList;

public class ShortestPath {
    private double pathLength;
    private ArrayList<Integer> path;

    public ShortestPath(){
        this.pathLength = 0.0;
        this.path = new ArrayList<Integer>();
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