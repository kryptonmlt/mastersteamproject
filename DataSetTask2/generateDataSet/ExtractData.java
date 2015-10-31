package generateDataSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtractData {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
		String temp = br.readLine();
		List<ArrayList<Float>> data = new ArrayList<ArrayList<Float>>();
		int dataPoints = Integer.parseInt(args[1]);
		for (int i = 0; i < dataPoints; i++) {
			data.add(new ArrayList<Float>());
		}
		System.out.println("Reading data file..");
		while ((temp = br.readLine()) != null) {
			temp = temp.replaceAll("\\s+", " ");
			String[] input = temp.split(" ");
			for (int i = 0; i < dataPoints; i++) {
				data.get(i).add(Float.parseFloat(input[i]));
			}
		}
		br.close();
		System.out.println("Calculating max and min..");
		float[][] columnProperties = new float[data.size()][2];
		for (int i = 0; i < data.size(); i++) {
			columnProperties[i][0] = Collections.min(data.get(i));
			columnProperties[i][1] = Collections.max(data.get(i));
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("n" + args[0])));

		System.out.println("Calculating and writing normalized data..");
		for (int i = 0; i < data.get(0).size(); i++) {
			for (int j = 0; j < data.size(); j++) {
				float n = (data.get(j).get(i) - columnProperties[j][0])
						/ (columnProperties[j][1] - columnProperties[j][0]);
				data.get(j).set(i, n - 0.5f);
				if (j < data.size() - 1) {
					bw.write(data.get(j).get(i) + ",");
				} else {
					bw.write("" + data.get(j).get(i));
				}
			}
			bw.write("\n");
		}
		bw.flush();
		bw.close();
		System.out.println("Finished successfully..");
	}
}
