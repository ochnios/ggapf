package ggapf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javafx.scene.control.skin.TextInputControlSkin.Direction;

public class Splitter {

    static final int MAXIMUM_AMOUNT_OF_NEIGHBOURS = 4;
    static final boolean EDGE_NODE = true;
    static final boolean NOT_EDGE_NODE = false;
    static final int EDGE_CASE = 1;
    static final int NORMAL_CASE = 0;
    static final int NOT_EDGE_CASE = 0;
    static final int ON_THE_ROAD = 1;
    static final int NOT_ON_THE_ROAD = 0;
    static final int UP = 0;
    static final int RIGHT = 1;
    static final int DOWN = 2;
    static final int LEFT = 3;


    public static Graph split(Graph graph, int subgraphs) {
        if(subgraphs <= 1) return graph;

        System.out.println("Splitting into " + subgraphs + " subgraphs...");
        int nodesAmount = graph.getNumberOfNodes();
        int startingNode;
        int endNode;
        ArrayList<Integer> seenNodes = new ArrayList<Integer>(nodesAmount);
        ArrayList<Integer> previousNodes = new ArrayList<Integer>(nodesAmount);
        ShortestPath shortestPath = new ShortestPath();

        //setting arrays
        for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(0);
            previousNodes.add(0);
		}

        for(int i = 1; i < subgraphs; i++) {

            for(int j = 0; j < nodesAmount; j++)
                seenNodes.set(j, Graph.UNSEEN_NODE);
            
            for(int j = 0; j < nodesAmount; j++)
                previousNodes.set(j, Graph.DEFAULT_NODE);

            startingNode = randomNode(nodesAmount);

            while( !isOnTheEdge(graph, startingNode) )
                startingNode = randomNode(nodesAmount);

            lookForTheSubgraph(graph, startingNode, seenNodes, previousNodes);

            do {
                endNode = randomNode(nodesAmount);
            } while( !(isOnTheEdge(graph, endNode)) && (seenNodes.get(endNode) == Graph.SEEN_NODE));

            shortestPath = Dijkstra.findShortestPath(graph, startingNode, endNode);

            System.out.println("splitter() - start");

            splitter(graph, shortestPath, seenNodes, previousNodes, startingNode, endNode);
            
        }

        


        return graph;
    }

    public static void splitter(Graph graph, ShortestPath shortestPath, ArrayList<Integer> seenNodes, ArrayList<Integer> previousNodes, int nodeFrom, int nodeTo) {

        int nodesAmount = graph.getNumberOfNodes();
        int columns = graph.getColumns();
        int rows = graph.getRows();
        int currentNode = nodeTo;
        int endNode = nodeFrom;
        int followingNode;
        int nodeToCut;
        int way;
        int mode = EDGE_CASE;
        ArrayList<Integer> road = new ArrayList<>(nodesAmount);

        System.out.println("SPLITO STARTO");

        for(int i = 0; i < nodesAmount; i++)
            road.add(NOT_ON_THE_ROAD);

        System.out.println("Added.");

        road.set(currentNode, ON_THE_ROAD);
        road.set(endNode, ON_THE_ROAD);

        System.out.println("some basic stuff");

        while( currentNode != endNode ) {
            road.set(currentNode, ON_THE_ROAD);
            currentNode = previousNodes.get(currentNode);
        }

        System.out.println("everything ready to go");

        currentNode = nodeTo;

        while( currentNode != endNode ) {

            System.out.println("CASE DEFINING");

            if( ((currentNode % columns) == (columns - 1)) || ((currentNode < columns)) ) {
                currentNode = previousNodes.get(currentNode);
            }
            else {
                mode = NORMAL_CASE;
                break;
            }

        }

        currentNode = nodeTo;

        if( mode == NORMAL_CASE ) {

            System.out.println("NORMAL CASE STARTO");

            for (Map.Entry<Integer, Double> edge : graph.getEdges(currentNode).entrySet()) {
                nodeToCut = edge.getKey();
    
                if( (nodeToCut != Graph.DEFAULT_NODE) && (road.get(nodeToCut) == NOT_ON_THE_ROAD) ) {

                    if(graph.getEdges(currentNode).containsKey(nodeToCut))
                        graph.getEdges(currentNode).remove(nodeToCut);
                
                    if(graph.getEdges(nodeToCut).containsKey(currentNode))
                        graph.getEdges(nodeToCut).remove(currentNode);
                
                }

            }

            System.out.println("FIRST CUT LATER");

            currentNode = nodeTo;

            while( currentNode != endNode ) {

                followingNode = previousNodes.get(currentNode);

                way = direction(rows, columns, currentNode, followingNode);

                if( way == UP ) {
                    //looking for the right connection to break
                    cutter( graph, currentNode, currentNode + 1, road);
    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode + 1, road);
    
                }
                else if( way == RIGHT ) {
                    //looking for the down connection to break
                    cutter( graph, currentNode, currentNode + columns, road);
    
                    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode + columns, road);
    
    
                }
                else if( way == DOWN ) {
                    //looking for the down connection to break
                    cutter( graph, currentNode, currentNode - 1, road);
    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode - 1, road);
                            
                }
                else if( way == LEFT ) {
                    //looking for the down connection to break
                    cutter( graph, currentNode, currentNode - columns, road);
    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode - columns, road);
                            
                }

                currentNode = previousNodes.get(currentNode);

            }

            System.out.println("CUT.");

            for (Map.Entry<Integer, Double> edge : graph.getEdges(endNode).entrySet()) {
                nodeToCut = edge.getKey();
    
                if( (nodeToCut != Graph.DEFAULT_NODE) && (road.get(nodeToCut) == NOT_ON_THE_ROAD) ) {

                    if(graph.getEdges(endNode).containsKey(nodeToCut))
                        graph.getEdges(endNode).remove(nodeToCut);
                
                    if(graph.getEdges(nodeToCut).containsKey(endNode))
                        graph.getEdges(nodeToCut).remove(endNode);
                
                }

            }

            System.out.println("DONE.");

        }
        else {
            while( currentNode != endNode ) {

                System.out.println("");

                followingNode = previousNodes.get(currentNode);
    
                way = direction( rows, columns, currentNode, followingNode );
    
                if( way == UP ) {
                    //looking for the down connection to break
                    cutter( graph, currentNode, currentNode - 1, road);
    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode - 1, road);
    
                }
                else if( way == RIGHT ) {
                    //looking for the down connection to break
                    cutter( graph, currentNode, currentNode - columns, road);
    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode - columns, road);
    
                }
                else if( way == DOWN ) {
                    //looking for the right connection to break
                    cutter( graph, currentNode, currentNode + 1, road);
    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode + 1, road);
                            
                }
                else if( way == LEFT ) {
                    //looking for the down connection to break
                    cutter( graph, currentNode, currentNode + columns, road);
    
                    
                    //deleting connection from previously deleted node
                    cutter( graph, followingNode, followingNode + columns, road);   
                }
            }
    
            currentNode = previousNodes.get(currentNode);
        }
        

    }

    public static void  cutter(Graph graph, int currentNode, int nodeToCut, ArrayList<Integer> road) {

        System.out.println("cuttero!!!: " + currentNode);

        if( (nodeToCut != Graph.DEFAULT_NODE) && (graph.getEdges(currentNode).containsKey(nodeToCut)) && (road.get(nodeToCut) == NOT_ON_THE_ROAD)) {
            
            if(graph.getEdges(currentNode).containsKey(nodeToCut))
                graph.getEdges(currentNode).remove(nodeToCut);

            if(graph.getEdges(nodeToCut).containsKey(currentNode))
                graph.getEdges(nodeToCut).remove(currentNode);
        }

        

    }

    public static int direction(int rows, int columns, int currentNode, int followingNode ) {



        if( followingNode == ( currentNode - columns ) )
            return UP;
        
        if( followingNode == ( currentNode + 1 ) )
            return RIGHT;
        
        if( followingNode == ( currentNode + columns ) )
            return DOWN;
        
        if( followingNode == ( currentNode - 1 ) )
            return LEFT;

        return 0;
    
    
    }

    public static void lookForTheSubgraph(Graph graph, int startingNode, ArrayList<Integer> seenNodes, ArrayList<Integer> previousNodes) {

		int nodesAmount = graph.getNumberOfNodes();
		int currentNode;
        Queue<Integer> queue = new LinkedList<Integer>(); 
		queue.add(startingNode);


		for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(0);
		}

		while( queue.peek() != null ) { 
			
			currentNode = queue.poll();
			seenNodes.set(currentNode, 1);
            for (Map.Entry<Integer, Double> edge : graph.getEdges(currentNode).entrySet()) {

                if((edge.getKey() != Graph.DEFAULT_NODE) && (seenNodes.get(edge.getKey()) == Graph.UNSEEN_NODE)) {
                    queue.add(edge.getKey());
                    seenNodes.set(edge.getKey(), Graph.SEEN_NODE);
                }
    
            }

		}

	}

    public static boolean isOnTheEdge(Graph graph, int node) {
        int connectionAmount = 0; //it's gona define if node is on the edge by the number of connections

        for (Map.Entry<Integer, Double> edge : graph.getEdges(node).entrySet())
			if(edge.getKey() != Graph.DEFAULT_NODE) 
                connectionAmount++;
			
        if(connectionAmount != MAXIMUM_AMOUNT_OF_NEIGHBOURS)
            return EDGE_NODE;

        return NOT_EDGE_NODE;
        
    }

    public static int randomNode(int nodesAmount) {
        return (int) Math.floor(Math.random()*(nodesAmount+1));
    }

}
