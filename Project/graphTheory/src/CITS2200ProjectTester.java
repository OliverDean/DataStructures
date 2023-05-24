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

		String pathToGraphFile = "C://Users/olive/OneDrive/Desktop/CompSci/2023 semester 1/cits2200 algorithms/project/DataStructures/Project/graphTheory/lib/wikiTestData3.txt";

		int testDataFlag = 4;

		// Create an instance of your implementation.
		CITS2200Project proj = new WikiPageGraph();
		// Load the graph into the project.
		loadGraph(proj, pathToGraphFile);

		// Print graph centers
		//String[] centers = proj.getCenters();
		//System.out.println("Graph centers: " + Arrays.toString(centers));

		if (testDataFlag == 0){
			testGetCenters(proj, new String[]{"/wiki/Braess%27_paradox"}); 
			// Test cases for getShortestPath
			testGetShortestPath(proj, "/wiki/Flow_network", "/wiki/Flow_network", 0);
			testGetShortestPath(proj, "/wiki/Non_existent_page", "/wiki/Flow_network", -1);
			testGetShortestPath(proj, "/wiki/Flow_network", "/wiki/Non_existent_page", -1);
			testGetShortestPath(proj, "/wiki/Dinic%27s_algorithm", "/wiki/Ford%E2%80%93Fulkerson_algorithm", 1);
			testGetShortestPath(proj, "/wiki/Dinic%27s_algorithm", "/wiki/Approximate_max-flow_min-cut_theorem", 2);
			testGetShortestPath(proj, "/wiki/Edmonds%E2%80%93Karp_algorithm", "/wiki/Nowhere-zero_flow", -1);
			testGetShortestPath(proj, "/wiki/Edmonds%E2%80%93Karp_algorithm", "/wiki/Minimum_cut", 3);
			// Test cases for getStronglyConnectedComponents
			testGetStronglyConnectedComponents(proj, new String[][]{{"/wiki/Approximate_max-flow_min-cut_theorem", "/wiki/Circulation_problem", "/wiki/Dinic%27s_algorithm", "/wiki/Edmonds%E2%80%93Karp_algorithm", "/wiki/Flow_network", "/wiki/Ford%E2%80%93Fulkerson_algorithm", "/wiki/Gomory%E2%80%93Hu_tree", "/wiki/Max-flow_min-cut_theorem", "/wiki/Maximum_flow_problem", "/wiki/Minimum-cost_flow_problem", "/wiki/Minimum_cut", "/wiki/Multi-commodity_flow_problem", "/wiki/Network_simplex_algorithm", "/wiki/Out-of-kilter_algorithm", "/wiki/Push%E2%80%93relabel_maximum_flow_algorithm"}, {"/wiki/Braess%27_paradox"}, {"/wiki/Nowhere-zero_flow"}});
			// Test cases for getHamiltonianPath
			testGetHamiltonianPath(proj, new String[0]);
		}
		if (testDataFlag == 1){
			testGetCenters(proj, new String[]{"/wiki/Jaguar_jungle"});
			// Test cases for getShortestPath
			testGetShortestPath(proj, "/wiki/Cheetah_chase", "/wiki/Hippopotamus_hideaway", 12);

			testGetStronglyConnectedComponents(proj, new String[][]{{"A_bunch"}});
			// Should report an error as there are more than 20 vertices 
			testGetHamiltonianPath(proj, new String[0]);

		}
		if (testDataFlag == 4){
			testGetCenters(proj, new String[]{"/wiki/Hockey", "/wiki/PingPong"});
			// Test cases for getShortestPath
			testGetShortestPath(proj, "/wiki/Hockey", "/wiki/Basketball", 1);

			testGetStronglyConnectedComponents(proj, new String[][]{{"A_bunch"}});

			testGetHamiltonianPath(proj, new String[0]);
		}

		if (testDataFlag == 2){
			testGetCenters(proj, new String[]{"/wiki/Sociology_of_religion"});
			// Test cases for getShortestPath
			testGetShortestPath(proj, "/wiki/Sociology_of_religion", "/wiki/Sociology_of_religion", 0);
			testGetShortestPath(proj, "/wiki/Non_existent_page", "/wiki/Sociology_of_religion", -1);
			testGetShortestPath(proj, "/wiki/Sociology_of_religion", "/wiki/Non_existent_page", -1);
			testGetShortestPath(proj, "/wiki/Game_theory", "/wiki/Topology", -1);
			testGetShortestPath(proj, "/wiki/Pythagorean_theorem", "/wiki/Cosmology", 8);

			testGetStronglyConnectedComponents(proj, new String[][]{{"A_bunch"}});
			// Should report an error as there are more than 20 vertices 
			testGetHamiltonianPath(proj, new String[0]);

		}
		if (testDataFlag == 3){

			// Test case 1: Single SCC
			CITS2200Project proj1 = new WikiPageGraph();
			testAddEdge(proj1,"/wiki/A", "/wiki/B");
			proj1.addEdge("/wiki/B", "/wiki/C");
			proj1.addEdge("/wiki/C", "/wiki/A");
			testGetShortestPath(proj1, "/wiki/A", "/wiki/B", 1);
			testGetStronglyConnectedComponents(proj1, new String[][]{{"/wiki/A", "/wiki/B", "/wiki/C"}});
			testGetHamiltonianPath(proj1, new String[] {"/wiki/A", "/wiki/B", "/wiki/C"});
		
			// Test case 2: Two separate SCCs
			CITS2200Project proj2 = new WikiPageGraph();
			proj2.addEdge("/wiki/A", "/wiki/B");
			proj2.addEdge("/wiki/B", "/wiki/C");
			proj2.addEdge("/wiki/C", "/wiki/A");
			proj2.addEdge("/wiki/D", "/wiki/E");
			proj2.addEdge("/wiki/E", "/wiki/F");
			proj2.addEdge("/wiki/F", "/wiki/D");
			testGetShortestPath(proj2, "/wiki/A", "/wiki/D", -1);
			testGetStronglyConnectedComponents(proj2, new String[][]{{"/wiki/A", "/wiki/B", "/wiki/C"}, {"/wiki/D", "/wiki/E", "/wiki/F"}});
			testGetHamiltonianPath(proj2, new String[0]);
		
			// Test case 3: Two overlapping SCCs
			CITS2200Project proj3 = new WikiPageGraph();
			proj3.addEdge("/wiki/A", "/wiki/B");
			proj3.addEdge("/wiki/B", "/wiki/C");
			proj3.addEdge("/wiki/C", "/wiki/A");
			proj3.addEdge("/wiki/C", "/wiki/D");
			proj3.addEdge("/wiki/D", "/wiki/E");
			proj3.addEdge("/wiki/E", "/wiki/F");
			proj3.addEdge("/wiki/F", "/wiki/D");
			testGetShortestPath(proj3, "/wiki/C", "/wiki/F", 3);
			testGetShortestPath(proj3, "/wiki/A", "/wiki/Does_not_exist", -1);
			testGetStronglyConnectedComponents(proj3, new String[][]{{"/wiki/A", "/wiki/B", "/wiki/C"}, {"/wiki/D", "/wiki/E", "/wiki/F"}});
			testGetHamiltonianPath(proj3, new String[] {"/wiki/A", "/wiki/B", "/wiki/C", "/wiki/D", "/wiki/E", "/wiki/F"});

			// Test case 4: Disconnected graph
			CITS2200Project proj4 = new WikiPageGraph();
			proj4.addEdge("/wiki/A", "/wiki/B");
			proj4.addEdge("/wiki/C", "/wiki/D");
			testGetShortestPath(proj4, "/wiki/A", "/wiki/D", -1);
			testGetShortestPath(proj4, "/wiki/C", "/wiki/C", 0);
			testGetStronglyConnectedComponents(proj4, new String[][]{{"/wiki/A"}, {"/wiki/B"}, {"/wiki/C"}, {"/wiki/D"}});
			testGetHamiltonianPath(proj4, new String[0]);
		
			// Test case 5: Empty graph
			CITS2200Project proj5 = new WikiPageGraph();
			testGetStronglyConnectedComponents(proj5, new String[][]{});
			testGetHamiltonianPath(proj5, new String[0]);
		}
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
		long duration = (endTime - startTime) ;

        if (result == expected) {
            System.out.println("Test passed: Shortest path " + urlFrom + " -> " + urlTo + " | Expected: " + expected + " | Result: " + result + " | Time: " + duration + " ns");
        } else {
            System.out.println("Test failed: Expected shortest path " + urlFrom + " -> " + urlTo + " | Expected: " + expected + " | Result: " + result + " | Time: " + duration + " ns");
        }
    }

	
    private static void testGetCenters(CITS2200Project proj, String[] expected) {
        long startTime = System.nanoTime();
        String[] result = proj.getCenters();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		long nanoDuration = (endTime - startTime);

		List<String> expectedList = Arrays.asList(expected);
        boolean success = false;
        for (String center : result) {
            if (expectedList.contains(center)) {
                success = true;
                break;
            }
        }

        if (success) {
            System.out.println("Test passed: Centers are " + Arrays.toString(result) + " | Expected: " + Arrays.toString(expected) + " | Time: " + duration + " ms" +  " (" + nanoDuration + " ns)");
        } else {
            System.out.println("Test failed: Expected centers are " + Arrays.toString(result) + " | Expected: " + Arrays.toString(expected) + " | Time: " + duration + " ns");
        }
    }

	private static void testGetStronglyConnectedComponents(CITS2200Project proj, String[][] expected) {
		long startTime = System.nanoTime();
		String[][] actual = proj.getStronglyConnectedComponents();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
	
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
			System.out.println("Test passed: Strongly connected component found: " + Arrays.deepToString(actual) + " in " + duration + " ns");
		} else {
			System.out.println("Test failed: Expected strongly connected component " + Arrays.deepToString(expected) + " but got " + Arrays.deepToString(actual));
		}
	}

	private static void testGetHamiltonianPath(CITS2200Project proj, String[] expectedHamiltonianPath) {
		long startTime = System.nanoTime();
		String[] actualHamiltonianPath = proj.getHamiltonianPath();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		long nanoDuration = (endTime - startTime);

		if (Arrays.equals(actualHamiltonianPath, expectedHamiltonianPath)) {
			System.out.println("Test passed: Hamiltonian path found: " + Arrays.toString(actualHamiltonianPath) + " in " + duration + " ms" + " (" + nanoDuration + " ns)");
		} else {
			System.out.println("Test failed: Expected Hamiltonian path " + Arrays.toString(expectedHamiltonianPath) +
					", but got " + Arrays.toString(actualHamiltonianPath) + " in " + duration + " ms");
		}
	}
	
	
}