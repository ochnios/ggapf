package ggapf;

public class Validator {
    public static final int MIN_GRAPH_SIZE = 1;
    public static final int MAX_GRAPH_SIZE = 200;
    public static final double MIN_WEIGHT = 0.0;
    public static final double MAX_WEIGHT = 100.0;
    public static final int MIN_SUBGRAPHS = 1;
    public static final int MAX_SUBGRAPHS = 5;

    /* from https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static Integer parseSize(String str) {
        if(!isInteger(str)) 
            return null;

        Integer size = Integer.parseInt(str);
        if(size < MIN_GRAPH_SIZE || size > MAX_GRAPH_SIZE)
            return null;
        else
            return size;
    }

    public static Double parseWeight(String str) {
        Double weight;
        try {
            weight = Double.parseDouble(str);
        } catch(NumberFormatException e) {
            return null;
        }

        if(weight < MIN_WEIGHT || weight > MAX_WEIGHT)
            return null;
        else 
            return weight;
    }

    public static Integer parseSubgraphs(String str) {
        if(!isInteger(str)) 
            return null;

        Integer subgraphs = Integer.parseInt(str);
        if(subgraphs < MIN_SUBGRAPHS || subgraphs > MAX_SUBGRAPHS)
            return null;
        else
            return subgraphs;
    }
}
