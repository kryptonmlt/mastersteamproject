package org.masters.qge;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.masters.qge.storage.Data;
import org.masters.qge.storage.DataStorage;
import org.masters.qge.tools.Tools;

public class QueryGE {

	private float theta = 0f;
	private int queryLimit = 0;
	private int noOfAxis = 0;
	private List<Data> queries = null;

	public QueryGE(float theta, int queryLimit, int noOfAxis) {
		this.theta = theta;
		this.queryLimit = queryLimit;
		this.noOfAxis = noOfAxis;
		queries = new ArrayList<Data>();
	}

	public void generateQueries(List<float[]> distributions) {
		System.out.println("Generating queries..");
		setQueries(Tools.generateQuerys(queryLimit, noOfAxis, distributions));
		System.out.println("Generated queries..");
	}

	public List<Data> generateAVGPoints(List<Data> queries) {
		System.out.println("Generating Average points from queries");
		List<Data> avgs = new ArrayList<Data>();
		int c = 0;
		for (Data query : queries) {
			if (((c / (float) queries.size()) * 100) % 10 == 0) {
				System.out.println("query completion: " + ((c / (float) queries.size()) * 100f) + "%");
			}
			Data d = Tools.getAverageDatumFromQuery(DataStorage.getInstance().getDataSet(), query, theta);
			if (d != null) {
				avgs.add(d);
			}
			c++;
		}
		System.out.println("Writing to AVGDATA.txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("AVGDATA_" + theta + "_" + queryLimit + ".txt"));
			for (Data avgD : avgs) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < avgD.getRow().length; i++) {
					builder.append(avgD.getRow()[i]);
					if (i + 1 < avgD.getRow().length) {
						builder.append(",");
					}
				}
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
		System.out.println("Finished writing to AVGDATA.txt");
		return avgs;
	}

	public List<Data> getQueries() {
		return queries;
	}

	public void setQueries(List<Data> queries) {
		this.queries = queries;
	}

	public void saveQueries() {
		System.out.println("Writing to queries.txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("queries_" + theta + "_" + queryLimit + ".txt"));
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
		System.out.println("Finished writing to queries.txt");
	}
}
