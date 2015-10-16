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
			avg[i] = avg[i] / dataInTheta.size();
		}
		return new Data(avg);
	}

	public static List<Data> generateQuerys(Data max, Data min, float theta) {
		List<Data> data = null;
		switch (max.getRow().length) {
		case 2:
			data = generateQueryPoints(max.getRow()[0], min.getRow()[0], max.getRow()[1], min.getRow()[1], theta);
			break;
		case 3:
			System.out.println("Not Supported yet...");
			break;
		}
		return data;
	}

	public static List<Data> generateQueryPoints(float maxX, float minX, float maxY, float minY, float theta) {
		List<Data> data = new ArrayList<Data>();
		int intervalsX = (int) ((maxX - minX) / theta) + 1;
		int intervalsY = (int) ((maxY - minY) / theta) + 1;
		for (int i = 0; i < intervalsY; i++) {
			for (int j = 0; j < intervalsX; j++) {
				float[] point = { minX + (j * theta), minY + (i * theta) };
				data.add(new Data(point));
			}
		}
		return data;
	}
}
