import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Network {
	public boolean[][] connectivity;
	public int[][] bandwidth;
	public int[][] cost;
	public double[][] delay;
	public double[][] lost;
	private int[][] usage;
	private int numNodes;

	public Network(int numNodes) {
		connectivity = new boolean[numNodes][numNodes];
		bandwidth = new int[numNodes][numNodes];
		cost = new int[numNodes][numNodes];
		delay = new double[numNodes][numNodes];
		lost = new double[numNodes][numNodes];
		usage = new int[numNodes][numNodes];
		this.numNodes = numNodes;
	}
	
	public Network(Network net){
		connectivity = net.connectivity.clone();
		bandwidth = net.bandwidth.clone();
		cost = net.cost.clone();
		delay = net.delay.clone();
		lost = net.lost.clone();
		usage = net.usage.clone();
		numNodes = net.numNodes();
	}
	
	public int numNodes(){
		return numNodes;
	}

	public void connect(int fromNode, int toNode, int bandwidth, int cost, double delay, double lost) {
		if (fromNode >= numNodes || toNode >= numNodes) {
			System.err.println("Error in Network.connect(): fromNode or toNode exceeds the number of nodes.");
		}
		connectivity[fromNode][toNode] = connectivity[toNode][fromNode] = true;
		this.bandwidth[fromNode][toNode] = this.bandwidth[toNode][fromNode] = bandwidth;
		this.cost[fromNode][toNode] = this.cost[toNode][fromNode] = cost;
		this.delay[fromNode][toNode] = this.delay[toNode][fromNode] = delay;
		this.lost[fromNode][toNode] = this.lost[toNode][fromNode] = lost;
	}

	public boolean isConnected(int fromNode, int toNode) {
		return connectivity[fromNode][toNode];
	}

	public List<Integer> getConnectedNodes(int node) {
		List<Integer> connectedNodes = new ArrayList<>();
		for (int i = 0; i < numNodes; i++) {
			if (connectivity[node][i] == true)
				connectedNodes.add(i);
		}
		return connectedNodes;
	}

	public void resetUsage() {
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				usage[i][j] = 0;
			}
		}
	}

	public boolean requestFlow(Flow flow) {
		boolean possible = true; // request possibility
		double cumCost = 0; // cumulative cost
		double cumDelay = 0; // cumulative delay
		double cumLost = 0; // cumulative lost
		// check if flow is possible
		for (int i = 0; i < flow.nodes.size(); i++) {
			for (int j = 0; j < flow.nodes.get(i).size() - 1; j++) {
				int from = flow.nodes.get(i).get(j);
				int to = flow.nodes.get(i).get(j + 1);
				cumCost += cost[from][to] * ((double) flow.bandwidth * flow.allocates.get(i) / bandwidth[from][to]);
				cumDelay += delay[from][to];
				cumLost = 1 - ((1 - cumLost) * (1 - lost[from][to]));
				if (usage[from][to] + flow.bandwidth > bandwidth[from][to] || cumCost > flow.acceptCost || cumDelay > flow.acceptDelay || cumLost > flow.acceptLost)
					possible = false;
			}
		}
		// if flow is possible, allocate requested bandwidth
		if (possible) {
			for (int i = 0; i < flow.nodes.size(); i++) {
				for (int j = 0; j < flow.nodes.get(i).size() - 1; j++) {
					int from = flow.nodes.get(i).get(j);
					int to = flow.nodes.get(i).get(j + 1);
					usage[from][to] += flow.bandwidth * flow.allocates.get(i);
				}
			}
		}
		return possible;
	}
	
	public int requestFlows(List<Flow> flows){
		int success = 0;
		int time = 0;
		for (int i=0; i<flows.size(); i++) {
			if(flows.get(i).time!=time){
				time = flows.get(i).time;
				resetUsage();
			}
			if (requestFlow(flows.get(i)) == true)
				success++;
		}
		return success;
	}
	

	public void read(String filePath) {

	}

	public void write(String filePath) {
		try {
			PrintWriter writer = new PrintWriter(filePath);
			writer.println("\n====== Connectivity ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += connectivity[i][j] == true ? " 1" : " 0";
				}
				writer.println(line);
			}
			writer.println("\n====== Bandwidth ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %3d", bandwidth[i][j]);
				}
				writer.println(line);
			}
			writer.println("\n====== Cost ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %3d", cost[i][j]);
				}
				writer.println(line);
			}
			writer.println("\n====== Delay ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %.3f", delay[i][j]);
				}
				writer.println(line);
			}
			writer.println("\n====== Lost ========\n");
			for (int i = 0; i < numNodes; i++) {
				String line = "";
				for (int j = 0; j < numNodes; j++) {
					line += String.format(" %.3f", lost[i][j]);
				}
				writer.println(line);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
