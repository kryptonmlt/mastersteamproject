
import java.util.ArrayList;
import java.util.List;

public class ART {

	// private List<Long> counts;
	private List<float[]> centroids;
	private float theta;
	private float alpha = 0.1f;

	public ART(float theta, float alpha) {
		this.theta = theta;
		this.alpha = alpha;
		centroids = new ArrayList<float[]>();
	}

	public List<float[]> getCentroids() {
		return centroids;
	}

	public Integer update(float[] point) {
		int nearestCentroid = Tools.classify(point, centroids);
		if (nearestCentroid == -1) {
			centroids.add(point);
			nearestCentroid = 0;
		} else {
			if (Tools.distance(point, centroids.get(nearestCentroid)) < theta) {
				// Increment count
				// this.counts.set(nearestCentroid,
				// this.counts.get(nearestCentroid) + 1);

				// Move centroid
				this.centroids.set(nearestCentroid, moveCentroid(point, nearestCentroid));

			} else {
				centroids.add(point);
				nearestCentroid = centroids.size() - 1;
			}
		}

		return nearestCentroid;
	}

	public float[] moveCentroid(float[] point, int nearestCentroid) {
		float[] update = Tools.subtract(point, this.centroids.get(nearestCentroid));
		update = Tools.multiply(update, alpha);
		return Tools.add(this.centroids.get(nearestCentroid), update);
	}
}
