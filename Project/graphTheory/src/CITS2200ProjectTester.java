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
		// Change this to be the path to the graph file.
		String pathToGraphFile = "C://Users/olive/OneDrive/Desktop/CompSci/2023 semester 1/cits2200 algorithms/project/DataStructures/Project/graphTheory/lib/wikiTestData.txt";
		//pathToGraphFile = pathToGraphFile.repaceAll("\\\\", "//");
		// Create an instance of your implementation.
		CITS2200Project proj = new WikiPageGraph();
		// Load the graph into the project.
		loadGraph(proj, pathToGraphFile);

		// This is just an example of how you might call getShortestPath.

		// Print graph centers
		String[] centers = proj.getCenters();
		System.out.println("Graph centers: " + Arrays.toString(centers));
		// graph center is /wiki/Braess%27_paradox

		testGetCenters(proj, new String[]{"/wiki/Braess%27_paradox"}); 
        testGetCenters(proj, new String[]{"/wiki/Center1", "/wiki/Center2"});


		// Test cases
        testGetShortestPath(proj, "/wiki/Flow_network", "/wiki/Flow_network", 0);
        testGetShortestPath(proj, "/wiki/Non_existent_page", "/wiki/Flow_network", -1);
        testGetShortestPath(proj, "/wiki/Flow_network", "/wiki/Non_existent_page", -1);
        testGetShortestPath(proj, "/wiki/Dinic%27s_algorithm", "/wiki/Ford%E2%80%93Fulkerson_algorithm", 1);
        testGetShortestPath(proj, "/wiki/Dinic%27s_algorithm", "/wiki/Approximate_max-flow_min-cut_theorem", 2);



		// Write your own tests!
	}

	private static void testGetShortestPath(CITS2200Project proj, String urlFrom, String urlTo, int expected) {
        long startTime = System.nanoTime();
        int result = proj.getShortestPath(urlFrom, urlTo);
        long elapsedTime = System.nanoTime() - startTime;

        if (result == expected) {
            System.out.println("Test passed: " + urlFrom + " -> " + urlTo + " | Expected: " + expected + " | Result: " + result + " | Time: " + elapsedTime + " ns");
        } else {
            System.out.println("Test failed: " + urlFrom + " -> " + urlTo + " | Expected: " + expected + " | Result: " + result + " | Time: " + elapsedTime + " ns");
        }
    }

	
    private static void testGetCenters(CITS2200Project proj, String[] expected) {
        long startTime = System.nanoTime();
        String[] result = proj.getCenters();
        long elapsedTime = System.nanoTime() - startTime;

        if (Arrays.equals(result, expected)) {
            System.out.println("Test passed: " + Arrays.toString(result) + " | Expected: " + Arrays.toString(expected) + " | Time: " + elapsedTime + " ns");
        } else {
            System.out.println("Test failed: " + Arrays.toString(result) + " | Expected: " + Arrays.toString(expected) + " | Time: " + elapsedTime + " ns");
        }
    }


}