import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrafficGenerator {
	public static List<Traffic> generate(int maxTime, int numNodes, double reqRate, int minReqBandwidth, int maxReqBandwidth, double costRate, int minAcceptCost, int maxAcceptCost, double delayRate, double minAcceptDelay, double maxAcceptDelay, double lostRate,  double minAcceptLost,
			double maxAcceptLost) {
		List<Traffic> traffics = new ArrayList<>();
		Random rand = new Random(0);
		for (int i = 0; i < maxTime; i++) {
			for (int j = 0; j < numNodes; j++) {
				if (rand.nextDouble() < reqRate) {
					Traffic traffic = new Traffic();
					traffic.time = i;
					traffic.fromNode = j;
					do {
						traffic.toNode = rand.nextInt(numNodes);
					} while (traffic.toNode == traffic.fromNode);
					traffic.reqBandwidth = rand.nextInt(maxReqBandwidth - minReqBandwidth) + minReqBandwidth;
					if(rand.nextDouble()<costRate)
						traffic.acceptCost = rand.nextInt(maxAcceptCost - minAcceptCost) + minAcceptCost;
					else
						traffic.acceptCost = Integer.MAX_VALUE;
					if(rand.nextDouble()<delayRate)
						traffic.acceptDelay = rand.nextDouble() * (maxAcceptDelay - minAcceptDelay) + minAcceptDelay;
					else
						traffic.acceptDelay = Double.MAX_VALUE;
					if(rand.nextDouble()<lostRate)
						traffic.acceptLost = rand.nextDouble() * (maxAcceptLost - minAcceptLost) + minAcceptLost;
					else
						traffic.acceptLost = 1;
					traffics.add(traffic);
				}
			}
		}
		return traffics;
	}

	public static void saveTraffic(List<Traffic> traffics, String filePath) {
		try {
			PrintWriter writer = new PrintWriter(filePath);
			writer.println("Time\tFrom Node\tTo Node\tReq.BW\tAccept. Cost\tAccept. Delay\tAccept. Lost");
			for (Traffic traffic : traffics) {
				writer.println(traffic);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
