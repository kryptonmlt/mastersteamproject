package org.masters.qge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.masters.qge.clustering.Clustering;
import org.masters.qge.clustering.OnlineKmeans;
import org.masters.qge.utils.Tools;

public class LearningApplication {

	/**
	 * Input: training set K alpha The aim of this application is to learn the
	 * training set and output to a file called queryDataMap.. the query
	 * representatives with their output
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			throw new Exception("At least 1 argument needed: TrainingSetFile , k and alpha are optional");
		}
		int k = 1;
		float alpha = 0.05f;

		if (args.length < 3) {
			System.out.println("K and alpha are not given, defaults will be used");
		} else {
			try {
				k = Integer.parseInt(args[1]);
				alpha = Float.parseFloat(args[2]);
			} catch (Exception e) {
				System.out.println("Error parsing K and alpha, defaults will be used");
			}
		}

		// initialise clustering
		Clustering queryClustering = new OnlineKmeans(k, alpha);
		Clustering dataClustering = new OnlineKmeans(k, alpha);

		// array that holds a reference between query centroids and data
		// centroids
		int[] queryDataClusterMap = new int[k];
		for (int i = 0; i < queryDataClusterMap.length; i++) {
			queryDataClusterMap[i] = -1;
		}

		BufferedReader trainingSetReader = new BufferedReader(new FileReader(args[0]));
		String temp = null;

		System.out.println("Starting to learn training set using k=" + k + ", alpha=" + alpha);
		// read and learn the training set
		while ((temp = trainingSetReader.readLine()) != null) {
			String[] queryAvgData = temp.split(";");

			float[] query = Tools.getInstance().convertToFloatArray(queryAvgData[0].split(","));
			float[] avgData = Tools.getInstance().convertToFloatArray(queryAvgData[1].split(","));

			// get clustering of query and data - and link them
			int queryClusterId = queryClustering.update(query);
			int dataClusterId = dataClustering.update(avgData);
			queryDataClusterMap[queryClusterId] = dataClusterId;
		}
		trainingSetReader.close();
		System.out.println("Finished Learning training set");

		// save the queryDataClusterMap - which represents the link between
		// query clusters and average data clusters
		System.out.println("Starting to write queryDataMap");
		try {
			BufferedWriter mapWriter = new BufferedWriter(new FileWriter("queryDataMap_" + k + "_" + alpha + ".txt"));
			for (int i = 0; i < queryDataClusterMap.length; i++) {
				if (queryDataClusterMap[i] != -1) {
					Tools.getInstance().writeQueryAndADToWriter(mapWriter, queryClustering.getCentroids().get(i),
							dataClustering.getCentroids().get(queryDataClusterMap[i]));
					mapWriter.flush();
				}
			}
			mapWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("queryDataMap written");
	}
}
