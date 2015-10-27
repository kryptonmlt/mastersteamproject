package org.masters.online.kmeans;

import java.util.ArrayList;
import java.util.List;

import org.masters.online.kmeans.utils.Tools;

public class OnlineKmeans {

	private List<Long> counts;
	private float[][] centroids;
	private Integer nbCluster;
	private List<float[]> initPoints = new ArrayList<float[]>();
	private float alpha = 0.1f;

	public OnlineKmeans(Integer nbCluster, float alpha) {
		this.nbCluster = nbCluster;
		this.alpha = alpha;
	}

	public float[][] getCentroids() {
		return centroids;
	}

	public Integer update(float[] point) {
		if (!this.isReady()) {
			this.initIfPossible(point);
			return null;
		} else {
			Integer nearestCentroid = Tools.classify(point, centroids);

			// Increment count
			this.counts.set(nearestCentroid, this.counts.get(nearestCentroid) + 1);

			// Move centroid
			this.centroids[nearestCentroid] = moveCentroid(point, nearestCentroid);

			return nearestCentroid;
		}
	}

	public float[] moveCentroid(float[] point, int nearestCentroid) {
		float[] update = this.subtract(point, this.centroids[nearestCentroid]);
		update = this.multiply(update, alpha);
		return this.add(this.centroids[nearestCentroid], update);
	}

	private float[] multiply(float[] update, float scalar) {
		float[] result = new float[update.length];
		for (int i = 0; i < update.length; i++) {
			result[i] = update[i] * scalar;
		}
		return result;
	}

	private float[] subtract(float[] point, float[] point2) {
		float[] result = new float[point.length];
		for (int i = 0; i < point.length; i++) {
			result[i] = point[i] - point2[i];
		}
		return result;
	}

	private float[] add(float[] vector1, float[] vector2) {

		float[] result = new float[vector1.length];
		for (int i = 0; i < vector1.length; i++) {
			result[i] = vector1[i] + vector2[i];
		}

		return result;
	}

	protected void initIfPossible(float[] features) {
		this.initPoints.add(features);

		// magic number : 10 ??!
		if (this.initPoints.size() >= this.nbCluster) {
			this.initCentroids();
		}
	}

	protected void initCentroids() {
		// Init counts
		this.counts = new ArrayList<Long>(this.nbCluster);
		for (int i = 0; i < this.nbCluster; i++) {
			this.counts.add(0L);
		}

		this.centroids = new float[this.nbCluster][];

		for (int i = 0; i < this.nbCluster; i++) {
			this.centroids[i] = initPoints.get(i);
		}

		this.initPoints.clear();
	}

	private boolean isReady() {
		boolean countsReady = this.counts != null;
		boolean centroidsReady = this.centroids != null;
		return countsReady && centroidsReady;
	}

	public void reset() {
		this.counts = null;
		this.centroids = null;
		this.initPoints = new ArrayList<float[]>();
	}
}
