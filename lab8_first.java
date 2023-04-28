import CITS2200.Graph;
import CITS2200.Path;

public class PathImp implements Path {
    
    /**
     * Computes the weight of the minimum spanning tree (MST) for a given weighted, undirected graph.
     * This method uses Prim's algorithm to find the MST.
     *
     * @param graph The input Graph object representing a weighted, undirected graph.
     * @return The weight of the minimum spanning tree, or -1 if no MST can be found.
     */

    public int getMinSpanningTree(Graph graph) {
        int numVertices = graph.getNumberOfVertices();
        if (numVertices == 0) {
            return -1;
        }

        boolean[] visited = new boolean[numVertices];
        int[] weights = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            weights[i] = Integer.MAX_VALUE;
        }
        
        weights[0] = 0;
        int mstWeight = 0;

        for (int i = 0; i < numVertices; i++) {
            int u = getMinWeightVertex(weights, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && graph.isWeighted() && graph.getWeight(u, v) > 0 && graph.getWeight(u, v) < weights[v]) {
                    weights[v] = graph.getWeight(u, v);
                }
            }
        }

        for (int i = 0; i < numVertices; i++) {
            mstWeight += weights[i];
        }

        return mstWeight;
    }

    private int getMinWeightVertex(int[] weights, boolean[] visited) {
        int minWeight = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < weights.length; i++) {
            if (!visited[i] && weights[i] < minWeight) {
                minWeight = weights[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Computes the shortest paths from a given source vertex to all other vertices in a
     * weighted, directed graph. This method uses Dijkstra's algorithm to find the shortest paths.
     *
     * @param graph  The input Graph object representing a weighted, directed graph.
     * @param source The source vertex from which the shortest paths should be calculated.
     * @return An integer array containing the shortest distances from the source vertex to all other vertices.
     */

    public int[] getShortestPaths(Graph graph, int source) {
        int numVertices = graph.getNumberOfVertices();
        int[] distances = new int[numVertices];
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        
        distances[source] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = getMinWeightVertex(distances, visited);

            if(u == -1) {
                break;
            }

            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && graph.getWeight(u, v) > 0 && distances[u] != Integer.MAX_VALUE && distances[u] + graph.getWeight(u, v) < distances[v]) {
                    distances[v] = distances[u] + graph.getWeight(u, v);
                }
            }
        }
        for (int i = 0; i < numVertices; i++) {
            if (distances[i] == Integer.MAX_VALUE) {
                distances[i] = -1;
            }
        }

        return distances;
    }
}