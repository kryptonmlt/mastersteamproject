package org.masters.qge.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.masters.qge.storage.Data;

public class Tools {

	private static Tools instance = null;
	private Random r = null;

	private Tools() {
		r = new Random();
	}

	public static Tools getInstance() {
		if (instance == null) {
			instance = new Tools();
		}
		return instance;
	}

	public float distance(Data p1, Data p2) {
		float ydist = p1.getRow()[1] - p2.getRow()[1];
		float xdist = p1.getRow()[0] - p2.getRow()[0];
		float distance = (float) Math.sqrt((ydist * ydist) + (xdist * xdist));
		return distance;
	}

	public Data getAverageDatumFromQuery(List<Data> dataSet, Data query, float theta) {
		List<Data> dataInTheta = new ArrayList<Data>();
		for (Data d : dataSet) {
			if (this.distance(query, d) < theta) {
				dataInTheta.add(d);
			}
		}
		return getAverage(dataInTheta);
	}

	public Data getAverage(List<Data> dataInTheta) {
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

	public List<Data> generateQuerys(int queryLimit, int noOfAxis, List<float[]> lPoints) {
		List<Data> data = new ArrayList<Data>();
		if (lPoints == null || lPoints.isEmpty()) {
			// generate totally random queries since no distributions specified
			for (int i = 0; i < queryLimit; i++) {
				float[] row = new float[noOfAxis];
				for (int j = 0; j < noOfAxis; j++) {
					row[j] = r.nextFloat() - 0.5f;
				}
				data.add(new Data(row));
			}
		} else {
			for (int i = 0; i < queryLimit; i++) {
				data.add(this.generateQuery(lPoints));
			}
		}
		return data;
	}

	private Data generateQuery(List<float[]> lPoints) {
		int J = r.nextInt(lPoints.size()); // select random subspace
		return this.getRandomPointInBox(lPoints.get(J));
	}

	private Data getRandomPointInBox(float[] input) {
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
}
