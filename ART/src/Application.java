import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {

	public static List<ClusterData> data = new ArrayList<ClusterData>();

	public static void main(String[] args) throws NumberFormatException, IOException {
		if (args.length != 3) {
			System.out.println("ARGS: 1)DATA.txt path 2) distance float 3) alpha learning value float");
			return;
		}
		float distance = Float.parseFloat(args[1]);
		float alpha = Float.parseFloat(args[2]);
		ART art = new ART(distance, alpha);

		File dataFileInput = new File(args[0]);
		if (!dataFileInput.exists()) {
			throw new IllegalArgumentException("Data File does not exist");
		}

		// read file and populate Data Store
		BufferedReader br = new BufferedReader(new FileReader(dataFileInput));
		String temp = null;

		System.out.println("Starting clustering...");
		try {
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				Integer clusterId = art.update(row);
				data.add(new ClusterData(row, clusterId));
			}
		} finally {
			br.close();
		}
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("pointsclusters_" + distance + "_" + alpha + ".txt")));
		System.out.println("Writing pointsclusters.txt ...");
		for (ClusterData cluster : data) {
			bw.write((cluster.getClusterId() + 1) + "\n");
		}
		bw.flush();
		bw.close();
	}
}
