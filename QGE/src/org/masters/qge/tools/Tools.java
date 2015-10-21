package org.masters.qge.tools;

import java.util.ArrayList;
import java.util.List;

import org.masters.qge.storage.Data;

public class Tools {

	public static float MAX = 0.5f;

	public static float MIN = -0.5f;

	private Tools() {

	}

	public static float distance(Data p1, Data p2) {

		float ydist = p1.getRow()[1] - p2.getRow()[1];
		float xdist = p1.getRow()[0] - p2.getRow()[0];
		float distance = (float) Math.sqrt((ydist * ydist) + (xdist * xdist));
		return distance;
	}

	public static Data generateRandomDatum(int cols) {
		float[] r = new float[cols];
		for (int i = 0; i < r.length; i++) {
			r[i] = (float) (Math.random() - 0.5f);
		}
		return new Data(r);
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

	public static List<Data> generateQuerys(Data max, Data min, float theta, int queryLimit) {
		List<Data> data = null;
		switch (max.getRow().length) {
		case 2:
			data = generateQueryPoints(max.getRow()[0], min.getRow()[0], max.getRow()[1], min.getRow()[1], theta,
					queryLimit);
			break;
		case 3:
			System.out.println("Not Supported yet...");
			break;
		}
		return data;
	}

	public static List<Data> generateQueryPoints(float maxX, float minX, float maxY, float minY, float theta,
			int queryLimit) {
		List<Data> data = new ArrayList<Data>();
		int[] queryL = getQueryLimitsXY(queryLimit);
		float intervalsX = (maxX - minX) / (queryL[0] - 1);
		float intervalsY = (maxY - minY) / (queryL[1] - 1);
		/*
		 * int intervalsX = (int) ((maxX - minX) / theta) + 1; int intervalsY =
		 * (int) ((maxY - minY) / theta) + 1; for (int i = 0; i < intervalsY;
		 * i++) { for (int j = 0; j < intervalsX; j++) { float[] point = { minX
		 * + (j * theta), minY + (i * theta) }; data.add(new Data(point)); } }
		 */
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
