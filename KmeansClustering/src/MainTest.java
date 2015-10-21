import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainTest {

	public static void main(String[] args) {

		OfflineKmeans km = new OfflineKmeans("data.txt", 5);
		ArrayList<Cluster> partition=km.update();
		for (int i = 0; i < partition.size(); i++) {
			System.out.print(partition.get(i).getCentroid()[0] + ",");
			System.out.print(partition.get(i).getCentroid()[1] + "\n");
		}

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("centroids.csv")));
			for (int i = 0; i < partition.size(); i++) {
				out.write(partition.get(i).getCentroid()[0] + "," + partition.get(i).getCentroid()[1]
						+ "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("clusters.csv")));
			for (int i = 0; i < partition.size(); i++) {
				for (int j = 0; j < partition.get(i).getPoints().size(); j++) {
					out.write(partition.get(i).getPoints().get(j)[0]+","
							+ partition.get(i).getPoints().get(j)[1]+" ");
				}
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
