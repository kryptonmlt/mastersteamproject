package org.masters.qge.querygeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

import org.masters.qge.utils.Tools;

public class QueryGE {

	public QueryGE() {
	}

	/**
	 * Generates the training set depending on the parameters given. The
	 * training set is of format: query, data
	 * 
	 * @param queryLimit
	 *            number of queries to generate and run
	 * @param distributions
	 *            query points around which queries are generated. should be in
	 *            format x,y,..,width
	 * @param noOfAxis
	 *            number of axis used in the dataset
	 * @param theta
	 *            size of search space around each query
	 * @param dataSet
	 *            the data set itself should be of format x,y,... each with the
	 *            same number of axis
	 */
	public void generateTrainingSet(int queryLimit, List<float[]> distributions, int noOfAxis, float theta,
			List<float[]> dataSet) {

		try {
			BufferedWriter avgDataWriter = new BufferedWriter(
					new FileWriter("AVGDATA_" + theta + "_" + queryLimit + ".txt"));
			BufferedWriter trainingSetWriter = new BufferedWriter(
					new FileWriter("trainingSet_" + theta + "_" + queryLimit + ".txt"));

			System.out.println("Generating Training Set..");
			for (int i = 0; i < queryLimit; i++) {
				// print status update every 10%
				printCompletion(i, queryLimit);

				// generate query
				float[] query = Tools.getInstance().generateQuery(distributions, noOfAxis);

				// run query to get avg data
				float[] dataCentroid = Tools.getInstance().getAverageDatumFromQuery(dataSet, query, theta);
				if (dataCentroid != null) {
					// write AVGDATA
					avgDataWriter.write(Arrays.toString(dataCentroid).replace("]", "").replace("[", "") + "\n");
					avgDataWriter.flush();
					// write training set = query, avgdata
					Tools.getInstance().writeQueryAndADToWriter(trainingSetWriter, query, dataCentroid);
				}
			}
			// close writers
			avgDataWriter.close();
			trainingSetWriter.close();
			System.out.println("Training Set Generated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints a statement at 10% intervals
	 * 
	 * @param c
	 * @param querySize
	 */
	private void printCompletion(int count, float querySize) {
		if (((count / querySize) * 100) % 10 == 0) {
			System.out.println("training set completion: " + ((count / querySize) * 100f) + "%");
		}
	}
}
