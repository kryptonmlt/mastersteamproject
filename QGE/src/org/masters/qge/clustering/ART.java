package org.masters.qge.clustering;

import java.util.ArrayList;
import java.util.List;

import org.masters.qge.utils.VectorFunctions;

public class ART implements Clustering {

	private List<float[]> centroids;
	private float row;
	private float alpha = 0.1f;

	public ART(float row, float alpha) {
		this.row = row;
		this.alpha = alpha;
		centroids = new ArrayList<float[]>();
	}

	@Override
	public List<float[]> getCentroids() {
		return centroids;
	}

	@Override
	public Integer update(float[] point) {
		int nearestCentroid = VectorFunctions.classify(point, centroids);
		if (nearestCentroid == -1) {
			centroids.add(point);
			nearestCentroid = 0;
		} else {
			if (VectorFunctions.distance(point, centroids.get(nearestCentroid)) < row) {
				// Move centroid
				this.centroids.set(nearestCentroid,
						VectorFunctions.moveCentroid(point, centroids.get(nearestCentroid), alpha));

			} else {
				centroids.add(point);
				nearestCentroid = centroids.size() - 1;
			}
		}
		return nearestCentroid;
	}

	@Override
	public String getDescription() {
		return row + "_" + alpha;
	}
	
	@Override
	public void setCentroids(List<float[]> centroids) {
		this.centroids = centroids;
		
	}
}
