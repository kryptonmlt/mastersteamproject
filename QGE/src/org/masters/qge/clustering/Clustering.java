package org.masters.qge.clustering;

import java.util.List;

public interface Clustering {

	List<float[]> getCentroids();

	/**
	 * Update the centroid using the point
	 * 
	 * @param point
	 * @return
	 */
	Integer update(float[] point);
	
	String getDescription();
	
	void setCentroids(List<float[]> centroids);
}
