import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Find All Pairs Shortest Path by Floyd-Warshall algorithm.
 */
public class AllPairShortestPath {

	final static int INF = 99999;
	private int shortestPath[][][];
	private int numNodes;
	
	public AllPairShortestPath(Network net){
		floydWarshall(net);
	}
	
	public void floydWarshall(Network net) {
		numNodes = net.numNodes();
		int length[][] = new int[numNodes][numNodes];
		shortestPath = new int[numNodes][numNodes][];
		int i, j, k;

		 // Initialize the solution matrix same as input graph matrix.
		for (i = 0; i < numNodes; i++) {
			for (j = 0; j < numNodes; j++) {
				if (i == j) {
					length[i][j] = 0;
					shortestPath[i][j] = new int[0];
				} else if (net.isConnected(i, j)) {
					length[i][j] = 1;
					shortestPath[i][j] = new int[] { i, j };
				} else {
					length[i][j] = INF;
					shortestPath[i][j] = new int[0];
				}
			}
		}

		/*
		 * Add all vertices one by one to the set of intermediate vertices. ---> Before start of an iteration, we have shortest distances between all pairs of vertices such that the shortest distances
		 * consider only the vertices in set {0, 1, 2, .. k-1} as intermediate vertices. ----> After the end of an iteration, vertex no. k is added to the set of intermediate vertices and the set
		 * becomes {0, 1, 2, .. k}
		 */
		for (k = 0; k < numNodes; k++) {
			for (i = 0; i < numNodes; i++) {
				for (j = 0; j < numNodes; j++) {
					if (length[i][k] + length[k][j] < length[i][j]) {
						length[i][j] = length[i][k] + length[k][j];
						shortestPath[i][j] = concat(shortestPath[i][k], shortestPath[k][j]);
					}
				}
			}
		}

		//printSolution(net, shortestPath);
	}

	private int[] concat(int[] path1, int[] path2) {
		if (path1.length == 0) {
			return path2.clone();
		}
		if (path2.length == 0) {
			return path1.clone();
		}
		if (path1[path1.length - 1] != path2[0]) {
			System.out.println("Error in concat() in AllPairShortestPath class.");
			return null;
		}
		int[] path = new int[path1.length + path2.length - 1];
		for (int i = 0; i < path1.length; i++) {
			path[i] = path1[i];
		}
		for (int i = 1; i < path2.length; i++) {
			path[i + path1.length - 1] = path2[i];
		}
		return path;
	}
	
	public List<Integer> getShortestPath(int from, int to){
		return Arrays.stream(shortestPath[from][to]).boxed().collect(Collectors.toList());
	}

	public void write(String filePath) {
		try {
			PrintWriter writer = new PrintWriter(filePath);
			for (int i = 0; i < numNodes; ++i) {
				for (int j = 0; j < numNodes; ++j) {
					List<Integer> path = Arrays.stream(shortestPath[i][j]).boxed().collect(Collectors.toList());
					writer.println(i + " to " + j + " : " + path);
				}
				writer.println();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
