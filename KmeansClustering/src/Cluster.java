import java.util.ArrayList;


public class Cluster {
	
	private ArrayList<double[]> points=new ArrayList<double[]>();
	private double[] centroid=new double[2];

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
