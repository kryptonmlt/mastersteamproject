package org.masters.online.kmeans.utils;

public class Tools {

	private Tools() {

	}

	public static float distance2D(float[] p1, float[] p2) {

		float ydist = p1[1] - p2[1];
		float xdist = p1[0] - p2[0];
		float distance = (float) Math.sqrt((ydist * ydist) + (xdist * xdist));
		return distance;
	}

	public static float distance(float[] p1, float[] p2) {

		float dist = 0;
		for (int i = 0; i < p1.length; i++) {
			dist += (p1[i] - p2[i]) * (p1[i] - p2[i]);
		}
		return (float) Math.sqrt(dist);
	}

	public static int classify(float[] point, float[][] centroids) {
		float minDist = Float.MAX_VALUE;
		int ans = 0;
		for (int i = 0; i < centroids.length; i++) {
			float tempDist = Tools.distance(point, centroids[i]);
			if (tempDist < minDist) {
				minDist = tempDist;
				ans = i;
			}
		}
		return ans;
	}
}
