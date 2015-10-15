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
}
