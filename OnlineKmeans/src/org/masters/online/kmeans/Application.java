package org.masters.online.kmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Application {

	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			System.out.println("ARGS: 1)DATA.txt path 2) number of clusters integer 3) alpha learning value float");
			return;
		}
		int k = Integer.parseInt(args[1]);
		float alpha = Float.parseFloat(args[2]);
		OnlineKmeans kMeans = new OnlineKmeans(k, alpha);

		File dataFileInput = new File(args[0]);
		if (!dataFileInput.exists()) {
			throw new IllegalArgumentException("Data File does not exist");
		}

		// read file and populate Data Store
		BufferedReader br = new BufferedReader(new FileReader(dataFileInput));
		String temp = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("pointsclusters_" + k + "_" + alpha + ".txt")));

		System.out.println("Starting clustering and writing to file...");
		try {
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				Integer clusterId = kMeans.update(row);
				bw.write((clusterId + 1) + "\n");
			}
		} finally {
			br.close();
		}
		System.out.println("Clustering ready ...");
		bw.close();
		System.out.println("Writing centroids.txt ...");
		BufferedWriter centroidWriter = new BufferedWriter(
				new FileWriter(new File("centroids_" + k + "_" + alpha + ".txt")));
		for (float[] cluster : kMeans.getCentroids()) {
			centroidWriter.write(cluster[0] + "," + cluster[1] + "\n");
		}
		System.out.println("Finished!");
		centroidWriter.close();
	}
}
