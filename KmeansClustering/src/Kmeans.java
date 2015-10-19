import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Kmeans {

	private LinkedList<double[]> data = new LinkedList<double[]>();
	private LinkedList<double[]> centroids = new LinkedList<double[]>();

	//the constructor receives two parameters: the path of the .txt file and the number of centroids
	
	public Kmeans(String s, int k) {

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

			ArrayList<Integer> random = new ArrayList<Integer>();
			for (int j = 0; j < k; j++) {
				random.add(j);
			}
			Collections.shuffle(random);

			for (int i = 0; i < k; i++) {
				centroids.add(data.get(random.get(i)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<double[]> update() {

		while (true) {

			ArrayList<LinkedList<double[]>> b = new ArrayList<LinkedList<double[]>>();
			int counter = 0;

			for (int x = 0; x < centroids.size(); x++) {
				b.add(new LinkedList());
			}

			for (int i = 0; i < data.size(); i++) {
				double max = 0;
				int maxIndex = -1;
				for (int j = 0; j < centroids.size(); j++) {
					double temp = (Math
							.sqrt(Math.pow(
									data.get(i)[0] - centroids.get(j)[0], 2)
									+ Math.pow(
											data.get(i)[1]
													- centroids.get(j)[1], 2)));
					if (maxIndex == -1 || temp <= max) {
						max = temp;
						maxIndex = j;
					}
				}
				b.get(maxIndex).add(data.get(i));
				System.out
						.println("The point " + data.get(i)[0] + ","
								+ data.get(i)[1] + " is added to the centroid number "
								+ maxIndex);
			}

			for (int k = 0; k < centroids.size(); k++) {

				double sum0 = 0;
				double sum1 = 0;
				int div = b.get(k).size();

				for (int i = 0; i < b.get(k).size(); i++) {
					sum0 += b.get(k).get(i)[0];
					sum1 += b.get(k).get(i)[1];
				}

				double update0 = sum0 / div;
				double update1 = sum1 / div;

				double condition1 = Math.abs(centroids.get(k)[0] - update0);
				double condition2 = Math.abs(centroids.get(k)[1] - update1);

				if (condition1 < 0.001 && condition2 < 0.001) {
					counter++;
				} else {
					centroids.get(k)[0] = update0;
					centroids.get(k)[1] = update1;
				}
			}

			if (counter == centroids.size()) {
				return centroids;

			}
		}

	}
}