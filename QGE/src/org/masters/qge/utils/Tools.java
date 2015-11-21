package org.masters.qge.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.masters.qge.storage.Data;

public class Tools {

	private static Tools instance = null;
	private Random r = null;

	private Tools() {
		r = new Random();
	}

	public static Tools getInstance() {
		if (instance == null) {
			instance = new Tools();
		}
		return instance;
	}

	/**
	 * Get the average result of the data from that query
	 * 
	 * @param dataSet
	 * @param query
	 * @param theta
	 *            search area next to query
	 * @return
	 */
	public Data getAverageDatumFromQuery(List<Data> dataSet, Data query, float theta) {
		List<Data> dataInTheta = new ArrayList<Data>();
		for (Data d : dataSet) {
			if (VectorFunctions.distance(query.getRow(), d.getRow()) < theta) {
				dataInTheta.add(d);
			}
		}
		return getAverage(dataInTheta);
	}

	/**
	 * Get average of a list of points
	 * 
	 * @param dataInTheta
	 * @return
	 */
	public Data getAverage(List<Data> dataInTheta) {
		if (dataInTheta.isEmpty()) {
			return null;
		}
		float[] avg = new float[dataInTheta.get(0).getRow().length];
		for (Data d : dataInTheta) {
			for (int i = 0; i < dataInTheta.get(0).getRow().length; i++) {
				avg[i] += d.getRow()[i];
			}
		}
		for (int i = 0; i < avg.length; i++) {
			avg[i] = (float) (Math.round((avg[i] / dataInTheta.size()) * 10000.0) / 10000.0);
		}
		return new Data(avg);
	}

	/**
	 * Generate Query using given subspaces (lPoints)
	 * 
	 * @param lPoints
	 *            if lPoints is null use whole space. Else use gaussian
	 *            distribution on subspaces
	 * @return Data
	 */
	public Data generateQuery(List<float[]> lPoints, int noOfAxis) {

		if (lPoints == null || lPoints.isEmpty()) {
			float[] row = new float[noOfAxis];
			for (int i = 0; i < row.length; i++) {
				row[i] = Tools.getInstance().getRandom().nextFloat() - 0.5f;
			}
			return new Data(row);
		}

		int J = r.nextInt(lPoints.size()); // select random subspace
		float[] input = lPoints.get(J);

		float[] points = new float[input.length - 1];
		float width = input[input.length - 1];
		for (int i = 0; i < input.length - 1; i++) {
			points[i] = input[i];
		}
		return this.getRandomPointInBox(points, width);
	}

	/**
	 * Generate a random point in a box given center of box and width
	 * 
	 * @param point
	 *            centre of box
	 * @param width
	 *            of box
	 * @return
	 */
	private Data getRandomPointInBox(float[] point, float width) {

		float[] result = new float[point.length];
		for (int i = 0; i < result.length; i++) {
			float g = 0.0f;
			boolean found = false;
			while (!found) {
				g = (float) ((r.nextGaussian() * (width / 3)) + point[i]);
				if (g < (point[i] + width) && g > (point[i] - width)) {
					found = true;
				}
			}
			result[i] = g;
		}
		return new Data(result);
	}

	public Random getRandom() {
		return r;
	}
}
