package org.masters.cluster.visualizer;

public class ClusterData {
	private float[] point;

	private int clusterId;

	public ClusterData(float[] point, int clusterId) {
		super();
		this.point = point;
		this.clusterId = clusterId;
	}

	public float[] getPoint() {
		return point;
	}

	public void setPoint(float[] point) {
		this.point = point;
	}

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}
}
