package ggapf;

import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Reader {
    private Graph graph;
    private File fileName;

    public Reader(File fileName) {
        System.out.println("->READER_SET_UP");
        this.fileName = fileName;
        System.out.println("->READER_SET^_UP");
    }

    public Graph readGraph() throws IOException {        
        //ADD CAN READ!!!
        System.out.println("->GRAPH_READING");
        int rows;
        int columns;

        String line;
        boolean firstLoop = true;
        int row = 0;
        int edges;
        int argsCounter;

        //parsing first 2 elements (rows, columns)
        BufferedReader reader = new BufferedReader( new FileReader(this.fileName) );

        while ( (line = reader.readLine()) != null ) {

            System.out.println("->AM READING! " + row);
            String [] words = line.split("(\\s+)|:");
            
            System.out.println(Arrays.toString(words));

            try {
                if(firstLoop == true) {
                    System.out.println("->FIRST_LOOP!");
                    rows = Integer.parseInt(words[0]);
                    columns = Integer.parseInt(words[1]);
                    this.graph = new Graph(rows, columns);
                    firstLoop = false;
                }
                else {
                    System.out.println("->SECOND_LOOP! " + row);
                    edges = words.length / 2;
                    argsCounter = 0;

                    for(int edgeCounter = 0; edgeCounter < edges; edgeCounter++) {
                        System.out.println("->EDGE_COUNTER " + argsCounter);
                        System.out.println(words[argsCounter] + " " + words[argsCounter+1]);
                        graph.getNode(row).getEdge(edgeCounter).setToNode(Integer.parseInt(words[argsCounter]));
                        graph.getNode(row).getEdge(edgeCounter).setWeight(Double.parseDouble(words[argsCounter + 1]));
                        System.out.println(graph.getNode(row).getEdge(edgeCounter).getToNode() + " " + graph.getNode(row).getEdge(edgeCounter).getWeight());
                        argsCounter += 2;
                    }

                    row++;

                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("->ERROR_READER_WHILE_READING");
            }
        }

        return graph;
    }
}