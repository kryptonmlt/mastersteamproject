package org.masters.qge.utils;

import java.util.List;

public class VectorFunctions {

	private VectorFunctions() {
	}

	/**
	 * Distance between two vectors
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static float distance(float[] p1, float[] p2) {
		float dist = 0;
		for (int i = 0; i < p1.length; i++) {
			dist += (p1[i] - p2[i]) * (p1[i] - p2[i]);
		}
		return (float) Math.sqrt(dist);
	}

	/**
	 * Finds the closest cluster to this point
	 * @param point
	 * @param centroids
	 * @return
	 */
	public static int classify(float[] point, List<float[]> centroids) {
		float minDist = Float.MAX_VALUE;
		int ans = -1;
		for (int i = 0; i < centroids.size(); i++) {
			float tempDist = VectorFunctions.distance(point, centroids.get(i));
			if (tempDist < minDist) {
				minDist = tempDist;
				ans = i;
			}
		}
		return ans;
	}

	public static float[] multiply(float[] update, float scalar) {
		float[] result = new float[update.length];
		for (int i = 0; i < update.length; i++) {
			result[i] = update[i] * scalar;
		}
		return result;
	}

	public static float[] subtract(float[] point, float[] point2) {
		float[] result = new float[point.length];
		for (int i = 0; i < point.length; i++) {
			result[i] = point[i] - point2[i];
		}
		return result;
	}

	public static float[] add(float[] vector1, float[] vector2) {
		float[] result = new float[vector1.length];
		for (int i = 0; i < vector1.length; i++) {
			result[i] = vector1[i] + vector2[i];
		}
		return result;
	}

	public static float[] moveCentroid(float[] point, float[] centroid, float alpha) {
		float[] update = VectorFunctions.subtract(point, centroid);
		update = VectorFunctions.multiply(update, alpha);
		return VectorFunctions.add(centroid, update);
	}
}
