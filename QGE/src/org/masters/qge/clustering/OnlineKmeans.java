package org.masters.qge.clustering;

import java.util.ArrayList;
import java.util.List;

import org.masters.qge.utils.VectorFunctions;

public class OnlineKmeans implements Clustering {

	private Integer k;
	private List<float[]> centroids = new ArrayList<float[]>();
	private float alpha = 0.1f;

	public OnlineKmeans(Integer k, float alpha) {
		this.k = k;
		this.alpha = alpha;
	}

	@Override
	public List<float[]> getCentroids() {
		return centroids;
	}

	@Override
	public Integer update(float[] point) {
		if (centroids.size() < k) {
			centroids.add(point);
			return centroids.size() - 1;
		} else {
			Integer nearestCentroid = VectorFunctions.classify(point, centroids);
			// Move centroid
			this.centroids.set(nearestCentroid,
					VectorFunctions.moveCentroid(point, centroids.get(nearestCentroid), alpha));

			return nearestCentroid;
		}
	}

	@Override
	public String getDescription() {
		return k + "_" + alpha;
	}

	@Override
	public void setCentroids(List<float[]> centroids) {
		this.centroids = centroids;
		
	}
}
