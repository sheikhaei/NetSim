public class Traffic {
	public int time;
	public int fromNode;
	public int toNode;
	public int reqBandwidth;
	public int acceptCost;
	public double acceptDelay;
	public double acceptLost;

	public String toString() {
		String str = String.format("%3d\t%2d\t%2d\t%3d\t%3d\t%.3f\t%.3f", time, fromNode, toNode, reqBandwidth, acceptCost == Integer.MAX_VALUE ? 0 : acceptCost,
				acceptDelay == Double.MAX_VALUE ? 0 : acceptDelay, acceptLost);
		return str;
	}
}
