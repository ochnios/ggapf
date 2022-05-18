package ggapf;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Reader {
    private Graph graph;
    private String fileName;

    public Reader(String fileName) {
        this.fileName = fileName;
    }

    public Graph readGraph() {        
        //ADD CAN READ!!!
        int rows;
        int columns;
        String rowsStr;
        String columnsStr;
        //parsing first 2 elements (rows, columns)
        Scanner scanner = new Scanner(this.fileName);
        rowsStr = scanner.next();
        rows = Integer.parseInt(rowsStr);
        columnsStr = scanner.next();
        columns = Integer.parseInt(columnsStr);

        this.graph = new Graph(rows, columns);

        while (scanner.hasNextLine()) {
            
        }

        return graph;
    }
}