package org.masters.online.kmeans;

import java.util.ArrayList;
import java.util.List;

import org.masters.online.kmeans.utils.Tools;

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

	public Integer update(float[] point) {
		if (centroids.size() < k) {
			centroids.add(point);
			return centroids.size() - 1;
		} else {
			Integer nearestCentroid = Tools.classify(point, centroids);
			// Move centroid
			this.centroids.set(nearestCentroid, moveCentroid(point, nearestCentroid));

			return nearestCentroid;
		}
	}

	public float[] moveCentroid(float[] point, int nearestCentroid) {
		float[] update = Tools.subtract(point, this.centroids.get(nearestCentroid));
		update = Tools.multiply(update, alpha);
		return Tools.add(this.centroids.get(nearestCentroid), update);
	}
}
