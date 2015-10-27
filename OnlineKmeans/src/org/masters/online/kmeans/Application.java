package org.masters.online.kmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.masters.online.kmeans.utils.Tools;

public class Application {

	public static List<ClusterData> data = new ArrayList<ClusterData>();

	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			System.out.println("ARGS: 1)DATA.txt path 2) number of clusters integer 3) alpha learning value float");
			return;
		}
		int nbCluster = Integer.parseInt(args[1]);
		float alpha = Float.parseFloat(args[2]);
		OnlineKmeans kMeans = new OnlineKmeans(nbCluster, alpha);
		kMeans.reset();

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
				Integer clusterId = kMeans.update(row);
				if (clusterId != null) {
					data.add(new ClusterData(row, clusterId));
				} else {
					data.add(new ClusterData(row, -1));
				}
			}
		} finally {
			br.close();
		}
		if(kMeans.getCentroids() == null){
			System.out.println("Not enough points");
		}
		System.out.println("Clustering ready ...");
		for (int i = 0; i < nbCluster; i++) {
			int clusterId = Tools.classify(data.get(i).getPoint(), kMeans.getCentroids());
			data.get(i).setClusterId(clusterId);

		}
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File("pointsclusters_" + nbCluster + "_" + alpha+ ".txt")));
		BufferedWriter bw2 = new BufferedWriter(
				new FileWriter(new File("pointsclusters2_" + nbCluster + "_" + alpha + ".txt")));
		BufferedWriter bw3 = new BufferedWriter(
				new FileWriter(new File("centroids_" + nbCluster + "_" + alpha + ".txt")));
		System.out.println("Writing pointsclusters.txt ...");
		for (ClusterData cluster : data) {
			bw.write((cluster.getClusterId() + 1) + "\n");
		}
		System.out.println("Writing pointsclusters2.txt ...");
		for (ClusterData d : data) {
			bw2.write((Tools.classify(d.getPoint(), kMeans.getCentroids()) + 1) + "\n");
		}
		System.out.println("Writing centroids.txt ...");
		for (float[] cluster : kMeans.getCentroids()) {
			bw3.write(cluster[0] + "," + cluster[1] + "\n");
		}
		System.out.println("Finished!");
		bw.close();
		bw2.close();
		bw3.close();
	}
}
