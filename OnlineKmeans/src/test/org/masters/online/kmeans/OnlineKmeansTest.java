package test.org.masters.online.kmeans;

import java.util.ArrayList;
import java.util.List;
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
		List<float[]> c = new ArrayList<float[]>();
		float[] c1 = { 20.0f, 5.0f };
		float[] c2 = { 10.0f, 5.0f };
		float[] c3 = { 230.0f, 52.0f };
		c.add(c1);
		c.add(c2);
		c.add(c3);
		float[] point = { 14.5f, 5.0f };
		Assert.assertEquals(1, Tools.classify(point, c));

	}

}
