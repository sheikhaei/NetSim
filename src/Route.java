import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Route {
	public List<Integer> nodes;
	public List<Integer> costs;
	public List<Integer> bandwidth;
	public int maxBandwidth;
	public double cumDelay;
	public double cumLost;

	public Route() {
		nodes = new ArrayList<Integer>();
		costs = new ArrayList<Integer>();
		bandwidth = new ArrayList<Integer>();
		maxBandwidth = 0;
		cumDelay = 0;
		cumLost = 0;
	}
	
	public Route(Network net, Stack<Integer> path) {
		nodes = new ArrayList<Integer>();
		costs = new ArrayList<Integer>();
		bandwidth = new ArrayList<Integer>();
		maxBandwidth = Integer.MAX_VALUE;
		cumDelay = 0;
		cumLost = 0;
		nodes.add(path.elementAt(0));
		for (int i = 1; i < path.size(); i++) {
			int from = path.elementAt(i - 1);
			int to = path.elementAt(i);
			nodes.add(to);
			costs.add(net.cost[from][to]);
			bandwidth.add(net.bandwidth[from][to]);
			if (maxBandwidth > net.bandwidth[from][to])
				maxBandwidth = net.bandwidth[from][to];
			cumDelay += net.delay[from][to];
			cumLost = 1 - ((1 - cumLost) * (1 - net.lost[from][to]));

		}
	}

	public Route(Network net, List<Integer> path) {
		set(net,path);
	}


	public void set(Network net, List<Integer> path){
		nodes = new ArrayList<Integer>();
		costs = new ArrayList<Integer>();
		bandwidth = new ArrayList<Integer>();
		maxBandwidth = Integer.MAX_VALUE;
		cumDelay = 0;
		cumLost = 0;
		nodes.add(path.get(0));
		for (int i = 1; i < path.size(); i++) {
			int from = path.get(i - 1);
			int to = path.get(i);
			nodes.add(to);
			costs.add(net.cost[from][to]);
			bandwidth.add(net.bandwidth[from][to]);
			if (maxBandwidth > net.bandwidth[from][to])
				maxBandwidth = net.bandwidth[from][to];
			cumDelay += net.delay[from][to];
			cumLost = 1 - ((1 - cumLost) * (1 - net.lost[from][to]));
		}
	}
	

	/**
	 * get cumulative cost for the required bandwidth
	 * 
	 * @param reqBandwidth
	 * @return
	 */
	public double getCost(int reqBandwidth) {
		double cumCost = 0;
		for (int i = 0; i < costs.size(); i++) {
			cumCost += costs.get(i) * (reqBandwidth / bandwidth.get(i));
		}
		return cumCost;
	}

	public int length() {
		return bandwidth.size();
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(nodes.get(0));
		for (int i = 1; i < nodes.size(); i++) {
			str.append("-" + nodes.get(i));
		}
		return str.toString();
	}
}
