package ggapf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Splits graph
 */
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

    /**
     * Finds best nodes to start spliting graph, finds the fastes route
     * between them using Dijkstra algorithm and splits the graph
     * @param graph graph we are actually working on
     * @param subgraphs amount of subgraphs we want our graph to be divided to
     * @return splitted graph
     */
    public static Graph split(Graph graph, int subgraphs) {
        if(subgraphs <= 1) return graph;

        System.out.println("Splitting into " + subgraphs + " subgraphs...");
        int nodesAmount = graph.getNumberOfNodes();
        int startingNode;
        int endNode;
        ArrayList<Integer> seenNodes = new ArrayList<Integer>(nodesAmount);
        ShortestPath shortestPath = new ShortestPath();

        //setting arrays
        for(int i = 0; i < nodesAmount; i++) {
			seenNodes.add(0);
		}

        for(int i = 1; i < subgraphs; i++) {

            for(int j = 0; j < nodesAmount; j++)
                seenNodes.set(j, Graph.UNSEEN_NODE);

            startingNode = randomNode(nodesAmount);

            while( !isOnTheEdge(graph, startingNode) )
                startingNode = randomNode(nodesAmount);

            lookForTheSubgraph(graph, startingNode, seenNodes);

            do {
                endNode = randomNode(nodesAmount);
                //System.out.println("siema");
            } while( !(isOnTheEdge(graph, endNode)) || (seenNodes.get(endNode) != Graph.SEEN_NODE) 
                    || (startingNode == endNode) || ( (endNode % graph.getColumns()) == (startingNode % graph.getColumns()) ) 
                    || ((endNode > startingNode) && (endNode <= startingNode + graph.getRows())));

            shortestPath = Dijkstra.findShortestPath(graph, startingNode, endNode);

            //System.out.println("splitter() - start");

            splitter(graph, shortestPath.getPath(), seenNodes);
            
        }

        


        return graph;
    }

    /**
     * Part responsible for the cutting graph into subgraphs
     * @param graph graph we are actually working on
     * @param previousNodes array with the fastest route
     * @param seenNodes array of seen nodes on the route
     */
    public static void splitter(Graph graph,  ArrayList<Integer> previousNodes, ArrayList<Integer> seenNodes) {

        int nodesAmount = graph.getNumberOfNodes();
        int columns = graph.getColumns();
        int rows = graph.getRows();
        int roadSize = previousNodes.size();
        Integer currentNode;
        Integer endNode;
        Integer followingNode;
        Integer nodeToCut;
        int way;
        int mode = EDGE_CASE;
        ArrayList<Integer> road = new ArrayList<>(nodesAmount);
        Queue<Integer> queue = new LinkedList<Integer>(); 

        //System.out.println("SPLITO STARTO");

        for(int i = 0; i < nodesAmount; i++)
            road.add(NOT_ON_THE_ROAD);

        //System.out.println("Added.");

        currentNode = previousNodes.get(0);
        endNode = previousNodes.get(roadSize - 1);

        road.set(currentNode, ON_THE_ROAD);
        road.set(endNode, ON_THE_ROAD);

        //System.out.println("startingNode: " + currentNode + " endNode: " + endNode);

        //for(int i = 0; i < roadSize; i++)
        //    System.out.print(i + "[" + previousNodes.get(i) + "], ");

        for(int i = 0; i < roadSize; i++) {

            currentNode = previousNodes.get(i);
            //System.out.println("currentNode:" + currentNode);
            road.set(currentNode, ON_THE_ROAD);

        }

        //System.out.println("everything ready to go");

        currentNode = previousNodes.get(0);

        for(int i = 0; i < roadSize; i++) {

            //System.out.println("CASE DEFINING");

            if( ((currentNode % columns) == (columns - 1)) || ((currentNode < columns)) ) {
                currentNode = previousNodes.get(i);
            }
            else {
                mode = NORMAL_CASE;
                break;
            }

        }

        currentNode = previousNodes.get(0);

        if( mode == NORMAL_CASE ) {

            //System.out.println("NORMAL CASE STARTO");

            for (Map.Entry<Integer, Double> edge : graph.getEdges(currentNode).entrySet()) {
                nodeToCut = edge.getKey();

                //System.out.println("NodeTOCUTTT " + nodeToCut);
    
                if( (nodeToCut != Graph.DEFAULT_NODE) && (road.get(nodeToCut) == NOT_ON_THE_ROAD) ) {
                    queue.add(nodeToCut);

                    //System.out.println("added to queue. " + nodeToCut); 
                }

            }

            removeEdges(graph, queue, currentNode);

            //System.out.println("FIRST CUT LATER");

            for(int i = 0; i < roadSize - 1; i++) {
            
                currentNode = previousNodes.get(i);

                followingNode = previousNodes.get(i + 1);

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

          

            }

            //System.out.println("CUT.");

            for (Map.Entry<Integer, Double> edge : graph.getEdges(endNode).entrySet()) {
                nodeToCut = edge.getKey();
    
                if( (nodeToCut != Graph.DEFAULT_NODE) && (road.get(nodeToCut) == NOT_ON_THE_ROAD) ) {

                    queue.add(nodeToCut);

                    //System.out.println("added to final queue");
                
                }

            }

            removeEdges(graph, queue, endNode);

            //System.out.println("DONE.");

        }
        else {

            System.out.println("OTHER CASE.");

            for(int i = 0; i < roadSize; i++) {

                currentNode = previousNodes.get(i);

                followingNode = previousNodes.get(i + 1);
    
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

        }
        

    }

    /**
     * removes edges from the currentNode
     * @param graph graph we are actually working on
     * @param queue queue which contains nodes to be cut
     * @param currentNode node from which we are cutting the edges
     */
    public static void removeEdges(Graph graph, Queue<Integer> queue, Integer currentNode) {
        Integer x;
        while(queue.peek() != null) {
            x = queue.poll();
            if(graph.getEdges(currentNode).containsKey(x))
                graph.getEdges(currentNode).remove(x);
            if(graph.getEdges(x).containsKey(currentNode))
                graph.getEdges(x).remove(currentNode);
        }
    }

    /**
     * removes specific connection between nodes
     * @param graph graph we are actually working on
     * @param currentNode node between which we want to cut the connection
     * @param nodeToCut node between which we want to cut the connection
     * @param road array which knows if node is a part of the road
     */
    public static void  cutter(Graph graph, int currentNode, int nodeToCut, ArrayList<Integer> road) {

        //System.out.println("cuttero!!!: " + currentNode);

        if( (nodeToCut != Graph.DEFAULT_NODE) && (graph.getEdges(currentNode).containsKey(nodeToCut)) && (road.get(nodeToCut) == NOT_ON_THE_ROAD)) {
            
            if(graph.getEdges(currentNode).containsKey(nodeToCut))
                graph.getEdges(currentNode).remove(nodeToCut);

            if(graph.getEdges(nodeToCut).containsKey(currentNode))
                graph.getEdges(nodeToCut).remove(currentNode);
        }

        

    }
    
    /**
     * Specifies the direction the shortest path goes
     * @param rows amount of rows in the graph
     * @param columns amount of columns in the graph
     * @param currentNode 
     * @param followingNode
     * @return the direction
     */
    public static int direction(int rows, int columns, int currentNode, int followingNode ) {

        if( followingNode == ( currentNode - columns ) )
            return UP;
        
        if( followingNode == ( currentNode + 1 ) )
            return RIGHT;
        
        if( followingNode == ( currentNode + columns ) )
            return DOWN;
        
        if( followingNode == ( currentNode - 1 ) )
            return LEFT;

        return -1;
    
    }

    /**
     * looks for the subgraph of the given graph
     * @param graph graph we are actually working on
     * @param startingNode node from which we are looking for the subgraph
     * @param seenNodes array containing info about subgraph's nodes
     */
    public static void lookForTheSubgraph(Graph graph, int startingNode, ArrayList<Integer> seenNodes) {

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

    /**
     * defines if a node is on the edge or not
     * @param graph graph we are actually working on
     * @param node node we want to know if it's a part of the edge
     * @return info about node being part of the edge
     */
    public static boolean isOnTheEdge(Graph graph, int node) {
        int connectionAmount = 0; //it's gona define if node is on the edge by the number of connections

        for (Map.Entry<Integer, Double> edge : graph.getEdges(node).entrySet())
			if(edge.getKey() != Graph.DEFAULT_NODE) 
                connectionAmount++;
			
        if(connectionAmount != MAXIMUM_AMOUNT_OF_NEIGHBOURS)
            return EDGE_NODE;

        return NOT_EDGE_NODE;
        
    }

    /**
     * randomizes node
     * @param nodesAmount amount of the nodes in graph
     * @return random node
     */
    public static int randomNode(int nodesAmount) {
        return (int) Math.floor(Math.random()*(nodesAmount+1));
    }

}
