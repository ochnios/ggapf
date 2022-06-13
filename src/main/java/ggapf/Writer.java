package ggapf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Responsible for saving graph to the file
 */
public class Writer {
    private Graph graph;
    private File file;
    
	static final int DEFAULT_NODE = -1;
	static final double DEFAULT_WEIGHT = -1.0;

    /**
     * Creates an instance of Writer
     * @param file File to write
     * @param graph graph to be written
     */
    public Writer(File file, Graph graph) {
        this.file = file;
        this.graph = graph;
    }

    /**
     * saves graph to the file
     * @return true on success
     * @throws IOException
     */
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