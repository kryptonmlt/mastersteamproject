package org.masters.cluster.visualizer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;

public class Application {

	public static void main(String args[]) throws IOException {

		File dataFileInput = new File(args[0]);
		if (!dataFileInput.exists()) {
			throw new IllegalArgumentException("Data File does not exist");
		}

		File centroidsFileInput = new File(args[1]);
		if (!centroidsFileInput.exists()) {
			throw new IllegalArgumentException("Centroids File does not exist");
		}

		File pointsFileInput = new File(args[2]);
		if (!pointsFileInput.exists()) {
			throw new IllegalArgumentException("Points File does not exist");
		}

		BufferedReader br = new BufferedReader(new FileReader(centroidsFileInput));
		String temp = null;

		List<float[]> centroids = new ArrayList<float[]>();
		try {
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				centroids.add(row);
			}
		} finally {
			br.close();
		}

		// read file and populate Data Store
		br = new BufferedReader(new FileReader(dataFileInput));
		temp = null;
		List<ClusterData> data = new ArrayList<ClusterData>();
		try {
			while ((temp = br.readLine()) != null) {
				String[] d = temp.split(",");
				float[] row = new float[d.length];
				for (int i = 0; i < d.length; i++) {
					row[i] = Float.parseFloat(d[i]);
				}
				data.add(new ClusterData(row, 0));
			}
		} finally {
			br.close();

		}

		br = new BufferedReader(new FileReader(pointsFileInput));
		temp = null;
		try {
			int i = 0;
			while ((temp = br.readLine()) != null) {
				data.get(i).setClusterId(Integer.parseInt(temp));
				i++;
			}
		} finally {
			br.close();

		}
		plot2DData("KMeans Clusters", data, centroids);
	}

	public static void plot2DData(String title, List<ClusterData> avgData, List<float[]> clusters) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		JFrame frame = new JFrame(title);

		DataTable data = new DataTable(Float.class, Float.class);
		for (int i = 0; i < avgData.size(); i++) {
			data.add(avgData.get(i).getPoint()[0], avgData.get(i).getPoint()[1]);
		}

		DataTable centroids = new DataTable(Float.class, Float.class);
		for (float[] c : clusters) {
			System.out.println("c: " + c[0] + ", " + c[1]);
			centroids.add(c[0], c[1]);
		}

		// Create series
		DataSeries centroidSeries = new DataSeries("Centroids Series", centroids, 0, 1);
		DataSeries dataSeries = new DataSeries("Data Series", data, 0, 1);
		XYPlot plot = new XYPlot(centroidSeries, dataSeries);

		// Style the plot
		double insetsTop = 20.0, insetsLeft = 60.0, insetsBottom = 60.0, insetsRight = 40.0;
		plot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
		plot.getTitle().setText("Nice scatter");

		// Style the plot area
		plot.getPlotArea().setBorderColor(new Color(0.0f, 0.3f, 1.0f));
		plot.getPlotArea().setBorderStroke(new BasicStroke(2f));

		// Style data series
		PointRenderer points1 = new DefaultPointRenderer2D();
		points1.setShape(new Ellipse2D.Double(-100.0, -100.0, 200.0, 200.0));
		points1.setColor(new Color(0.0f, 0.3f, 1.0f, 0.3f));
		plot.setPointRenderer(centroidSeries, points1);

		PointRenderer points2 = new DefaultPointRenderer2D();
		points2.setShape(new Rectangle2D.Double(-2.5, -2.5, 5, 5));
		points2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
		plot.setPointRenderer(dataSeries, points2);

		// Style axes
		plot.getAxisRenderer(XYPlot.AXIS_X).setLabel("X");
		plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel("Y");
		plot.getAxisRenderer(XYPlot.AXIS_X).setTickSpacing(1.0);
		plot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(2.0);
		plot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
		plot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);

		// Display on screen
		frame.getContentPane().add(new InteractivePanel(plot), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(frame.getContentPane().getMinimumSize());
		frame.setBounds((int) ((width / 2)), (int) (height / 7), (int) (width / 2), (int) (height / 1.5));

		frame.setVisible(true);
	}

}
