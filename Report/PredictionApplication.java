package org.masters.qge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.masters.qge.utils.Tools;
import org.masters.qge.utils.VectorFunctions;

public class PredictionApplication {

	/**
	 * Using query and average data centroids this program predicts the output
	 * of a dataset. Then calculates the error.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			throw new Exception("2 arguments needed: QueryDataMap file, Test dataset");
		}

		BufferedReader dataMapReader = new BufferedReader(new FileReader(args[0]));
		String temp = null;

		ArrayList<float[]> queryCentroids = new ArrayList<float[]>();
		ArrayList<float[]> avgDataCentroids = new ArrayList<float[]>();

		System.out.println("Reading querymap file");
		// read the query and average data centroids
		while ((temp = dataMapReader.readLine()) != null) {
			String[] queryAvgData = temp.split(";");
			String[] queryCentroid = queryAvgData[0].split(",");
			String[] avgDataCentroid = queryAvgData[1].split(",");

			float[] query = Tools.getInstance().convertToFloatArray(queryCentroid);
			float[] aD = Tools.getInstance().convertToFloatArray(avgDataCentroid);

			queryCentroids.add(query);
			avgDataCentroids.add(aD);
		}
		dataMapReader.close();

		System.out.println("Finished reading querymap file");

		// read set B and evaluate
		BufferedReader setBReader = new BufferedReader(new FileReader(args[1]));
		BufferedWriter resultWriter = new BufferedWriter(new FileWriter("results.txt"));

		temp = null;
		float error = 0;
		int count = 0;

		System.out.println("Starting Error checking");
		while ((temp = setBReader.readLine()) != null) {
			// read query and actual output
			String[] queryAvgData = temp.split(";");
			float[] query = Tools.getInstance().convertToFloatArray(queryAvgData[0].split(","));
			float[] actualXBar = Tools.getInstance().convertToFloatArray(queryAvgData[1].split(","));

			// predict the output
			Integer queryClusterId = VectorFunctions.classify(query, queryCentroids);
			float[] predictedXBar = avgDataCentroids.get(queryClusterId);

			// calculate the error
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

		System.out.println("Finished Error checking");
		System.out.println("Mean error: " + meanError);
	}

	/**
	 * Write float[] to writers in correct format.
	 * 
	 * @param bw
	 * @param query
	 * @param actualAVG
	 * @param predictedAVG
	 * @param error
	 * @throws IOException
	 */
	private static void writeToBufferedWriter(BufferedWriter bw, float[] query, float[] actualAVG, float[] predictedAVG,
			float error) throws IOException {
		bw.write(Arrays.toString(query).replace("]", "").replace("[", "") + ";"
				+ Arrays.toString(actualAVG).replace("]", "").replace("[", "") + ";"
				+ Arrays.toString(predictedAVG).replace("]", "").replace("[", "") + ";" + error + "\n");
	}
}
