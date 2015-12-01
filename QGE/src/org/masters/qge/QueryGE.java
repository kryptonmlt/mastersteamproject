package org.masters.qge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.masters.qge.clustering.ART;
import org.masters.qge.clustering.Clustering;
import org.masters.qge.clustering.OnlineKmeans;
import org.masters.qge.utils.Tools;

public class QueryGE {

	public QueryGE() {
	}

	/**
	 * Generates and runs queries depending on the parameters given.
	 * 
	 * @param queryLimit
	 *            number of queries to generate and run
	 * @param distributions
	 *            query points around which queries are generated. should be in
	 *            format x,y,..,width
	 * @param noOfAxis
	 *            number of axis used in the dataset
	 * @param k
	 *            number of clusters for queries (normally equal to the length
	 *            of distributions)
	 * @param alpha
	 *            learning rate of clustering
	 * @param theta
	 *            size of search space around each query
	 * @param dataSet
	 *            the data set itself should be of format x,y,... each with the
	 *            same number of axis
	 * @param row
	 *            maximum distance between a point and centroid for data
	 *            clustering
	 */
	public void generateAndRunQueries(int queryLimit, List<float[]> distributions, int noOfAxis, int k, float alpha,
			float theta, List<float[]> dataSet, float row) {

		// initialise clustering
		Clustering queriesOnline = new OnlineKmeans(k, alpha);
		Clustering dataArt = new ART(row, alpha);
		// array that holds a reference between query centroids and data
		// centroids
		int[] queryDataClusterMap = new int[k];
		for (int i = 0; i < queryDataClusterMap.length; i++) {
			queryDataClusterMap[i] = -1;
		}

		try {
			// initialise file writers
			BufferedWriter queryClusterWriter = new BufferedWriter(
					new FileWriter("queryclusters_" + theta + "_" + queriesOnline.getDescription() + ".txt"));
			BufferedWriter dataWriter = new BufferedWriter(
					new FileWriter("dataclusters_" + theta + "_" + dataArt.getDescription() + ".txt"));
			BufferedWriter avgDataWriter = new BufferedWriter(
					new FileWriter("AVGDATA_" + theta + "_" + queryLimit + ".txt"));
			BufferedWriter queryWriter = new BufferedWriter(
					new FileWriter("setA_" + theta + "_" + queryLimit + ".txt"));

			System.out.println("Generating queries..");
			for (int i = 0; i < queryLimit; i++) {
				printQueryCompletion(i, queryLimit);
				// generate query
				float[] query = Tools.getInstance().generateQuery(distributions, noOfAxis);
				// update online kmeans and write cluster to file
				int queryClusterId = queriesOnline.update(query);
				queryClusterWriter.write((queryClusterId + 1) + "\n");

				// run query to get avg data
				float[] dataCentroid = Tools.getInstance().getAverageDatumFromQuery(dataSet, query, theta);
				if (dataCentroid != null) {

					// update art data and write cluster to file
					int dataClusterId = dataArt.update(dataCentroid);
					dataWriter.write((dataClusterId + 1) + "\n");
					avgDataWriter.write(Arrays.toString(dataCentroid).replace("]", "").replace("[", "") + "\n");
					queryDataClusterMap[queryClusterId] = dataClusterId;

					// write set A
					writeToBufferedWriter(queryWriter, query, dataCentroid);
				}
			}
			// close writers
			queryClusterWriter.close();
			dataWriter.close();
			avgDataWriter.close();
			queryWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Generated queries..");

		// save centroids to file
		this.saveCentroids("Queries", queriesOnline);
		this.saveCentroids("AVGDATA", dataArt);

		// save the queryDataClusterMap - which represents the link between
		// query clusters and average data clusters
		try {
			BufferedWriter mapWriter = new BufferedWriter(new FileWriter("queryDataMap_" + theta + ".txt"));
			for (int i = 0; i < queryDataClusterMap.length; i++) {
				if (queryDataClusterMap[i] != -1) {
					writeToBufferedWriter(mapWriter, queriesOnline.getCentroids().get(i),
							dataArt.getCentroids().get(queryDataClusterMap[i]));
					mapWriter.flush();
				}
			}
			mapWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints a statement at 10% intervals
	 * 
	 * @param c
	 * @param querySize
	 */
	private void printQueryCompletion(int c, float querySize) {
		if (((c / querySize) * 100) % 10 == 0) {// output percentage complete..
			System.out.println("query completion: " + ((c / querySize) * 100f) + "%");
		}
	}

	/**
	 * Writes float[] to buffered writer
	 * 
	 * @param bw
	 * @param query
	 * @param avg
	 * @throws IOException
	 */
	private void writeToBufferedWriter(BufferedWriter bw, float[] query, float[] avg) throws IOException {
		bw.write(Arrays.toString(query).replace("]", "").replace("[", "") + ";"
				+ Arrays.toString(avg).replace("]", "").replace("[", "") + "\n");
	}

	/**
	 * Write the centroids to file
	 * 
	 * @param clustering
	 */
	public void saveCentroids(String name, Clustering clustering) {
		if (clustering != null) {
			String fileName = "centroids_" + name + "_" + clustering.getDescription() + ".txt";
			System.out.println("Writing " + fileName);
			BufferedWriter centroidWriter = null;
			try {
				centroidWriter = new BufferedWriter(new FileWriter(new File(fileName)));
				for (float[] cluster : clustering.getCentroids()) {
					centroidWriter.write(cluster[0] + "," + cluster[1] + "\n");
				}
				centroidWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (centroidWriter != null) {
					centroidWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Finished " + fileName);
		}
	}
}
