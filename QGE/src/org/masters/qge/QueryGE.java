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

	float theta = 0f;
	int queryLimit = 0;

	public QueryGE(float theta, int queryLimit) {
		this.theta = theta;
		this.queryLimit = queryLimit;
	}

	public List<Data> generateQueries() {

		float[] max = { Tools.MAX, Tools.MAX };
		float[] min = { Tools.MIN, Tools.MIN };
		System.out.println("Generating queries..");
		List<Data> queries = Tools.generateQuerys(new Data(max), new Data(min), theta, queryLimit);

		System.out.println("Generating Average points from queries");
		List<Data> avgs = new ArrayList<Data>();
		for (Data query : queries) {
			Data d = Tools.getAverageDatumFromQuery(DataStorage.getInstance().getDataSet(), query, theta);
			if (d != null) {
				avgs.add(d);
			}
		}
		System.out.println("Writing to AVGDATA.txt");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("AVGDATA.txt"));
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
}
