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
     * Adds an edge to the Wikipedia page graph. If the pages do not
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

        // Add the directed edge from urlFrom to urlTo
        adjacencyList.get(urlFrom).add(urlTo);
    }

    public void loadGraphFromString(String inputData) {
        String[] lines = inputData.split("\n");
        for (int i = 0; i < lines.length - 1; i += 2) {
            String from = lines[i];
            String to = lines[i + 1];
            addEdge(from, to);
        }
    }

	/**
	 * Finds the shorest path in number of links between two pages.
	 * If there is no path, returns -1.
	 * 
	 * @param urlFrom the URL where the path should start.
	 * @param urlTo the URL where the path should end.
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
	 * @return an array containing all the URLs that correspond to pages that are centers.
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
    
        String[][] result = new String[sccList.size()][];
        for (int i = 0; i < sccList.size(); i++) {
            result[i] = sccList.get(i).toArray(new String[0]);
        }
        return result;
    }
    
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
    
    private void dfsTransposeGraph(String vertex, Set<String> visited, Map<String, List<String>> transposeGraph, List<String> scc) {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHamiltonianPath'");
    }

    // Other methods
}
