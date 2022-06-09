package ggapf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Writer {
    private Graph graph;
    private File file;
    
	static final int DEFAULT_NODE = -1;
	static final double DEFAULT_WEIGHT = -1.0;

    public Writer(File file, Graph graph) {
        this.file = file;
        this.graph = graph;
    }

    public boolean saveGraph() throws IOException {
        FileWriter output = new FileWriter(file);
        String headerLine = graph.getRows() + " " + graph.getColumns();
        String line;

        output.write(headerLine);
        for(int i = 0; i < graph.getNumberOfNodes(); i++) {
            line = new String("\n");
            for (Map.Entry<Integer, Double> edge : graph.getEdges(i).entrySet())
                line += (" " + edge.getKey() + ":" + edge.getValue());

            output.write(line);
        } 

        output.close();
        return true;
    }
}