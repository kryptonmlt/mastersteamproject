package test.org.masters.qge.tools;

import java.util.ArrayList;
import java.util.List;

import javax.tools.Tool;

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
	public void testGenerateRandomDatum() {
		Data d1 = Tools.generateRandomDatum();
		Data d2 = Tools.generateRandomDatum();
		Assert.assertNotNull(d1);
		Assert.assertNotNull(d2);
		Assert.assertNotEquals(d1.getRow()[0], d2.getRow()[0]);
		Assert.assertNotEquals(d1.getRow()[1], d2.getRow()[1]);
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
	public void testGetAverage() {
		List<Data> dataInTheta = new ArrayList<Data>();
		dataInTheta.add(Tools.generateRandomDatum());
		dataInTheta.add(Tools.generateRandomDatum());
		dataInTheta.add(Tools.generateRandomDatum());
		Data avg = Tools.getAverage(dataInTheta);
		Assert.assertEquals(
				(dataInTheta.get(0).getRow()[0] + dataInTheta.get(1).getRow()[0] + dataInTheta.get(2).getRow()[0])
						/ 3.0f,
				avg.getRow()[0], 0);
		Assert.assertEquals(
				(dataInTheta.get(0).getRow()[1] + dataInTheta.get(1).getRow()[1] + dataInTheta.get(2).getRow()[1])
						/ 3.0f,
				avg.getRow()[1], 0);
	}

}
