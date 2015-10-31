import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Application {
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

		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("pointsclusters_" + distance + "_" + alpha + ".txt")));

		System.out.println("Starting clustering and writing to file...");
		try {
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				Integer clusterId = art.update(row);
				bw.write((clusterId + 1) + "\n");
			}
		} finally {
			br.close();
		}
		bw.flush();
		bw.close();
		BufferedWriter bw3 = new BufferedWriter(
				new FileWriter(new File("centroids_" + distance + "_" + alpha + ".txt")));
		System.out.println("Writing centroids.txt ...");
		for (float[] cluster : art.getCentroids()) {
			for (int i = 0; i < cluster.length; i++) {
				if (i < cluster.length - 1) {
					bw3.write(cluster[i] + ", ");
				} else {
					bw3.write(cluster[i] + "");
				}
			}
			bw3.write("\n");
		}
		System.out.println("Finished!");
		bw3.close();
		System.out.println("Finished Successfully...");
	}
}
