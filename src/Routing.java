import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Routing {
	public static List<Route> findRoutes(Network net, int fromNode, int toNode) {
		List<Route> routes = new ArrayList<>();
		Stack<Integer> path = new Stack<>();
		List<Integer> check = new ArrayList<Integer>();
		path.push(fromNode);
		check.addAll(net.getConnectedNodes(fromNode));
		while (check.size() > 0) {
			Integer node = check.get(check.size() - 1);
			if (node == path.peek()) {
				path.pop();
				check.remove(check.size() - 1);
			} else if (path.contains(node)) {
				check.remove(check.size() - 1);
			} else {
				path.push(node);
				if (node == toNode) {
					routes.add(new Route(net, path));
				} else {
					check.addAll(net.getConnectedNodes(node));
				}
			}
		}
		return routes;
	}

	public static List<Route> findRoutesRecursive(Network net, int fromNode, int toNode) {
		List<Route> routes = new ArrayList<Route>();
		List<Integer> path = new ArrayList<Integer>();
		path.add(fromNode);
		int numFoundedRoutes = findRoutesRecursive(net, fromNode, toNode, path, routes, 0);
		//System.out.println("Number of founded routes from " + fromNode + " to " + toNode + " : " + numFoundedRoutes);
		return routes;
	}

	private static int findRoutesRecursive(Network net, int fromNode, int toNode, List<Integer> path, List<Route> routes, int numFoundedRoutes) {
		for (int i = 0; i < net.numNodes(); i++) {
			if (net.isConnected(fromNode, i) && path.contains(i) == false) {
				path.add(i);
				if (i == toNode) {
					routes.add(new Route(net, path));
					numFoundedRoutes++;
					if (numFoundedRoutes % 100000 == 0){
						System.out.println("Number of founded routes from " + path.get(0) + " to " + toNode + " : " + numFoundedRoutes);
					}
				} else {
					numFoundedRoutes = findRoutesRecursive(net, i, toNode, path, routes, numFoundedRoutes);
				}
				path.remove(path.size() - 1);
			}
		}
		return numFoundedRoutes;
	}

	
	private static void findShortestRoute(Network net, Traffic traffic, int currentNode, List<Integer> path, Route shortestRoute){
		for (int i = 0; i < net.numNodes(); i++) {
			if (net.isConnected(currentNode, i) && path.contains(i) == false) {
				path.add(i);
				if (i == traffic.toNode) {
					if(shortestRoute.length()==0 || (path.size()-1)<shortestRoute.length()){
						shortestRoute.set(net, path);
					}
				} else if(shortestRoute.length()==0 || path.size()<shortestRoute.length()){
					findShortestRoute(net, traffic, i, path, shortestRoute);
				}
				path.remove(path.size() - 1);
			}
		}
	}
	
	public static Route findShortestRoute(Network net, Traffic traffic){
		Route shortestRoute=new Route();
		List<Integer> path = new ArrayList<Integer>();
		path.add(traffic.fromNode);
		findShortestRoute(net, traffic, traffic.fromNode, path, shortestRoute);
		return shortestRoute;
	}

}
