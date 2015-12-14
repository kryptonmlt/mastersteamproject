package org.masters.qge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.masters.qge.clustering.ART;
import org.masters.qge.clustering.Clustering;
import org.masters.qge.clustering.OnlineKmeans;
import org.masters.qge.storage.Data;
import org.masters.qge.utils.Tools;

public class QueryGE {

	public QueryGE() {
	}

	/**
	 * Generates queries depending on the distributions. If distributions is
	 * null then all dataset is used.
	 * 
	 * @param queryLimit
	 * @param distributions
	 *            Contains the subspaces to use including width of each
	 *            subspace, Example: x,y,z,width
	 * @param noOfAxis
	 *            number of axis the queries generated will have.
	 * @param k
	 *            number of clusters for query clustering. Use 0 for no
	 *            clustering.
	 * @param alpha
	 *            learning rate for query clustering
	 */
	public List<Data> generateQueries(int queryLimit, List<float[]> distributions, int noOfAxis, int k, float alpha,
			float theta) {
		List<Data> queries = new ArrayList<Data>();
		Clustering queriesOnline = new OnlineKmeans(k, alpha);
		try {
			BufferedWriter queryWriter = new BufferedWriter(
					new FileWriter("queryclusters_" + theta + "_" + queriesOnline.getDescription() + ".txt"));

			System.out.println("Generating queries..");
			for (int i = 0; i < queryLimit; i++) {
				Data q = Tools.getInstance().generateQuery(distributions, noOfAxis);
				queries.add(q);
				// update online kmeans and write cluster to file
				int clusterId = queriesOnline.update(q.getRow());
				queryWriter.write((clusterId + 1) + "\n");
			}
			queryWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Generated queries..");
		this.saveCentroids("", queriesOnline);
		return queries;
	}

	/**
	 * Generates the average points using the data and queries provided Also
	 * clusters data using ART
	 * 
	 * @param queries
	 */
	public List<Data> generateAVGPoints(List<Data> dataSet, List<Data> queries, float theta, float row, float alpha) {
		List<Data> avgData = new ArrayList<Data>();
		Clustering dataArt = new ART(row, alpha);
		try {
			BufferedWriter dataWriter = new BufferedWriter(
					new FileWriter("dataclusters_" + theta + "_" + dataArt.getDescription() + ".txt"));

			System.out.println("Generating Average points from queries");
			int c = 0;
			for (Data query : queries) {
				printQueryCompletion(c, queries.size());
				Data d = Tools.getInstance().getAverageDatumFromQuery(dataSet, query, theta);
				if (d != null) {
					avgData.add(d);
					int clusterId = dataArt.update(d.getRow());
					dataWriter.write((clusterId + 1) + "\n");
				}
				c++;
			}
			dataWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.saveCentroids("", dataArt);
		return avgData;
	}

	private void printQueryCompletion(int c, float querySize) {
		if (((c / querySize) * 100) % 10 == 0) {// output percentage complete..
			System.out.println("query completion: " + ((c / querySize) * 100f) + "%");
		}
	}

	public void generateAndRunQueries(int queryLimit, List<float[]> distributions, int noOfAxis, int k, float alpha,
			float theta, List<Data> dataSet, float row) {
		Clustering queriesOnline = new OnlineKmeans(k, alpha);
		Clustering dataArt = new ART(row, alpha);
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
					new FileWriter("seta_" + theta + "_" + queryLimit + ".txt"));

			System.out.println("Generating queries..");
			for (int i = 0; i < queryLimit; i++) {
				printQueryCompletion(i, queryLimit);
				// generate query
				Data query = Tools.getInstance().generateQuery(distributions, noOfAxis);
				// update online kmeans and write cluster to file
				int queryClusterId = queriesOnline.update(query.getRow());
				Data dataCentroid = new Data(queriesOnline.getCentroids().get(queryClusterId));
				queryClusterWriter.write((queryClusterId + 1) + "\n");
				// run query to get avg data
//				Data dataCentroid = Tools.getInstance().getAverageDatumFromQuery(dataSet, query, theta);
				if (dataCentroid != null) {
					int dataClusterId = dataArt.update(dataCentroid.getRow());
					dataWriter.write((dataClusterId + 1) + "\n");
					avgDataWriter
							.write(Arrays.toString(dataCentroid.getRow()).replace("]", "").replace("[", "") + "\n");
					queryDataClusterMap[queryClusterId] = dataClusterId;

					// write set A
					writeToBufferedWriter(queryWriter, query.getRow(), dataCentroid.getRow());
				}
			}
			queryClusterWriter.close();
			dataWriter.close();
			avgDataWriter.close();
			queryWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Generated queries..");
		this.saveCentroids("Queries", queriesOnline);
		this.saveCentroids("AVGDATA", dataArt);
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

	private void writeToBufferedWriter(BufferedWriter bw, float[] query, float[] avg) throws IOException {
		bw.write(Arrays.toString(query).replace("]", "").replace("[", "") /*+ ";"
				+ Arrays.toString(avg).replace("]", "").replace("[", "")*/ + "\n");
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

	/**
	 * Saves the queries to a file
	 */
	public void saveQueries(List<Data> queries, float theta, int queryLimit) {
		String fileName = "queries_" + theta + "_" + queryLimit + ".txt";
		System.out.println("Writing to " + fileName);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
			for (Data q : queries) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < q.getRow().length; i++) {
					builder.append(q.getRow()[i]);
					builder.append(",");
				}
				builder.append(theta);
				builder.append("\n");
				bw.write(builder.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Finished writing to " + fileName);
	}

	/**
	 * Saves the Average data to a file
	 */
	public void saveAvgData(List<Data> avgData, float theta, int queryLimit) {
		String fileName = "AVGDATA_" + theta + "_" + queryLimit + ".txt";
		System.out.println("Writing to " + fileName);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
			for (Data avgD : avgData) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < avgD.getRow().length; i++) {
					builder.append(avgD.getRow()[i]);
					if (i + 1 < avgD.getRow().length) {
						builder.append(",");
					}
				}
				builder.append("\n");
				bw.write(builder.toString());
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Finished writing to " + fileName);
	}
}
