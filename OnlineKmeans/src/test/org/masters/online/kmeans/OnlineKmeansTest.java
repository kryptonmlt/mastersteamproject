package test.org.masters.online.kmeans;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.masters.online.kmeans.OnlineKmeans;
import org.masters.online.kmeans.utils.Tools;

public class OnlineKmeansTest {
	@Test
	public void testDistance() {
		Random r = new Random();
		float[] p1 = { r.nextFloat() * r.nextInt(20), r.nextFloat() * r.nextInt(3) };
		float[] p2 = { r.nextFloat() * r.nextInt(100), r.nextFloat() * r.nextInt(50) };
		Assert.assertEquals(Tools.distance(p1, p2), Tools.distance2D(p1, p2), 0);
		Assert.assertEquals(Tools.distance(p1, p2), Tools.distance2D(p2, p1), 0);
	}

	@Test
	public void testMoveCentroid() {
		OnlineKmeans online = new OnlineKmeans(1, 0.1f);
		float[] f = { 70.0f, 30.0f };
		float[] c = { 20.0f, 5.0f };
		online.update(c);
		float[] result = online.moveCentroid(f, 0);
		Assert.assertEquals(25.0f, result[0], 0);
		Assert.assertEquals(7.5f, result[1], 0);
	}

	@Test
	public void testClassify() {
		float[][] c = new float[3][2];
		float[] c1 = { 20.0f, 5.0f };
		float[] c2 = { 10.0f, 5.0f };
		float[] c3 = { 230.0f, 52.0f };
		float[] point = { 14.5f, 5.0f };
		c[0] = c1;
		c[2] = c2;
		c[3] = c3;
		Assert.assertEquals(1, Tools.classify(point, c));

	}

}
