import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Flow {
	public List<ArrayList<Integer>> nodes; // a flow can pass through several paths and a path contains several nodes
	public List<Double> allocates; // bandwidth allocation for each path
	public int bandwidth;
	public int acceptCost;
	public double acceptDelay;
	public double acceptLost;
	public int time;

	public Flow(Traffic traffic, List<Route> routes, List<Double> allocates) {
		nodes = new ArrayList<ArrayList<Integer>>();
		for(Route route:routes){
			nodes.add(new ArrayList<>(route.nodes));
		}
		this.allocates = new ArrayList<>(allocates);
		bandwidth = traffic.reqBandwidth;
		acceptCost = traffic.acceptCost;
		acceptDelay = traffic.acceptDelay;
		acceptLost = traffic.acceptLost;
		time = traffic.time;
	}
	
	public Flow(Traffic traffic, Route route) {
		nodes = new ArrayList<ArrayList<Integer>>();
		nodes.add(new ArrayList<>(route.nodes));
		allocates = Arrays.asList(1.0);
		bandwidth = traffic.reqBandwidth;
		acceptCost = traffic.acceptCost;
		acceptDelay = traffic.acceptDelay;
		acceptLost = traffic.acceptLost;
		time = traffic.time;
	}
	
}
