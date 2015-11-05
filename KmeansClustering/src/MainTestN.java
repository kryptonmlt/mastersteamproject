import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainTestN {

	public static void main(String[] args) {

		int N = Integer.parseInt(args[2]);
		OfflineKmeansN km = new OfflineKmeansN(args[0],
				Integer.parseInt(args[1]), N);
		ArrayList<ClusterN> partition = km.update();

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("centroids_" + args[1] + ".csv")));
			for (int i = 0; i < partition.size(); i++) {
				for (int j = 0; j < N; j++) {
					out.write(partition.get(i).getCentroid()[0] + "");
					if (j != N)
						out.write(",");
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("clusters_" + args[1] + ".csv")));
			for (int i = 0; i < partition.size(); i++) {
				for (int j = 0; j < partition.get(i).getPoints().size(); j++) {
					for (int k = 0; k < N; k++) {
						out.write(partition.get(i).getPoints().get(j)[k] + "");
						if (k != N)
							out.write(",");
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Integer> pointsclusters = km.getPointsClusters();

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("pointsclusters_" + args[1] + ".csv")));
			for (int i = 0; i < pointsclusters.size(); i++) {
				out.write(pointsclusters.get(i) + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
