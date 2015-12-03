package org.masters.qge.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
	public float[] getAverageDatumFromQuery(List<float[]> dataSet, float[] query, float theta) {
		List<float[]> dataInTheta = new ArrayList<float[]>();
		for (float[] d : dataSet) {
			if (VectorFunctions.distance(query, d) < theta) {
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
	public float[] getAverage(List<float[]> dataInTheta) {
		if (dataInTheta.isEmpty()) {
			return null;
		}
		float[] avg = new float[dataInTheta.get(0).length];
		for (float[] d : dataInTheta) {
			for (int i = 0; i < d.length; i++) {
				avg[i] += d[i];
			}
		}
		for (int i = 0; i < avg.length; i++) {
			avg[i] = (float) (Math.round((avg[i] / dataInTheta.size()) * 10000.0) / 10000.0);
		}
		return avg;
	}

	/**
	 * Generate Query using given subspaces (lPoints)
	 * 
	 * @param lPoints
	 *            if lPoints is null use whole space. Else use gaussian
	 *            distribution on subspaces
	 * @return Data
	 */
	public float[] generateQuery(List<float[]> lPoints, int noOfAxis) {

		if (lPoints == null || lPoints.isEmpty()) {
			float[] row = new float[noOfAxis];
			for (int i = 0; i < row.length; i++) {
				row[i] = Tools.getInstance().getRandom().nextFloat() - 0.5f;
			}
			return row;
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
	private float[] getRandomPointInBox(float[] point, float width) {

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
		return result;
	}

	public Random getRandom() {
		return r;
	}

	/**
	 * Convert string[] to float[]
	 * 
	 * @param s
	 * @return
	 */
	public float[] convertToFloatArray(String[] s) {
		float[] f = new float[s.length];
		for (int i = 0; i < s.length; i++) {
			f[i] = Float.parseFloat(s[i]);
		}
		return f;
	}

	/**
	 * Writes float[] to buffered writer
	 * 
	 * @param bw
	 * @param query
	 * @param avg
	 * @throws IOException
	 */
	public void writeQueryAndADToWriter(BufferedWriter bw, float[] query, float[] avg) throws IOException {
		bw.write(Arrays.toString(query).replace("]", "").replace("[", "") + ";"
				+ Arrays.toString(avg).replace("]", "").replace("[", "") + "\n");
		bw.flush();
	}
}
