package org.masters.qge;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.masters.qge.clustering.ART;
import org.masters.qge.clustering.Clustering;
import org.masters.qge.clustering.OnlineKmeans;
import org.masters.qge.utils.VectorFunctions;

public class PerdicationApplication {

	public static void main(String[] args) throws IOException {

		int k = 10;
		float alpha = 0.05f;
		float row = 0.1f;

		BufferedReader dataMapReader = new BufferedReader(new FileReader(args[0]));
		String temp = null;
		Clustering queryOnline = new OnlineKmeans(k, alpha);
		Clustering avgDataArt = new ART(row, alpha);

		ArrayList<float[]> queryCentroids = new ArrayList<float[]>();
		ArrayList<float[]> avgDataCentroids = new ArrayList<float[]>();
		// initialize the clustering
		while ((temp = dataMapReader.readLine()) != null) {
			String[] queryAvgData = temp.split(";");
			String[] queryCentroid = queryAvgData[0].split(",");
			String[] avgDataCentroid = queryAvgData[1].split(",");

			float[] query = convertToFloatArray(queryCentroid);
			float[] aD = convertToFloatArray(avgDataCentroid);

			queryCentroids.add(query);
			avgDataCentroids.add(aD);
		}
		dataMapReader.close();
		queryOnline.setCentroids(queryCentroids);
		avgDataArt.setCentroids(avgDataCentroids);

		// read set B and evaluate
		BufferedReader setBReader = new BufferedReader(new FileReader(args[1]));
		BufferedWriter resultWriter = new BufferedWriter(new FileWriter("results.txt"));
		temp = null;
		float error = 0;
		int count = 0;
		while ((temp = setBReader.readLine()) != null) {
			String[] queryAvgData = temp.split(";");
			float[] query = convertToFloatArray(queryAvgData[0].split(","));
			float[] actualXBar = convertToFloatArray(queryAvgData[1].split(","));

			// Integer queryClusterId = queryOnline.update(queryCentroid);
			Integer queryClusterId = VectorFunctions.classify(query, queryOnline.getCentroids());
			float[] predictedXBar = avgDataArt.getCentroids().get(queryClusterId);

			float e = VectorFunctions.distance(actualXBar, predictedXBar);
			error += e;
			count++;
			writeToBufferedWriter(resultWriter, query, actualXBar, predictedXBar, e);
		}
		setBReader.close();
		float meanError = error / (float) count;
		resultWriter.write("---------------------------------------------------------------\n");
		resultWriter.write("" + meanError + "\n");
		resultWriter.write("---------------------------------------------------------------\n");
		resultWriter.close();
	}

	private static void writeToBufferedWriter(BufferedWriter bw, float[] query, float[] actualAVG, float[] predictedAVG,
			float error) throws IOException {
		bw.write(Arrays.toString(query).replace("]", "").replace("[", "") + ";"
				+ Arrays.toString(actualAVG).replace("]", "").replace("[", "") + ";"
				+ Arrays.toString(predictedAVG).replace("]", "").replace("[", "") + ";" + error + "\n");
	}

	private static float[] convertToFloatArray(String[] s) {
		float[] f = new float[s.length];
		for (int i = 0; i < s.length; i++) {
			f[i] = Float.parseFloat(s[i]);
		}
		return f;
	}
}
