import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class OfflineKmeansN {

	private ArrayList<double[]> data = new ArrayList<double[]>();
	private ArrayList<ClusterN> Clusters = new ArrayList<ClusterN>();
	private ArrayList<Integer> pointsclusters = new ArrayList<Integer>();

	// the constructor receives two parameters: the path of the .txt file and
	// the number of centroids

	public OfflineKmeansN(String s, int k, int N) {
		
		//reading input files
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(s));
			while ((line = br.readLine()) != null) {
				String[] result = line.split(",");
				double[] point = new double[N];
				for (int i = 0; i < N; i++) {
					point[i] = Double.parseDouble(result[i]);
				}
				data.add(point);
			}

			for (int j = 0; j < data.size(); j++) {
				pointsclusters.add(0);
			}
			//randomly initializing centroids
			ArrayList<Integer> random = new ArrayList<Integer>();
			for (int j = 0; j < k; j++) {
				random.add(j);
			}
			Collections.shuffle(random);

			for (int i = 0; i < k; i++) {
				Clusters.add(new ClusterN(N));
			}

			for (int i = 0; i < k; i++) {
				Clusters.get(i).setCentroid(data.get(random.get(i)).clone());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ClusterN> update() {

		while (true) {

			int counter = 0;

			for (int i = 0; i < data.size(); i++) {

				double max = 0;
				int maxIndex = -1;
				for (int j = 0; j < Clusters.size(); j++) {
					double powsum = 0;
					//calculating Euclidean distances
					for (int k = 0; k < data.get(0).length; k++) {
						powsum += Math.pow(data.get(i)[k]
								- Clusters.get(j).getCentroid()[k], 2);
					}
					double temp = Math.sqrt(powsum);
					//searching for the smallest distance
					if (maxIndex == -1 || temp <= max) {
						max = temp;
						maxIndex = j;
					}
				}
				pointsclusters.set(i, maxIndex + 1);
				Clusters.get(maxIndex).getPoints().add(data.get(i));
			}

			for (int k = 0; k < Clusters.size(); k++) {
				
				//initializing centroids
				double[] c_d = new double[data.get(0).length];
				for (int j = 0; j < c_d.length; j++) {
					c_d[j] = 0;
				}

				int points = Clusters.get(k).getPoints().size();
				
				//calculating the potential centroids updates
				for (int i = 0; i < points; i++) {
					for (int w = 0; w < c_d.length; w++) {
						c_d[w] += Clusters.get(k).getPoints().get(i)[w];
					}
				}

				if (points > 0) {
					for (int w = 0; w < c_d.length; w++) {
						c_d[w] /= points;
					}
				}

				double[] conditions = new double[c_d.length];
				
				//calculating conditions: shift length
				for (int w = 0; w < c_d.length; w++) {
					conditions[w] = Math.abs(Clusters.get(k).getCentroid()[w]
							- c_d[w]);
				}
				
				int condcounter = 0;
				//checking if conditions value are under convergence error
				for(int w=0;w<conditions.length;w++){
					if(conditions[w]<0.05)
					//condcounter is increased each time a centroid is not eligible for further updates
						condcounter++;
				}
				//if condcounter is equal to the number of centroids counter is incremented
				if (condcounter == c_d.length) {
					counter++;
				} else {
					for (int l = 0; l < c_d.length; l++) {
						//updating centroids
						Clusters.get(k).getCentroid()[l] = c_d[l];
					}
				}
			}
			//the program terminates if all centroids (Clusters.size()) are not eligible for further updates
			if (counter == Clusters.size()) {
				return Clusters;
			}

			for (int i = 0; i < Clusters.size(); i++) {
				Clusters.get(i).getPoints().clear();
			}
		}

	}

	public ArrayList<Integer> getPointsClusters() {
		return pointsclusters;
	}

}