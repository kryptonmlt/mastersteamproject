package org.masters.qge;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	public List<Data> generateQueries(int queryLimit, List<float[]> distributions, int noOfAxis, int k, float alpha) {
		List<Data> queries = new ArrayList<Data>();
		OnlineKmeans queriesOnline = null;
		if (k != 0) {
			queriesOnline = new OnlineKmeans(k, alpha);
		}

		System.out.println("Generating queries..");
		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter("queryclusters_" + queriesOnline.getK() + "_" + queriesOnline.getAlpha() + ".txt"));
			if (distributions == null || distributions.isEmpty()) {
				// generate totally random queries since no distributions
				// specified
				for (int i = 0; i < queryLimit; i++) {
					float[] row = new float[noOfAxis];
					for (int j = 0; j < noOfAxis; j++) {
						row[j] = Tools.getInstance().getRandom().nextFloat() - 0.5f;
					}
					this.updateOnline(queriesOnline, row, writer);
					queries.add(new Data(row));
				}
			} else {
				for (int i = 0; i < queryLimit; i++) {
					Data q = Tools.getInstance().generateQuery(distributions);
					this.updateOnline(queriesOnline, q.getRow(), writer);
					queries.add(q);
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Generated queries..");
		return queries;
	}

	/**
	 * Updates online kmeans and writes to file
	 * 
	 * @param onine
	 * @param row
	 * @param writer
	 * @throws IOException
	 */
	private void updateOnline(OnlineKmeans onine, float[] row, BufferedWriter writer) throws IOException {
		if (onine != null) {
			int clusterId = onine.update(row);
			writer.write((clusterId + 1) + "\n");
		}
	}

	/**
	 * Generates the average points using the data and queries provided
	 * 
	 * @param queries
	 */
	public List<Data> generateAVGPoints(List<Data> dataSet, List<Data> queries, float theta) {
		List<Data> avgData = new ArrayList<Data>();
		System.out.println("Generating Average points from queries");
		int c = 0;
		for (Data query : queries) {
			if (((c / (float) queries.size()) * 100) % 10 == 0) {
				System.out.println("query completion: " + ((c / (float) queries.size()) * 100f) + "%");
			}
			Data d = Tools.getInstance().getAverageDatumFromQuery(dataSet, query, theta);
			if (d != null) {
				avgData.add(d);
			}
			c++;
		}
		return avgData;
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
