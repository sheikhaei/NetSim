import java.util.List;

public class Test {

	public static void main(String[] args) {
		int numNodes = 10; // 100
		int numConnections = 20; // 110
		int minBandwidth = 10;
		int maxBandwidth = 100;
		int minCost = 1;
		int maxCost = 10;
		double minDelay = 0.001;
		double maxDelay = 0.1;
		double minLost = 0.001;
		double maxLost = 0.1;

		Network net = NetworkGenerator.generate(numNodes, numConnections, minBandwidth, maxBandwidth, minCost, maxCost, minDelay, maxDelay, minLost, maxLost);
		net.write("output/Net3.txt");

		int maxTime = 10;
		double reqRate = 0.5;
		int minReqBandwidth = minBandwidth / 3;
		int maxReqBandwidth = maxBandwidth / 3;
		double costRate = 0.5;
		int minAcceptCost = minCost * numNodes / 2;
		int maxAcceptCost = maxCost * numNodes / 2;
		double delayRate = 0.5;
		double minAcceptDelay = minDelay * numNodes / 2;
		double maxAcceptDelay = maxDelay * numNodes / 2;
		double lostRate = 0.5;
		double minAcceptLost = minLost * numNodes / 2;
		double maxAcceptLost = maxLost * numNodes / 2;

		List<Traffic> traffics = TrafficGenerator.generate(maxTime, numNodes, reqRate, minReqBandwidth, maxReqBandwidth, costRate, minAcceptCost, maxAcceptCost, delayRate, minAcceptDelay,
				maxAcceptDelay, lostRate, minAcceptLost, maxAcceptLost);
		TrafficGenerator.saveTraffic(traffics, "output/traffics3.txt");

		// List<Route> routes = Routing.findRoutes(net, 1, 0);
		// System.out.println(routes);
		// routes = Routing.findRoutesRecursive(net, 1, 2);
		// System.out.println(routes);
		
		long start = System.currentTimeMillis();
		Route shortestRoute = Routing.findShortestRoute(net, traffics.get(6));
		System.out.println(String.format("Elapsed time for findShortestRoute: %.3f seconds", (System.currentTimeMillis() - start) / 1000.0));
		System.out.println("Shortest route from "+traffics.get(6).fromNode+" to "+traffics.get(6).toNode+": "+shortestRoute);

		// List<Flow> flows = TrafficAllocation.allocateToRandomPath(net, traffics);
		// int success = net.requestFlows(flows);
		// System.out.println("allocateToRandomPath: " + success + " of " + traffics.size() + " requests successfully passed.");
		//
		// flows = TrafficAllocation.allocateToShortestPath(net, traffics);
		// success = net.requestFlows(flows);
		// System.out.println("allocateToShortestPath: " + success + " of " + traffics.size() + " requests successfully passed.");
		//
		// flows = TrafficAllocation.equalAllocateToAllPath(net, traffics);
		// success = net.requestFlows(flows);
		// System.out.println("equalAllocateToAllPath: " + success + " of " + traffics.size() + " requests successfully passed.");

//		List<Flow> flows = TrafficAllocation.allocateToRandomPathRegardingCostDelayLostUsage(net, traffics);
//		int success = net.requestFlows(flows);
//		System.out.println("allocateToRandomPathRegardingCostDelayLostUsage: " + success + " of " + traffics.size() + " requests successfully passed.");
//
//		flows = TrafficAllocation.allocateToShortestPathRegardingCostDelayLostUsage(net, traffics);
//		success = net.requestFlows(flows);
//		System.out.println("allocateToShortestPathRegardingCostDelayLostUsage: " + success + " of " + traffics.size() + " requests successfully passed.");
//
//		flows = TrafficAllocation.allocateToShortestPathForSameTimeTraffics(net, traffics);
//		success = net.requestFlows(flows);
//		System.out.println("allocateToShortestPathForSameTimeTraffics: " + success + " of " + traffics.size() + " requests successfully passed.");
//
//		List<Integer> notFeasibleTraffics = TrafficAllocation.getNotFeasibleTraffics(net, traffics);
//		System.out.println("notFeasibleTraffics: " + notFeasibleTraffics.size());
		
//		int[][] numUsageOfConnections = TrafficAllocation.getNumUsageOfConnections(net, traffics);
//		for(int i=0; i<net.numNodes(); i++){
//			for(int j=0; j<net.numNodes(); j++){
//				System.out.print(" " + numUsageOfConnections[i][j]);
//			}
//			System.out.println();
//		}
		
		
		AllPairShortestPath allPairShortestPath = new AllPairShortestPath(net);
		allPairShortestPath.write("output/shortestPath.txt");
		System.out.println(allPairShortestPath.getShortestPath(1, 2));
		
	}

}
