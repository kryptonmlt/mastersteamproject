package org.masters.qge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.masters.qge.storage.Data;
import org.masters.qge.storage.DataStorage;

public class Application {

	public static void main(String args[]) throws Exception {

		if (args.length < 3) {
			throw new IllegalArgumentException(
					"6 arguments required: 1) path to input data file. 2) theta value in float 3) integer representing query limit 4) K 5) alpha 6) L file");
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
			List<float[]> lPoints = new ArrayList<float[]>();
			if (args.length > 5) {
				BufferedReader lReader = new BufferedReader(new FileReader(args[5]));
				temp = null;
				while ((temp = lReader.readLine()) != null) {
					String[] points = temp.split(",");
					float[] r = new float[3];
					for (int i = 0; i < points.length; i++) {
						r[i] = Float.parseFloat(points[i]);
					}
					lPoints.add(r);
				}
				lReader.close();
			}
			int k = 0;
			float alpha = 0.0f;
			if (args.length > 3) {
				k = Integer.parseInt(args[3]);
			}
			if (args.length > 4) {
				alpha = Float.parseFloat(args[4]);
			}

			QueryGE qGE = new QueryGE(theta, queryLimit, noOfAxis, k, alpha);
			qGE.generateQueries(lPoints);
			qGE.saveQueries();
			qGE.generateAVGPoints(qGE.getQueries());
		} else {
			System.out.println("Please fix input file...");
		}
	}
}
