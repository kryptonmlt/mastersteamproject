import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class OfflineKmeans {

	private ArrayList<double[]> data = new ArrayList<double[]>();
	private ArrayList<Cluster> Clusters = new ArrayList<Cluster>();
	private ArrayList<Integer> pointsclusters = new ArrayList<Integer>();

	// the constructor receives two parameters: the path of the .txt file and
	// the number of centroids

	public OfflineKmeans(String s, int k) {

		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(s));
			while ((line = br.readLine()) != null) {
				String[] result = line.split(",");
				double[] point = new double[2];
				point[0] = Double.parseDouble(result[0]);
				point[1] = Double.parseDouble(result[1]);
				data.add(point);
			}
			
			for (int j = 0; j < data.size(); j++) {
				pointsclusters.add(0);
			}
			
			ArrayList<Integer> random = new ArrayList<Integer>();
			for (int j = 0; j < k; j++) {
				random.add(j);
			}
			Collections.shuffle(random);

			for (int i = 0; i < k; i++) {
				Clusters.add(new Cluster());
			}

			for (int i = 0; i < k; i++) {
				Clusters.get(i).setCentroid(data.get(random.get(i)).clone());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Cluster> update() {

		while (true) {

			int counter = 0;

			for (int i = 0; i < data.size(); i++) {

				double max = 0;
				int maxIndex = -1;
				for (int j = 0; j < Clusters.size(); j++) {
					double temp = (Math.sqrt(Math.pow(data.get(i)[0]
							- Clusters.get(j).getCentroid()[0], 2)
							+ Math.pow(data.get(i)[1]
									- Clusters.get(j).getCentroid()[1], 2)));
					if (maxIndex == -1 || temp <= max) {
						max = temp;
						maxIndex = j;
					}
				}
				pointsclusters.set(i, maxIndex);
				Clusters.get(maxIndex).getPoints().add(data.get(i));
				System.out.println("The point " + data.get(i)[0] + ","
						+ data.get(i)[1] + " is added to the centroid number "
						+ maxIndex);
			}

			for (int k = 0; k < Clusters.size(); k++) {

				double sum0 = 0;
				double sum1 = 0;
				int div = Clusters.get(k).getPoints().size();

				for (int i = 0; i < Clusters.get(k).getPoints().size(); i++) {
					sum0 += Clusters.get(k).getPoints().get(i)[0];
					sum1 += Clusters.get(k).getPoints().get(i)[1];
				}

				double update0 = sum0 / div;
				double update1 = sum1 / div;

				double condition1 = Math.abs(Clusters.get(k).getCentroid()[0]
						- update0);
				double condition2 = Math.abs(Clusters.get(k).getCentroid()[1]
						- update1);

				if (condition1 < 0.001 && condition2 < 0.001) {
					counter++;
				} else {
					Clusters.get(k).getCentroid()[0] = update0;
					Clusters.get(k).getCentroid()[1] = update1;
				}
			}

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