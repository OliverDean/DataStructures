import CITS2200.Graph;
import CITS2200.Search;

public class SearchImp implements Search {
    // Helper class for Queue
    private class Queue {
        private int[] data;
        private int head;
        private int tail;

        public Queue(int size) {
            data = new int[size];
            head = 0;
            tail = 0;
        }

        public void enqueue(int value) {
            data[tail++] = value;
        }

        public int dequeue() {
            return data[head++];
        }

        public boolean isEmpty() {
            return head == tail;
        }
    }
    
    /**
     * Returns an array representing a connected tree of the graph starting from the
     * specified vertex using BFS.
     *
     * @param testGraph       the graph to search
     * @param startVertex     the starting vertex of the BFS
     * @return an array representing the connected tree
     */
    public int[] getConnectedTree(Graph testGraph, int startVertex) {
        
        int[] parents = new int[testGraph.getNumberOfVertices()];
        int[] distances = new int[testGraph.getNumberOfVertices()];
        bfs(testGraph, startVertex, parents, distances);
        return parents;
    }
    
    /**
     * Returns an array of distances from the specified
     * starting vertex using BFS.
     *
     * @param graph       the graph to search
     * @param startVertex the starting vertex of the BFS
     * @return an array representing the distances of vertices
     */
    public int[] getDistances(Graph g, int startVertex) {

        int[] parents = new int[g.getNumberOfVertices()];
        int[] distances = new int[g.getNumberOfVertices()];
        bfs(g, startVertex, parents, distances);
        return distances;
    }
    
    /**
     * Returns a 2D array representing the start and finish times for each vertex in
     * the graph
     * using DFS.
     *
     * @param graph       the graph to search
     * @param startVertex the starting vertex of the DFS
     * @return a 2D array representing the start and finish times for each vertex
     */
    public int[][] getTimes(Graph g, int startVertex) {
        int[][] times = new int[g.getNumberOfVertices()][2];
        boolean[] visited = new boolean[g.getNumberOfVertices()];
        int[] time = { 0 };

        dfs(g, startVertex, visited, times, time);

        return times;
    }
    
    
    /**
     * Performs Breadth First Search on the given graph, populating the parents and
     * distances arrays.
     *
     * @param graph       the graph to search
     * @param startVertex the starting vertex of the BFS
     * @param parents     an array to store the parents of each vertex
     * @param distances   an array to store the distances of each vertex from the
     *                    start vertex
     */
    private void bfs(Graph g, int startVertex, int[] parents, int[] distances) {
        Queue queue = new Queue(g.getNumberOfVertices());
        boolean[] visited = new boolean[g.getNumberOfVertices()];
        
        for (int i = 0; i < g.getNumberOfVertices(); i++) {
            parents[i] = -1;
            distances[i] = -1;
        }

        queue.enqueue(startVertex);
        visited[startVertex] = true;
        parents[startVertex] = -1;
        distances[startVertex] = 0;

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();

            for (int neighbor = 0; neighbor < g.getNumberOfVertices(); neighbor++) {
                int weight = g.getWeight(currentVertex, neighbor);
                if (weight > 0 && !visited[neighbor]) {
                    queue.enqueue(neighbor);
                    visited[neighbor] = true;
                    parents[neighbor] = currentVertex;
                    distances[neighbor] = distances[currentVertex] + 1;
                }
            }
        }
    }
    
    
    /**
     * Performs Depth First Search on the given graph, populating the times array
     * with start and
     * finish times for each vertex.
     *
     * @param graph   the graph to search
     * @param vertex  the current vertex in the DFS
     * @param visited an array to keep track of visited vertices
     * @param times   a 2D array to store the start and finish times for each vertex
     * @param time    an array containing a single integer representing the current
     *                time
     */
    private void dfs(Graph g, int vertex, boolean[] visited, int[][] times, int[] time) {
        visited[vertex] = true;
        times[vertex][0] = time[0]++; // Start time

        for (int neighbor = 0; neighbor < g.getNumberOfVertices(); neighbor++) {
            if (g.getWeight(vertex, neighbor) > 0 && !visited[neighbor]) {
                dfs(g, neighbor, visited, times, time);
            }
        }
        times[vertex][1] = time[0]++; // Finish time
    }
}
