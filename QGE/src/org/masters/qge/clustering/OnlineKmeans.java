package org.masters.qge.clustering;

import java.util.ArrayList;
import java.util.List;

import org.masters.qge.utils.VectorFunctions;

public class OnlineKmeans {

	private Integer k;
	private List<float[]> centroids = new ArrayList<float[]>();
	private float alpha = 0.1f;

	public OnlineKmeans(Integer k, float alpha) {
		this.k = k;
		this.alpha = alpha;
	}

	public List<float[]> getCentroids() {
		return centroids;
	}

	/**
	 * Update the centroid using the point
	 * @param point
	 * @return
	 */
	public Integer update(float[] point) {
		if (centroids.size() < k) {
			centroids.add(point);
			return centroids.size() - 1;
		} else {
			Integer nearestCentroid = VectorFunctions.classify(point, centroids);
			// Move centroid
			this.centroids.set(nearestCentroid, moveCentroid(point, nearestCentroid));

			return nearestCentroid;
		}
	}

	public float[] moveCentroid(float[] point, int nearestCentroid) {
		float[] update = VectorFunctions.subtract(point, this.centroids.get(nearestCentroid));
		update = VectorFunctions.multiply(update, alpha);
		return VectorFunctions.add(this.centroids.get(nearestCentroid), update);
	}

	public int getK() {
		return k;
	}

	public float getAlpha() {
		return alpha;
	}
}
