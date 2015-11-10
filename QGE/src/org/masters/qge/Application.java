package org.masters.qge;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import org.masters.qge.storage.Data;
import org.masters.qge.storage.DataStorage;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;

public class Application {

	public static void main(String args[]) throws IOException {

		if (args.length < 3) {
			throw new IllegalArgumentException(
					"2 arguments required: 1) path to input data file. 2) theta value in float 3) integer representing query limit 4) true if graph plotting is required");
		}

		File input = new File(args[0]);
		if (!input.exists()) {
			throw new IllegalArgumentException("File does not exist");
		}
		// read file and populate Data Store
		BufferedReader br = new BufferedReader(new FileReader(input));
		String temp = null;

		boolean completed = false;
		try {
			boolean first = true;
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				if (first) {
					DataStorage.getInstance().setColumns(d);
					first = false;
				}
				if (d.length != DataStorage.getInstance().getColumns().length) {
					throw new IllegalArgumentException("Number of columns doesnt match data in input file..." + temp);
				}
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				DataStorage.getInstance().getDataSet().add(new Data(row));
			}
			completed = true;
		} finally {
			br.close();

		}
		if (completed) { // no errors in populating data
			float theta = 0.0f;
			int queryLimit = 0;
			int noOfAxis = DataStorage.getInstance().getDataSet().get(0).getRow().length;
			try {
				theta = Float.parseFloat(args[1]);
				queryLimit = Integer.parseInt(args[2]);
			} catch (Exception e) {
				System.out.println("Theta (2nd parameter) must be in float format example 0.1");
			}
			QueryGE qGE = new QueryGE(theta, queryLimit, noOfAxis);
			qGE.generateQueries();
			qGE.saveQueries();
			List<Data> avgData = qGE.generateAVGPoints(qGE.getQueries());
			if (args.length > 3 && "true".equals(args[3])) {
				plot2DData("DATA.txt", DataStorage.getInstance().getDataSet(), 0);
				plot2DData("AVGDATA.txt", avgData, 1);
				System.out.println("Finished plotting..");
			}
		}
	}

	public static void plot2DData(String title, List<Data> avgData, int num) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds((int) (num * (width / 2)), (int) (height / 7), (int) (width / 2), (int) (height / 1.5));

		DataTable data = new DataTable(Float.class, Float.class);
		for (int i = 0; i < avgData.size(); i++) {
			data.add(avgData.get(i).getRow()[0], avgData.get(i).getRow()[1]);
		}

		XYPlot plot = new XYPlot(data);
		frame.getContentPane().add(new InteractivePanel(plot));
		Color color = new Color(0.0f, 0.3f, 1.0f);
		plot.getPointRenderer(data).setColor(color);
		frame.setVisible(true);
	}
}
