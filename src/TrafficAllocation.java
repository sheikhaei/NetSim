import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TrafficAllocation {
	public static List<Flow> allocateToRandomPath(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		Random rand = new Random();
		for (Traffic traffic : traffics) {
			List<Route> routes = Routing.findRoutesRecursive(net, traffic.fromNode, traffic.toNode);
			int index = rand.nextInt(routes.size());
			flows.add(new Flow(traffic, routes.get(index)));
		}
		return flows;
	}

	public static List<Flow> allocateToShortestPath(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		for (Traffic traffic : traffics) {
			List<Route> routes = Routing.findRoutesRecursive(net, traffic.fromNode, traffic.toNode);
			int index = indexOfShortestRoute(routes);
			flows.add(new Flow(traffic, routes.get(index)));
		}
		return flows;
	}

	public static List<Flow> allocateToRandomPathRegardingCostDelayLostUsage(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		Network netCopy = new Network(net);
		int time = 0;
		for (Traffic traffic : traffics) {
			if (traffic.time != time) {
				time = traffic.time;
				netCopy.resetUsage();
			}
			List<Route> routes = Routing.findRoutesRecursive(net, traffic.fromNode, traffic.toNode);
			List<Integer> indexes = new ArrayList<Integer>();
			for (int i = 0; i < routes.size(); i++)
				indexes.add(i);
			Collections.shuffle(indexes);
			Flow flow = null;
			for (int index : indexes) {
				flow = new Flow(traffic, routes.get(index));
				if (netCopy.requestFlow(flow) == true) {
					break;
				}
			}
			flows.add(flow);
		}
		return flows;
	}

	public static List<Flow> allocateToShortestPathRegardingCostDelayLostUsage(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		Network netCopy = new Network(net);
		int time = 0;
		for (Traffic traffic : traffics) {
			if (traffic.time != time) {
				time = traffic.time;
				netCopy.resetUsage();
			}
			List<Route> routes = Routing.findRoutesRecursive(net, traffic.fromNode, traffic.toNode);
			Map<Integer, Integer> routeLength = new HashMap<>();
			for (int i = 0; i < routes.size(); i++) {
				routeLength.put(i, routes.get(i).length());
			}
			Map<Integer, Integer> sortedRouteLength = MapUtil.sortByValue(routeLength, true);
			Flow flow = null;
			for (int index : sortedRouteLength.keySet()) {
				flow = new Flow(traffic, routes.get(index));
				if (netCopy.requestFlow(flow) == true) {
					break;
				}
			}
			flows.add(flow);
		}
		return flows;
	}

	public static List<Flow> allocateToShortestPathForSameTimeTraffics(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		Network netCopy = new Network(net);
		int endTime = traffics.get(traffics.size() - 1).time;
		int index = 0; // traffics index
		for (int time = 0; time <= endTime; time++) {
			netCopy.resetUsage();
			List<Route> routes = new ArrayList<Route>();
			List<Integer> correspondingTraffic = new ArrayList<Integer>();
			while (index < traffics.size() && traffics.get(index).time == time) {
				routes.addAll(Routing.findRoutesRecursive(net, traffics.get(index).fromNode, traffics.get(index).toNode));
				while (correspondingTraffic.size() < routes.size())
					correspondingTraffic.add(index);
				index++;
			}
			Map<Integer, Integer> routeLength = new HashMap<>();
			for (int i = 0; i < routes.size(); i++) {
				routeLength.put(i, routes.get(i).length());
			}
			Map<Integer, Integer> sortedRouteLength = MapUtil.sortByValue(routeLength, true);
			Flow flow = null;
			Set<Integer> passedTraffics = new HashSet<Integer>(); // traffics that assigned a successful flow
			for (int routeIndex : sortedRouteLength.keySet()) {
				if (passedTraffics.contains(correspondingTraffic.get(routeIndex)) == false) {
					flow = new Flow(traffics.get(correspondingTraffic.get(routeIndex)), routes.get(routeIndex));
					if (netCopy.requestFlow(flow) == true) {
						passedTraffics.add(correspondingTraffic.get(routeIndex));
						flows.add(flow);
					}
				}
			}
		}
		return flows;
	}

	public static List<Flow> equalAllocateToAllPath(Network net, List<Traffic> traffics) {
		List<Flow> flows = new ArrayList<>();
		for (Traffic traffic : traffics) {
			List<Route> routes = Routing.findRoutesRecursive(net, traffic.fromNode, traffic.toNode);
			List<Double> allocates = Collections.nCopies(routes.size(), 1.0 / routes.size());
			flows.add(new Flow(traffic, routes, allocates));
		}
		return flows;
	}

	public static List<Integer> getNotFeasibleTraffics(Network net, List<Traffic> traffics) {
		List<Integer> notFeasibleTraffics = new ArrayList<Integer>();
		Network netCopy = new Network(net);
		for (int i = 0; i < traffics.size(); i++) {
			netCopy.resetUsage();
			List<Route> routes = Routing.findRoutesRecursive(net, traffics.get(i).fromNode, traffics.get(i).toNode);
			boolean feasible = false;
			for (Route route : routes) {
				Flow flow = new Flow(traffics.get(i), route);
				if (netCopy.requestFlow(flow) == true) {
					feasible = true;
					break;
				}
			}
			if (feasible == false) {
				notFeasibleTraffics.add(i);
			}
		}
		return notFeasibleTraffics;
	}

	private static int indexOfShortestRoute(List<Route> routes) {
		int shortestLength = Integer.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < routes.size(); i++) {
			if (shortestLength > routes.get(i).nodes.size()) {
				shortestLength = routes.get(i).nodes.size();
				index = i;
			}
		}
		return index;
	}
	
	public static int[][] getNumUsageOfConnections(Network net, List<Traffic> traffics){
		int[][] numUsage = new int[net.numNodes()][net.numNodes()];
		for(Traffic traffic:traffics){
			List<Route> routes = Routing.findRoutesRecursive(net, traffic.fromNode, traffic.toNode);
			for(Route route: routes){
				for(int i=0; i<route.nodes.size()-1; i++){
					int fromNode = route.nodes.get(i);
					int toNode = route.nodes.get(i+1);
					numUsage[fromNode][toNode]++;
					numUsage[toNode][fromNode]++;
				}
			}
		}
		return numUsage;
	}

}
