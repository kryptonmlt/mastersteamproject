package test.org.masters.qge.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.masters.qge.storage.Data;
import org.masters.qge.tools.Tools;

public class ToolsTest {

	@Test
	public void testDistance() {
		float[] p1 = { -0.5f, -0.5f };
		float[] p2 = { 0.5f, 0.5f };
		Assert.assertEquals(1.41f, Tools.distance(new Data(p1), new Data(p2)), 0.1);
	}

	@Test
	public void testGetAverageDatumFromQuery() {
		float[] p1 = { -0.5f, -0.5f };
		float[] p2 = { 0.5f, 0.5f };
		float[] p3 = { 0.3f, 0.3f };
		float[] query = { 0.4f, 0.4f };
		List<Data> dataSet = new ArrayList<Data>();
		dataSet.add(new Data(p1));
		dataSet.add(new Data(p2));
		dataSet.add(new Data(p3));
		Data avg = Tools.getAverageDatumFromQuery(dataSet, new Data(query), 0.15f);
		Assert.assertEquals(0.4f, avg.getRow()[0], 0);
		Assert.assertEquals(0.4f, avg.getRow()[1], 0);
	}

	@Test
	public void testGetAverageDatumFromQuery_NULL() {
		float[] p1 = { -0.5f, -0.5f };
		float[] p2 = { 0.5f, 0.5f };
		float[] p3 = { 0.3f, 0.3f };
		float[] query = { 0.1f, 0.1f };
		List<Data> dataSet = new ArrayList<Data>();
		dataSet.add(new Data(p1));
		dataSet.add(new Data(p2));
		dataSet.add(new Data(p3));
		Data avg = Tools.getAverageDatumFromQuery(dataSet, new Data(query), 0.1f);
		Assert.assertNull(avg);
	}

	@Test
	public void testGetAverage() {
		List<Data> dataInTheta = Tools.generateQuerys(3, 2, null);
		Data avg = Tools.getAverage(dataInTheta);
		Assert.assertEquals(
				(dataInTheta.get(0).getRow()[0] + dataInTheta.get(1).getRow()[0] + dataInTheta.get(2).getRow()[0])
						/ 3.0f,
				avg.getRow()[0], 0.001);
		Assert.assertEquals(
				(dataInTheta.get(0).getRow()[1] + dataInTheta.get(1).getRow()[1] + dataInTheta.get(2).getRow()[1])
						/ 3.0f,
				avg.getRow()[1], 0.001);
	}

	@Test
	public void testGenerateQuerys() {
		List<Data> data = Tools.generateQuerys(10, 2, null);
		int n = 5;
		for (int i = 0; i < data.size(); i++) {
			System.out.print(Arrays.toString(data.get(i).getRow()) + " ");
			n--;
			if (n == 0) {
				System.out.println();
				n = 5;
			}
		}
	}

}
