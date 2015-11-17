package org.masters.qge.tools;

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

	public float distance(float[] p1, float[] p2) {

		float dist = 0;
		for (int i = 0; i < p1.length; i++) {
			dist += (p1[i] - p2[i]) * (p1[i] - p2[i]);
		}
		return (float) Math.sqrt(dist);
	}

	public Data getAverageDatumFromQuery(List<Data> dataSet, Data query, float theta) {
		List<Data> dataInTheta = new ArrayList<Data>();
		for (Data d : dataSet) {
			if (this.distance(query.getRow(), d.getRow()) < theta) {
				dataInTheta.add(d);
			}
		}
		return getAverage(dataInTheta);
	}

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

	public Data generateQuery(List<float[]> lPoints) {
		int J = r.nextInt(lPoints.size()); // select random subspace
		return this.getRandomPointInBox(lPoints.get(J));
	}

	private Data getRandomPointInBox(float[] input) {
		float[] points = new float[input.length - 1];
		float width = input[input.length - 1];
		for (int i = 0; i < input.length - 1; i++) {
			points[i] = input[i];
		}

		float[] result = new float[points.length];
		for (int i = 0; i < result.length; i++) {
			float g = 0.0f;
			boolean found = false;
			while (!found) {
				g = (float) ((r.nextGaussian() * (width / 3)) + points[i]);
				if (g < (points[i] + width) && g > (points[i] - width)) {
					found = true;
				}
			}
			result[i] = g;
		}
		return new Data(result);
	}

	public int classify(float[] point, List<float[]> centroids) {
		float minDist = Float.MAX_VALUE;
		int ans = 0;
		for (int i = 0; i < centroids.size(); i++) {
			float tempDist = this.distance(point, centroids.get(i));
			if (tempDist < minDist) {
				minDist = tempDist;
				ans = i;
			}
		}
		return ans;
	}

	public float[] multiply(float[] update, float scalar) {
		float[] result = new float[update.length];
		for (int i = 0; i < update.length; i++) {
			result[i] = update[i] * scalar;
		}
		return result;
	}

	public float[] subtract(float[] point, float[] point2) {
		float[] result = new float[point.length];
		for (int i = 0; i < point.length; i++) {
			result[i] = point[i] - point2[i];
		}
		return result;
	}

	public float[] add(float[] vector1, float[] vector2) {

		float[] result = new float[vector1.length];
		for (int i = 0; i < vector1.length; i++) {
			result[i] = vector1[i] + vector2[i];
		}

		return result;
	}

	public Random getRandom() {
		return r;
	}
}
