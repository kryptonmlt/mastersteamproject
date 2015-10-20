import java.util.LinkedList;


public class Cluster {
	
	private LinkedList<double[]> points=new LinkedList<double[]>();
	private double[] centroid=new double[2];
	
	public LinkedList<double[]> getPoints() {
		return points;
	}
	public void setPoints(LinkedList<double[]> points) {
		this.points = points;
	}
	public double[] getCentroid() {
		return centroid;
	}
	public void setCentroid(double[] centroid) {
		this.centroid = centroid;
	}
	
	
	
}
