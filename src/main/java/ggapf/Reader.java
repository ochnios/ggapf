package ggapf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private Graph graph;
    private File file;

    public Reader(File file) {
        this.file = file;
    }

    public Graph readGraph() throws IOException {

        int rows, columns;

        String line;
        String words[];

        int rowNumber = 0;
        int edges;
        int argsCounter;

        if(file == null) return null;
        if(!(file.canRead())) return null;

        BufferedReader reader = new BufferedReader( new FileReader(file) );

        //parsing first 2 elements (rows, columns)
        line = reader.readLine();
        words = line.split("(\\s+)|:");

        rows = Integer.parseInt(words[0]);
        columns = Integer.parseInt(words[1]);
        this.graph = new Graph(rows, columns);

        // parsing edges
        while ( (line = reader.readLine()) != null ) {

            // remove whitespaces from the beggining of the line
            while(line.startsWith(" ")) {
                line = line.substring(1);
            }
            words = line.split("(\\s+)|:");
            try {
                edges = words.length / 2;
                argsCounter = 0;

                if(edges == 0) {
                    
                    int to = Graph.DEFAULT_NODE;
                    double weight = Graph.DEFAULT_WEIGHT;
                    graph.addEdge(rowNumber, to, weight);
                    
                }
                else {

                    for(int i = 0; i < edges; i++) {
                        int to = Integer.parseInt(words[argsCounter]);
                        double weight = Double.parseDouble(words[argsCounter + 1]);
                        graph.addEdge(rowNumber, to, weight);
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