import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetworkGenerator {
	public static Network generate(int numNodes, int numConnections, int minBandwidth, int maxBandwidth, int minCost, int maxCost, double minDelay, double maxDelay, double minLost, double maxLost) {
		if (numConnections < numNodes - 1 || numConnections > (numNodes - 1) * numNodes / 2) {
			System.err.println("Invalid input in NetworkGenerator.generate(): numNodes=" + numNodes + ", numConnections=" + numConnections);
			return null;
		}
		Network net = new Network(numNodes);
		Random rand = new Random(0);
		List<Integer> addedToNet = new ArrayList<Integer>(); // nodes that added to network
		List<Integer> notAddedToNet = new ArrayList<Integer>(); // nodes that not added to network yet
		for (int i = 0; i < numNodes; i++) {
			notAddedToNet.add(i);
		}
		addedToNet.add(rand.nextInt(numNodes)); // add first node to addedToNet
		notAddedToNet.remove(addedToNet.get(0)); // remove that first node from notAddedToNet
		// create numNodes-1 connections in a such random way that we would have a connected network
		for (int i = 0; i < numNodes-1; i++) {
			int fromNode = addedToNet.get(rand.nextInt(addedToNet.size()));
			int toNode = notAddedToNet.get(rand.nextInt(notAddedToNet.size()));
			int bandwidth = rand.nextInt(maxBandwidth - minBandwidth) + minBandwidth;
			int cost = rand.nextInt(maxCost - minCost) + minCost;
			double delay = rand.nextDouble() * (maxDelay - minDelay) + minDelay;
			double lost = rand.nextDouble() * (maxLost - minLost) + minLost;
			net.connect(fromNode, toNode, bandwidth, cost, delay, lost);
			addedToNet.add(toNode);
			notAddedToNet.remove((Integer)toNode); // The input casted to Integer to call remove(Object o)
		}
		// randomly create the remaining connections
		for (int i = numNodes-1; i < numConnections; i++) {
			int fromNode, toNode;
			do {
				fromNode = rand.nextInt(numNodes);
				toNode = rand.nextInt(numNodes);
			} while (fromNode == toNode || net.isConnected(fromNode, toNode));
			int bandwidth = rand.nextInt(maxBandwidth - minBandwidth) + minBandwidth;
			int cost = rand.nextInt(maxCost - minCost) + minCost;
			double delay = rand.nextDouble() * (maxDelay - minDelay) + minDelay;
			double lost = rand.nextDouble() * (maxLost - minLost) + minLost;
			net.connect(fromNode, toNode, bandwidth, cost, delay, lost);
		}
		net.resetUsage();
		return net;
	}
}
