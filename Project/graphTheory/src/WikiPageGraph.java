package Project.graphTheory.src;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import CITS2200.*;

public class WikiPageGraph implements CITS2200Project {

    private Map<String, List<String>> adjacencyList;

    public WikiPageGraph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds an edge to the Wiki page graph. If the pages do not
     * already exist in the graph, they will be added to the graph.
     * 
     * @param urlFrom the URL which has a link to urlTo.
     * @param urlTo   the URL which urlFrom has a link to.
     */
    @Override
    public void addEdge(String urlFrom, String urlTo) {
        // Add the vertices to the graph if they don't exist
        adjacencyList.putIfAbsent(urlFrom, new ArrayList<>());
        adjacencyList.putIfAbsent(urlTo, new ArrayList<>());

        // Check if the edge is a self-loop or already exists
        if (!urlFrom.equals(urlTo) && !adjacencyList.get(urlFrom).contains(urlTo)) {
            // Add the directed edge from urlFrom to urlTo
            adjacencyList.get(urlFrom).add(urlTo);
        }
    }

    public static void loadGraph(CITS2200Project project, String path) {
        // The graph is in the following format:
        // Every pair of consecutive lines represent a directed edge.
        // The edge goes from the URL in the first line to the URL in the second line.
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                while (reader.ready()) {
                    String from = reader.readLine();
                    String to = reader.readLine();
                    System.out.println("Adding edge from " + from + " to " + to);
                    project.addEdge(from, to);
                }
            }
        } catch (Exception e) {
            System.out.println("There was a problem:");
            System.out.println(e.toString());
        }
    }

    /**
     * Finds the shortest path in number of links between two pages.
     * If there is no path, returns -1.
     * 
     * @param urlFrom the URL where the path should start.
     * @param urlTo   the URL where the path should end.
     * @return the legnth of the shorest path in number of links followed.
     */
    @Override
    public int getShortestPath(String urlFrom, String urlTo) {
        if (!adjacencyList.containsKey(urlFrom) || !adjacencyList.containsKey(urlTo)) {
            return -1;
        }

        if (urlFrom.equals(urlTo)) {
            return 0;
        }

        Queue queue = new Queue(adjacencyList.size());
        Map<String, Integer> distances = new HashMap<>();

        for (String url : adjacencyList.keySet()) {
            distances.put(url, -1);
        }

        distances.put(urlFrom, 0);
        queue.enqueue(urlFrom);

        while (!queue.isEmpty()) {
            String currentUrl = queue.dequeue();
            int currentDistance = distances.get(currentUrl);

            for (String neighborUrl : adjacencyList.get(currentUrl)) {
                if (distances.get(neighborUrl) == -1) {
                    distances.put(neighborUrl, currentDistance + 1);
                    queue.enqueue(neighborUrl);

                    if (neighborUrl.equals(urlTo)) {
                        return distances.get(urlTo);
                    }
                }
            }
        }
        return -1;
    }

    // Helper class for Queue
    private class Queue {
        private String[] data;
        private int head;
        private int tail;

        public Queue(int size) {
            data = new String[size];
            head = 0;
            tail = 0;
        }

        public void enqueue(String value) {
            data[tail++] = value;
        }

        public String dequeue() {
            return data[head++];
        }

        public boolean isEmpty() {
            return head == tail;
        }
    }

    /**
     * Finds all the centers of the page graph. The order of pages
     * in the output does not matter. Any order is correct as long as
     * all the centers are in the array, and no pages that aren't centers
     * are in the array.
     * 
     * @return an array containing all the URLs that correspond to pages that are
     *         centers.
     */
    @Override
    public String[] getCenters() {

        Map<String, Integer> eccentricities = new HashMap<>();

        // Compute the eccentricity of each node using BFS
        for (String url : adjacencyList.keySet()) {
            int maxDistance = 0;
            for (String otherUrl : adjacencyList.keySet()) {
                int distance = getShortestPath(url, otherUrl);
                maxDistance = Math.max(maxDistance, distance);
            }
            eccentricities.put(url, maxDistance);
        }

        // Find the minimum eccentricity
        int minEccentricity = Integer.MAX_VALUE;
        for (Integer eccentricity : eccentricities.values()) {
            minEccentricity = Math.min(minEccentricity, eccentricity);
        }

        // Collect the centers of the graph
        List<String> centers = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : eccentricities.entrySet()) {
            if (entry.getValue() == minEccentricity) {
                centers.add(entry.getKey());
            }
        }

        // Sort the centers alphabetically
        Collections.sort(centers);

        return centers.toArray(new String[0]);
    }

    /**
     * Finds all the strongly connected components of the page graph.
     * Every strongly connected component can be represented as an array
     * containing the page URLs in the component. The return value is thus an array
     * of strongly connected components. The order of elements in these arrays
     * does not matter. Any output that contains all the strongly connected
     * components is considered correct.
     * 
     * @return an array containing every strongly connected component.
     */
    @Override
    public String[][] getStronglyConnectedComponents() {

        // Step 1: Transpose the graph (reverse the direction of all edges)
        // Step 2: Perform a depth-first search on the original graph and store vertices
        // in post-order
        // Step 3: Perform a depth-first search on the transposed graph, exploring
        // vertices in the order of postOrder

        Map<String, List<String>> transposeGraph = getTransposeGraph();
        Stack<String> postOrder = getDFSPostOrder();
        List<List<String>> sccList = new ArrayList<>();

        Set<String> visited = new HashSet<>();
        while (!postOrder.isEmpty()) {
            String vertex = postOrder.pop();
            if (!visited.contains(vertex)) {
                List<String> scc = new ArrayList<>();
                dfsTransposeGraph(vertex, visited, transposeGraph, scc);
                sccList.add(scc);
            }
        }
        // Convert the list of SCCs to a 2D array of strings
        String[][] result = new String[sccList.size()][];
        for (int i = 0; i < sccList.size(); i++) {
            result[i] = sccList.get(i).toArray(new String[0]);
        }
        return result;
    }

    /**
     * Gets the transpose of the input graph.
     *
     * @return A map containing the transposed adjacency list of the graph.
     */
    private Map<String, List<String>> getTransposeGraph() {
        Map<String, List<String>> transposeGraph = new HashMap<>();
        for (String vertex : adjacencyList.keySet()) {
            for (String neighbor : adjacencyList.get(vertex)) {
                if (!transposeGraph.containsKey(neighbor)) {
                    transposeGraph.put(neighbor, new ArrayList<>());
                }
                transposeGraph.get(neighbor).add(vertex);
            }
        }
        return transposeGraph;
    }

    /**
     * Performs a depth-first search on the graph.
     *
     * @return A stack of strings containing the vertices in post-order.
     */
    private Stack<String> getDFSPostOrder() {
        Stack<String> postOrder = new Stack<>();
        Set<String> visited = new HashSet<>();
        for (String vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex)) {
                dfsPostOrder(vertex, visited, postOrder);
            }
        }
        return postOrder;
    }

    /**
     * Recursive depth-first search that populates the postOrder stack with vertices
     * in post-order.
     *
     * @param vertex    The current vertex being explored.
     * @param visited   The set of visited vertices.
     * @param postOrder The stack containing vertices in post-order.
     */
    private void dfsPostOrder(String vertex, Set<String> visited, Stack<String> postOrder) {
        visited.add(vertex);
        if (adjacencyList.containsKey(vertex)) {
            for (String neighbor : adjacencyList.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsPostOrder(neighbor, visited, postOrder);
                }
            }
        }
        postOrder.push(vertex);
    }

    /**
     * Recursive depth-first search that populates the scc list with vertices in the
     * current strongly connected component.
     *
     * @param vertex         The current vertex being explored.
     * @param visited        The set of visited vertices.
     * @param transposeGraph The adjacency list of the transposed graph.
     * @param scc            The list containing the vertices in the current
     *                       strongly connected component.
     */
    private void dfsTransposeGraph(String vertex, Set<String> visited, Map<String, List<String>> transposeGraph,
            List<String> scc) {
        visited.add(vertex);
        scc.add(vertex);
        if (transposeGraph.containsKey(vertex)) {
            for (String neighbor : transposeGraph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsTransposeGraph(neighbor, visited, transposeGraph, scc);
                }
            }
        }
    }

    /**
     * Finds a Hamiltonian path in the page graph. There may be many
     * possible Hamiltonian paths. Any of these paths is a correct output.
     * This method should never be called on a graph with more than 20
     * vertices. If there is no Hamiltonian path, this method will
     * return an empty array. The output array should contain the URLs of pages
     * in a Hamiltonian path. The order matters, as the elements of the
     * array represent this path in sequence. So the element [0] is the start
     * of the path, and [1] is the next page, and so on.
     * 
     * @return a Hamiltonian path of the page graph.
     */
    @Override
    public String[] getHamiltonianPath() {
        int n = getVertices().length;

        // Check if the graph has more than 20 vertices
        // could get "Exception in thread "main" java.lang.OutOfMemoryError: Java heap
        // space" if ignored.
        if (n > 20) {
            throw new UnsupportedOperationException(
                    "This method should not be called on a graph with more than 20 vertices.");
        }

        // Initialize the dynamic programming and next arrays and fill with max values
        int[][] dp = new int[n][(1 << n)];
        int[][] next = new int[n][(1 << n)];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE / 2);
        }

        // Set the base case values for the dynamic programming array
        for (int i = 0; i < n; i++) {
            dp[i][1 << i] = 1;
        }

        // Iterate through all subsets of vertices
        for (int mask = 1; mask < (1 << n); mask++) {
            // Iterate through all vertices in the current subset
            for (int v = 0; v < n; v++) {
                if ((mask & (1 << v)) != 0) {
                    // Iterate through all vertices not in the current subset
                    for (int u = 0; u < n; u++) {
                        if ((mask & (1 << u)) == 0) {
                            // Check if there is an edge from vertex v to vertex u
                            if (isEdge(getVertices()[v], getVertices()[u])) {
                                // Update the dynamic programming array and next array
                                if (dp[v][mask] + 1 < dp[u][mask | (1 << u)]) {
                                    dp[u][mask | (1 << u)] = dp[v][mask] + 1;
                                    next[u][mask | (1 << u)] = v;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Find the last vertex in the minimum Hamiltonian path
        int minPath = Integer.MAX_VALUE;
        int last = -1;
        for (int i = 0; i < n; i++) {
            if (dp[i][(1 << n) - 1] < minPath) {
                minPath = dp[i][(1 << n) - 1];
                last = i;
            }
        }

        // If there is no Hamiltonian path, return an empty array
        if (minPath == Integer.MAX_VALUE / 2) {
            return new String[0];
        }

        // Reconstruct the Hamiltonian path from the next array
        String[] path = new String[n];
        int mask = (1 << n) - 1;
        for (int i = n - 1; i >= 0; i--) {
            path[i] = getVertices()[last];
            int newLast = next[last][mask];
            mask ^= (1 << last);
            last = newLast;
        }
        return path;
    }

    /**
     * Checks if there is an edge between two given vertices in the graph.
     *
     * @param vertexU The first vertex of the edge.
     * @param vertexV The second vertex of the edge.
     * @return true if there is an edge between vertexU and vertexV, false
     *         otherwise.
     */
    private boolean isEdge(String vertexU, String vertexV) {
        List<String> edges = adjacencyList.get(vertexU);
        return edges != null && edges.contains(vertexV);
    }

    /**
     * Returns an array containing all vertices (URLs) in the graph.
     *
     * @return an array of vertices (URLs) in the graph.
     */
    public String[] getVertices() {
        return adjacencyList.keySet().toArray(new String[0]);
    }
}