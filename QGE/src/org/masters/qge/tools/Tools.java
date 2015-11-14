package org.masters.qge.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.masters.qge.storage.Data;

public class Tools {

	private Tools() {

	}

	public static float distance(Data p1, Data p2) {

		float ydist = p1.getRow()[1] - p2.getRow()[1];
		float xdist = p1.getRow()[0] - p2.getRow()[0];
		float distance = (float) Math.sqrt((ydist * ydist) + (xdist * xdist));
		return distance;
	}

	public static Data getAverageDatumFromQuery(List<Data> dataSet, Data query, float theta) {
		List<Data> dataInTheta = new ArrayList<Data>();
		for (Data d : dataSet) {
			if (Tools.distance(query, d) < theta) {
				dataInTheta.add(d);
			}
		}
		return getAverage(dataInTheta);
	}

	public static Data getAverage(List<Data> dataInTheta) {
		if (dataInTheta.isEmpty()) {
			return null;
		}
		float[] avg = new float[dataInTheta.get(0).getRow().length];
		for (Data d : dataInTheta) {
			for (int i = 0; i < dataInTheta.get(0).getRow().length; i++) {
				avg[i] += d.getRow()[i];
			}
		}
		for (int i = 0; i < avg.length; i++) {
			avg[i] = (float) (Math.round((avg[i] / dataInTheta.size()) * 10000.0) / 10000.0);
		}
		return new Data(avg);
	}

	public static List<Data> generateQuerys(int queryLimit, int noOfAxis, List<float[]> lPoints) {
		List<Data> data = new ArrayList<Data>();
		Random r = new Random();
		if (lPoints.isEmpty()) {// generate totally random queries since no
								// distributions specified.f
			for (int i = 0; i < queryLimit; i++) {
				float[] row = new float[noOfAxis];
				for (int j = 0; j < noOfAxis; j++) {
					row[j] = r.nextFloat() - 0.5f;
				}
				data.add(new Data(row));
			}
		} else {
			int lQueries = queryLimit / lPoints.size();
			for (int i = 0; i < lPoints.size(); i++) {// query each box
				for (int j = 0; j < lQueries; j++) {
					data.add(getRandomPointInBox(lPoints.get(i), r));
				}
			}
		}
		return data;
	}

	private static Data getRandomPointInBox(float[] input, Random r) {
		float[] points = new float[input.length - 1];
		float width = input[input.length - 1];
		for (int i = 0; i < input.length - 1; i++) {
			points[i] = input[i];
		}

		float[] result = new float[points.length];
		for (int i = 0; i < result.length; i++) {
			float g = 0.0f;
			boolean found = false;
			while (!found) {
				g = (float) ((r.nextGaussian() * (width / 3)) + points[i]);
				if (g < (points[i] + width) && g > (points[i] - width)) {
					found = true;
				}
			}
			result[i] = g;
		}
		return new Data(result);
	}

	public static List<Data> generateEquallyDistributed2DQueryPoints(float maxX, float minX, float maxY, float minY,
			float theta, int queryLimit) {
		List<Data> data = new ArrayList<Data>();
		int[] queryL = getQueryLimitsXY(queryLimit);
		float intervalsX = (maxX - minX) / (queryL[0] - 1);
		float intervalsY = (maxY - minY) / (queryL[1] - 1);

		for (int i = 0; i < queryL[1]; i++) {
			for (int j = 0; j < queryL[0]; j++) {
				float[] point = { minX + (j * intervalsX), minY + (i * intervalsY) };
				data.add(new Data(point));
			}
		}
		return data;
	}

	public static int[] getQueryLimitsXY(int queryLimit) {
		float sqrt = (float) Math.sqrt(queryLimit);
		if (sqrt % 1 == 0) {// whole number
			int[] limit = { (int) sqrt, (int) sqrt };
			return limit;
		} else {
			int starting = (int) (sqrt - 5);
			if (starting < 3) {
				starting = 3;
			}
			return getLimit(queryLimit, starting, 0);
		}
	}

	public static int[] getLimit(int queryLimit, int starting, int depth) {
		int[] result = new int[2];
		if (depth == 10) {
			result[0] = (int) Math.sqrt(queryLimit);
			result[1] = (int) Math.sqrt(queryLimit);
			return result;
		}

		if (queryLimit % starting == 0) {
			result[0] = starting;
			result[1] = queryLimit / starting;
			return result;
		} else {
			return getLimit(queryLimit, starting + 1, depth++);
		}
	}
}
