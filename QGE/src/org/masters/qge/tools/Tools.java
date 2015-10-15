package org.masters.qge.tools;

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
}
