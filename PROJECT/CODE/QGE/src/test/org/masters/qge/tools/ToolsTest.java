package test.org.masters.qge.tools;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.masters.qge.utils.Tools;
import org.masters.qge.utils.VectorFunctions;

public class ToolsTest {

	@Test
	public void testDistance() {

		float[] p1 = { -0.5f, -0.5f };
		float[] p2 = { 0.5f, 0.5f };
		Assert.assertEquals(1.41f, VectorFunctions.distance(p1, p2), 0.1);
	}

	@Test
	public void testGetAverageDatumFromQuery() {
		Tools tools = Tools.getInstance();

		float[] p1 = { -0.5f, -0.5f };
		float[] p2 = { 0.5f, 0.5f };
		float[] p3 = { 0.3f, 0.3f };
		float[] query = { 0.4f, 0.4f };
		List<float[]> dataSet = new ArrayList<float[]>();
		dataSet.add(p1);
		dataSet.add(p2);
		dataSet.add(p3);
		float[] avg = tools.getAverageDatumFromQuery(dataSet, query, 0.15f);
		Assert.assertEquals(0.4f, avg[0], 0);
		Assert.assertEquals(0.4f, avg[1], 0);
	}

	@Test
	public void testGetAverageDatumFromQuery_NULL() {
		Tools tools = Tools.getInstance();

		float[] p1 = { -0.5f, -0.5f };
		float[] p2 = { 0.5f, 0.5f };
		float[] p3 = { 0.3f, 0.3f };
		float[] query = { 0.1f, 0.1f };
		List<float[]> dataSet = new ArrayList<float[]>();
		dataSet.add(p1);
		dataSet.add(p2);
		dataSet.add(p3);
		float[] avg = tools.getAverageDatumFromQuery(dataSet, query, 0.1f);
		Assert.assertNull(avg);
	}

}
