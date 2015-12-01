package org.masters.qge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Application {

	/**
	 * 6 arguments required: 1) path to input data file 2) theta value in float
	 * 3) M representing query limit 4) K number of clusters in online kmeans
	 * for query clustering 5) alpha which is the learning rate 6) L file which
	 * contains subspaces in format x,y,z,...,width etc...
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		if (args.length < 3) {
			throw new IllegalArgumentException(
					"6 arguments required: 1) path to input data file. 2) theta value in float 3) integer representing query limit 4) K 5) alpha 6) row 7) L file");
		}

		// read file and populate DataSet
		boolean completed = false;
		List<float[]> dataSet = new ArrayList<float[]>();

		File dataFile = new File(args[0]);
		if (!dataFile.exists()) {
			throw new IllegalArgumentException("File does not exist");
		}
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		try {
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i].trim());
				}
				dataSet.add(row);
			}
			completed = true;
		} finally {
			br.close();
		}

		// Read L File containing subspaces
		List<float[]> lPoints = new ArrayList<float[]>();
		if (args.length > 6) {
			BufferedReader lReader = new BufferedReader(new FileReader(args[6]));
			String temp = null;
			while ((temp = lReader.readLine()) != null) {
				String[] points = temp.split(",");
				float[] r = new float[points.length];
				for (int i = 0; i < points.length; i++) {
					r[i] = Float.parseFloat(points[i]);
				}
				lPoints.add(r);
			}
			lReader.close();
		} else {
			// if L file is not present add 0,0,.. as subspace with width 0.5
			float[] f = new float[dataSet.get(0).length];
			for (int i = 0; i < f.length; i++) {
				if (i < f.length - 1) {
					f[i] = 0f;
				} else {
					f[i] = 0.5f;
				}
			}
			lPoints.add(f);
		}

		// completed is true only if no errors where encountered when reading
		// the data file.
		if (completed) {
			// variables for query generation
			float theta = 0.01f;
			int queryLimit = 0;
			int k = lPoints.size();
			float alpha = 0.05f;
			float row = theta;
			int noOfAxis = dataSet.get(0).length;

			try {// read from commandline
				theta = Float.parseFloat(args[1]);
				queryLimit = Integer.parseInt(args[2]);
				if (args.length > 3) {
					k = Integer.parseInt(args[3]);
				}
				if (args.length > 4) {
					alpha = Float.parseFloat(args[4]);
				}
				if (args.length > 5) {
					row = Float.parseFloat(args[5]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Theta and alpha must be a float, query limit and k an integer");
			}

			// query generation
			QueryGE qGE = new QueryGE();
			qGE.generateAndRunQueries(queryLimit, lPoints, noOfAxis, k, alpha, theta, dataSet, row);
		} else {
			System.out.println("Please fix input file...");
		}
	}
}
