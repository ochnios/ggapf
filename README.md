# GGAPF
GGAPF - Graph Generator And Path Finder - JIMP project (Programming Languages and Methods course).

GGAPF is based on Java with JavaFX and uses Maven as a build tool.

## What GGAPF can do?
GGAPF can:
1. generate a graph with a given number of columns and rows of nodes and weights of random edges in a given range of values.
2. save such a graph to a file with a fixed format
3. read such a graph from a fixed-format file
4. check if a given graph is consistent (BFS algorithm)
5. find in a graph the shortest paths between selected pairs of nodes, using the Dijkstra algorithm
6. show graph in GUI
7. show the shortest path in GUI
8. interact with user by GUI


## Usage

```bash
mvn compile
mvn exec:java
```

Command should be executed in main project folder.
### source_file structure:
```bash
2 3
1:0.263 3:0.865
0:0.263 4:0.920 2:0.876
1:0.876 5:0.213
0:0.865 4:0.007
3:0.007 1:0.920 5:0.017
4:0.017 2:0.213
```
The first row contains the following data: number of rows and number of columns - the entry "2 3" means a graph with 2 rows and 3 columns.

The following lines of the file contain a adjacency list (starting with the zero node) containing information about connections between nodes. For example, the notation "1: 0.263 3: 0.865" means that from node 0 the edges extend to node 1 with a weight of 0.263 and to node 3 with a weight of 0.865. A period (".") is required as a decimal separator.
