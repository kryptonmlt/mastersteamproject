import java.util.ArrayList;

public class ClusterN {

	private ArrayList<double[]> points = new ArrayList<double[]>();
	private double[] centroid;

	public ClusterN(int N) {
		centroid = new double[N];
	}

	public ArrayList<double[]> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<double[]> points) {
		this.points = points;
	}

	public double[] getCentroid() {
		return centroid;
	}

	public void setCentroid(double[] centroid) {
		this.centroid = centroid;
	}

}
