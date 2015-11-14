package org.masters.qge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.masters.qge.storage.Data;
import org.masters.qge.storage.DataStorage;

public class Application {

	public static void main(String args[]) throws Exception {

		if (args.length < 3) {
			throw new IllegalArgumentException(
					"2 arguments required: 1) path to input data file. 2) theta value in float 3) integer representing query limit 4) L, Number of distributions 5...) distributions");
		}

		File input = new File(args[0]);
		if (!input.exists()) {
			throw new IllegalArgumentException("File does not exist");
		}
		// read file and populate Data Store
		BufferedReader br = new BufferedReader(new FileReader(input));
		String temp = null;

		boolean completed = false;
		try {
			boolean first = true;
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				if (first) {
					DataStorage.getInstance().setColumns(d);
					first = false;
				}
				if (d.length != DataStorage.getInstance().getColumns().length) {
					throw new IllegalArgumentException("Number of columns doesnt match data in input file..." + temp);
				}
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				DataStorage.getInstance().getDataSet().add(new Data(row));
			}
			completed = true;
		} finally {
			br.close();

		}
		if (completed) { // no errors in populating data
			float theta = 0.0f;
			int queryLimit = 0;
			int noOfAxis = DataStorage.getInstance().getDataSet().get(0).getRow().length;
			try {
				theta = Float.parseFloat(args[1]);
				queryLimit = Integer.parseInt(args[2]);
			} catch (Exception e) {
				System.out.println("Theta (2nd parameter) must be in float format example 0.1");
			}

			float[] distributions = null;

			if (args.length > 3) {
				int LwithoutValue = 0;
				int distStart = 0;
				float equalDistribution = 0;

				int L = Integer.parseInt(args[3]);
				if (L < 0) {
					throw new Exception("L must be positive");
				}
				if (L != 0) {
					distributions = new float[L];
				}
				LwithoutValue = L;
				float totalL = 0;
				if (args.length > 4) {
					int j = 0;
					LwithoutValue = L - (args.length - 4);
					if (LwithoutValue < 0) {
						throw new Exception("There cannot be more L Distributions than number of L");
					}
					for (int i = 4; i < args.length; i++, j++) {
						distributions[j] = Float.parseFloat(args[i]);
						totalL += distributions[j];
					}
					distStart = j;
					if (totalL > 1 && totalL < 0) {
						throw new Exception("L Distributions must add up to a value between 0 and 1");
					}
				}
				equalDistribution = (1 - totalL) / LwithoutValue;

				for (int i = distStart; i < distStart + LwithoutValue; i++) {
					distributions[i] = equalDistribution;
				}
			}

			QueryGE qGE = new QueryGE(theta, queryLimit, noOfAxis);
			qGE.generateQueries(distributions);
			qGE.saveQueries();
			qGE.generateAVGPoints(qGE.getQueries());
		} else {
			System.out.println("Please fix input file...");
		}
	}
}
