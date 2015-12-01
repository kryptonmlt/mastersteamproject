package org.masters.qge.clustering;

import java.util.List;

public interface Clustering {

	List<float[]> getCentroids();

	/**
	 * Update the centroid using the point
	 * 
	 * @param point
	 * @return cluster id the point is associated with
	 */
	Integer update(float[] point);
	
	/**
	 * Description of clustering technique example alpha and K or row
	 * @return
	 */
	String getDescription();
	
	/**
	 * Sets the centroids of the clustering technique. Can be used in initialization
	 * @param centroids
	 */
	void setCentroids(List<float[]> centroids);
}
