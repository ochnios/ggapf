package ggapf;

import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private Graph graph;
    private String pathname;
    
	static final int DEFAULT_NODE = -1;
	static final double DEFAULT_WEIGHT = -1.0;

    public Reader(String pathname) {
        //System.out.println("->READER_SET_UP");
        this.pathname = pathname;
    }

    public Graph readGraph() throws IOException {
        //System.out.println("->GRAPH_READING");
        int rows, columns;

        String line;
        String words[];

        int rowNumber = 0;
        int edges;
        int argsCounter;

        File file = new File(this.pathname);
        if(!(file.canRead())) {
            System.out.println("CANNOT READ FROM GIVEN FILE");
            return null;
        }

        BufferedReader reader = new BufferedReader( new FileReader(file) );

        //parsing first 2 elements (rows, columns)
        line = reader.readLine();
        words = line.split("(\\s+)|:");
        //System.out.println(Arrays.toString(words));

        rows = Integer.parseInt(words[0]);
        columns = Integer.parseInt(words[1]);
        this.graph = new Graph(rows, columns);

        // parsing edges
        while ( (line = reader.readLine()) != null ) {
            //System.out.println("->AM READING! " + rowNumber);

            // remove whitespaces from the beggining of the line
            while(line.startsWith(" ")) {
                line = line.substring(1);
            }
            words = line.split("(\\s+)|:");
            try {
                //System.out.println("->SECOND_LOOP! " + rowNumber);
                edges = words.length / 2;
                argsCounter = 0;

                if(edges == 0) {
                    
                    int to = DEFAULT_NODE;
                    double weight = DEFAULT_WEIGHT;
                    graph.addEdge(rowNumber, to, weight);
                    
                }
                else {

                    for(int i = 0; i < edges; i++) {
                        //System.out.println("->EDGE_COUNTER: " + argsCounter);
                        //System.out.println("from: " + rowNumber);
                        //System.out.println(words[argsCounter] + " " + words[argsCounter+1]);

                        int to = Integer.parseInt(words[argsCounter]);
                        double weight = Double.parseDouble(words[argsCounter + 1]);
                        graph.addEdge(rowNumber, to, weight);

                        //System.out.println("added edge from " + rowNumber + " to " + to + " with weight " + weight);
                        argsCounter += 2;
                    }

                }
                rowNumber++;

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("->ERROR_READER_WHILE_READING");
            }
        }

        reader.close();
        return graph;
    }
}