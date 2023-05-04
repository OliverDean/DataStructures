package Project.graphTheory.src;

import java.io.*;
import java.util.*;

public class CITS2200ProjectTester {
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

	public static void main(String[] args) {

		String pathToGraphFile = "C://Users/olive/OneDrive/Desktop/CompSci/2023 semester 1/cits2200 algorithms/project/DataStructures/Project/graphTheory/lib/wikiTestData.txt";

		// Create an instance of your implementation.
		CITS2200Project proj = new WikiPageGraph();
		// Load the graph into the project.
		loadGraph(proj, pathToGraphFile);

		// Print graph centers
		String[] centers = proj.getCenters();
		System.out.println("Graph centers: " + Arrays.toString(centers));
		// graph center is /wiki/Braess%27_paradox

		testGetCenters(proj, new String[]{"/wiki/Braess%27_paradox"}); 


		// Test cases for getShortestPath
        testGetShortestPath(proj, "/wiki/Flow_network", "/wiki/Flow_network", 0);
        testGetShortestPath(proj, "/wiki/Non_existent_page", "/wiki/Flow_network", -1);
        testGetShortestPath(proj, "/wiki/Flow_network", "/wiki/Non_existent_page", -1);
        testGetShortestPath(proj, "/wiki/Dinic%27s_algorithm", "/wiki/Ford%E2%80%93Fulkerson_algorithm", 1);
        testGetShortestPath(proj, "/wiki/Dinic%27s_algorithm", "/wiki/Approximate_max-flow_min-cut_theorem", 2);

		// Test case 1: Single SCC
		CITS2200Project proj1 = new WikiPageGraph();
		proj1.addEdge("/wiki/A", "/wiki/B");
		proj1.addEdge("/wiki/B", "/wiki/C");
		proj1.addEdge("/wiki/C", "/wiki/A");
		testGetStronglyConnectedComponents(proj1, new String[][]{{"/wiki/A", "/wiki/B", "/wiki/C"}});
	
		// Test case 2: Two separate SCCs
		CITS2200Project proj2 = new WikiPageGraph();
		proj2.addEdge("/wiki/A", "/wiki/B");
		proj2.addEdge("/wiki/B", "/wiki/C");
		proj2.addEdge("/wiki/C", "/wiki/A");
		proj2.addEdge("/wiki/D", "/wiki/E");
		proj2.addEdge("/wiki/E", "/wiki/F");
		proj2.addEdge("/wiki/F", "/wiki/D");
		testGetStronglyConnectedComponents(proj2, new String[][]{{"/wiki/A", "/wiki/B", "/wiki/C"}, {"/wiki/D", "/wiki/E", "/wiki/F"}});
	
		// Test case 3: Two overlapping SCCs
		CITS2200Project proj3 = new WikiPageGraph();
		proj3.addEdge("/wiki/A", "/wiki/B");
		proj3.addEdge("/wiki/B", "/wiki/C");
		proj3.addEdge("/wiki/C", "/wiki/A");
		proj3.addEdge("/wiki/C", "/wiki/D");
		proj3.addEdge("/wiki/D", "/wiki/E");
		proj3.addEdge("/wiki/E", "/wiki/F");
		proj3.addEdge("/wiki/F", "/wiki/D");
		testGetStronglyConnectedComponents(proj3, new String[][]{{"/wiki/A", "/wiki/B", "/wiki/C"}, {"/wiki/D", "/wiki/E", "/wiki/F"}});
	
		// Test case 4: Disconnected graph
		CITS2200Project proj4 = new WikiPageGraph();
		proj4.addEdge("/wiki/A", "/wiki/B");
		proj4.addEdge("/wiki/C", "/wiki/D");
		testGetStronglyConnectedComponents(proj4, new String[][]{{"/wiki/A"}, {"/wiki/B"}, {"/wiki/C"}, {"/wiki/D"}});
	
		// Test case 5: Empty graph
		CITS2200Project proj5 = new WikiPageGraph();
		testGetStronglyConnectedComponents(proj5, new String[][]{});
	

		// Modify the graph structure
		modifyGraph(proj);

		// Test cases for getCenters after modifying the graph
		testGetCenters(proj, new String[]{"/wiki/Braess%27_paradox","/wiki/NodeE"});

	}



	private static void modifyGraph(CITS2200Project proj) {
        testAddEdge(proj, "/wiki/NodeA", "/wiki/NodeB");
        testAddEdge(proj, "/wiki/NodeB", "/wiki/NodeC");
        testAddEdge(proj, "/wiki/NodeC", "/wiki/NodeA");
        testAddEdge(proj, "/wiki/NodeC", "/wiki/NodeD");
        testAddEdge(proj, "/wiki/NodeD", "/wiki/NodeE");
    }

	private static void testAddEdge(CITS2200Project proj, String urlFrom, String urlTo) {
        proj.addEdge(urlFrom, urlTo);
        if (proj.getShortestPath(urlFrom, urlTo) == 1) {
            System.out.println("Test passed: Added edge from " + urlFrom + " to " + urlTo);
        } else {
            System.out.println("Test failed: Could not add edge from " + urlFrom + " to " + urlTo);
        }
    }

	private static void testGetShortestPath(CITS2200Project proj, String urlFrom, String urlTo, int expected) {
        long startTime = System.nanoTime();
        int result = proj.getShortestPath(urlFrom, urlTo);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;

        if (result == expected) {
            System.out.println("Test passed: " + urlFrom + " -> " + urlTo + " | Expected: " + expected + " | Result: " + result + " | Time: " + duration + " ms");
        } else {
            System.out.println("Test failed: " + urlFrom + " -> " + urlTo + " | Expected: " + expected + " | Result: " + result + " | Time: " + duration + " ms");
        }
    }

	
    private static void testGetCenters(CITS2200Project proj, String[] expected) {
        long startTime = System.nanoTime();
        String[] result = proj.getCenters();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;

        if (Arrays.equals(result, expected)) {
            System.out.println("Test passed: " + Arrays.toString(result) + " | Expected: " + Arrays.toString(expected) + " | Time: " + duration + " ms");
        } else {
            System.out.println("Test failed: " + Arrays.toString(result) + " | Expected: " + Arrays.toString(expected) + " | Time: " + duration + " ms");
        }
    }

	private static void testGetStronglyConnectedComponents(CITS2200Project proj, String[][] expected) {
		long startTime = System.nanoTime();
		String[][] actual = proj.getStronglyConnectedComponents();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
	
		// Sort the arrays for comparison
		for (String[] arr : actual) {
			Arrays.sort(arr);
		}
		for (String[] arr : expected) {
			Arrays.sort(arr);
		}
		Arrays.sort(actual, (a, b) -> a[0].compareTo(b[0]));
		Arrays.sort(expected, (a, b) -> a[0].compareTo(b[0]));
	
		if (Arrays.deepEquals(actual, expected)) {
			System.out.println("PASSED: " + Arrays.deepToString(actual) + " in " + duration + " ms");
		} else {
			System.out.println("FAILED: Expected " + Arrays.deepToString(expected) + " but got " + Arrays.deepToString(actual));
		}
	}
	
}