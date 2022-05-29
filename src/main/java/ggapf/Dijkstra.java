package ggapf;

import java.util.ArrayList;

public class Dijkstra extends ShortestPath {

    public static ShortestPath findShortestPath(Graph graph, int beginNode, int endNode){
        ShortestPath shortestPath = new ShortestPath();
        shortestPath.setPathLength(dijkstra(graph, beginNode, endNode));
        shortestPath.setPath(null);
        return shortestPath;
    }

   public static double dijkstra(Graph graph, int beginNode, int endNode) {
       double pathLength = 0.0;
       return pathLength;
   }

}