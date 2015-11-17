package org.masters.qge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.masters.qge.storage.Data;

public class Application {

	/**
	 * 6 arguments required: 1) path to input data file 2) theta value in float
	 * 3) M representing query limit 4) K number of clusters in online kmeans
	 * for query clustering 5) alpha which is the learning rate in online kmeans
	 * for query clustering 6) L file which contains subspaces in format
	 * x,y,z,...,width etc...
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		if (args.length < 3) {
			throw new IllegalArgumentException(
					"6 arguments required: 1) path to input data file. 2) theta value in float 3) integer representing query limit 4) K 5) alpha 6) L file");
		}

		// read file and populate DataSet
		boolean completed = false;
		List<Data> dataSet = new ArrayList<Data>();

		File input = new File(args[0]);
		if (!input.exists()) {
			throw new IllegalArgumentException("File does not exist");
		}
		BufferedReader br = new BufferedReader(new FileReader(input));
		try {
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i].trim());
				}
				dataSet.add(new Data(row));
			}
			completed = true;
		} finally {
			br.close();
		}

		// completed is true only if no errors where encountered when reading
		// the data file.
		if (completed) {
			// variables for query generation
			float theta = 0.0f;
			int queryLimit = 0;
			int k = 0;
			float alpha = 0.0f;
			int noOfAxis = dataSet.get(0).getRow().length;

			try {// read from commandline
				theta = Float.parseFloat(args[1]);
				queryLimit = Integer.parseInt(args[2]);
				if (args.length > 3) {
					k = Integer.parseInt(args[3]);
				}
				if (args.length > 4) {
					alpha = Float.parseFloat(args[4]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Theta and alpha must be a float, query limit and k an integer");
			}

			// Read L File containing subspaces
			List<float[]> lPoints = new ArrayList<float[]>();
			if (args.length > 5) {
				BufferedReader lReader = new BufferedReader(new FileReader(args[5]));
				String temp = null;
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

			// query generation
			QueryGE qGE = new QueryGE();
			List<Data> queries = qGE.generateQueries(queryLimit, lPoints, noOfAxis, k, alpha);
			qGE.saveQueries(queries, theta, queryLimit);
			List<Data> avgData = qGE.generateAVGPoints(dataSet, queries, theta);
			qGE.saveAvgData(avgData, theta, queryLimit);
		} else {
			System.out.println("Please fix input file...");
		}
	}
}
