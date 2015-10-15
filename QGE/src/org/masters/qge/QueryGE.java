package org.masters.qge;

import java.util.ArrayList;
import java.util.List;

import org.masters.qge.storage.Data;
import org.masters.qge.storage.DataStorage;
import org.masters.qge.tools.Tools;

public class QueryGE {

	public QueryGE() {

	}

	public Data generateRandomDatum() {
		float[] r = { (float) (Math.random() - 0.5f), (float) (Math.random() - 0.5f) };
		return new Data(r);
	}

	private float[] getAverageDatumFromQuery(Data query, float theta) {
		List<Data> ds = DataStorage.getInstance().getDataSet();
		List<Data> dataInTheta = new ArrayList<Data>();
		for (Data d : ds) {
			if (Tools.distance(query, d) < theta) {
				dataInTheta.add(d);
			}
		}
		return getAverage(dataInTheta);
	}

	private float[] getAverage(List<Data> dataInTheta) {
		float[] avg = new float[2];
		for (Data d : dataInTheta) {
			avg[0] += d.getRow()[0];
			avg[1] += d.getRow()[1];
		}
		avg[0] = avg[0] / dataInTheta.size();
		avg[1] = avg[1] / dataInTheta.size();
		return avg;
	}
}
