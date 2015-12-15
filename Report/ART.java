
import java.util.ArrayList;
import java.util.List;

public class ART {

	private List<float[]> centroids;
	private float row;
	private float alpha = 0.1f;

	public ART(float row, float alpha) {
		this.row = row;
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
			//check if the distance from nearest centroid is less than vigilance 'row'
			if (Tools.distance(point, centroids.get(nearestCentroid)) < row) {
				// add point to the cluster
				this.centroids.set(nearestCentroid, moveCentroid(point, nearestCentroid));

			} else {
				//create new centroid
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
