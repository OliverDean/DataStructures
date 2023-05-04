package Project.graphTheory.src;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Map;

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


    @Override
    public String[][] getStronglyConnectedComponents() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStronglyConnectedComponents'");
    }

    @Override
    public String[] getHamiltonianPath() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHamiltonianPath'");
    }

    // Other methods
}
