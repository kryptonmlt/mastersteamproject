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

	public static Data generateRandomDatum() {
		float[] r = { (float) (Math.random() - 0.5f), (float) (Math.random() - 0.5f) };
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
		float[] avg = new float[2];
		for (Data d : dataInTheta) {
			avg[0] += d.getRow()[0];
			avg[1] += d.getRow()[1];
		}
		avg[0] = avg[0] / dataInTheta.size();
		avg[1] = avg[1] / dataInTheta.size();
		return new Data(avg);
	}
}
