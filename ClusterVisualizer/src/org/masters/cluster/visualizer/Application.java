package org.masters.cluster.visualizer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;

public class Application {

	private static Color[] colors = new Color[256];

	public static void main(String args[]) throws IOException {

		Random rand = new Random();
		for (int i = 0; i < colors.length; i++) {
			colors[i] = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		}

		File dataFileInput = new File(args[0]);
		if (!dataFileInput.exists()) {
			throw new IllegalArgumentException("Data File does not exist");
		}

		File pointsFileInput = new File(args[1]);
		if (!pointsFileInput.exists()) {
			throw new IllegalArgumentException("Points File does not exist");
		}
		int x = 0;
		int y = 1;
		if (args.length > 3) {
			x = Integer.parseInt(args[2]);
			y = Integer.parseInt(args[3]);
		}

		// read file and populate Data Store
		BufferedReader br = new BufferedReader(new FileReader(dataFileInput));
		String temp = null;
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
		plot2DData("KMeans Clusters", data, x, y);
	}

	public static void plot2DData(String title, List<ClusterData> avgData, int x, int y) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		JFrame frame = new JFrame(title);
		HashMap<Integer, List<float[]>> coloredData = new HashMap<Integer, List<float[]>>();
		for (ClusterData aD : avgData) {
			if (coloredData.get(aD.getClusterId()) == null) {
				coloredData.put(aD.getClusterId(), new ArrayList<float[]>());
			}
			coloredData.get(aD.getClusterId()).add(aD.getPoint());
		}
		DataSource[] dataSeries = new DataSource[coloredData.keySet().size()];
		int z = 0;
		for (Integer id : coloredData.keySet()) {
			DataTable data = new DataTable(Float.class, Float.class);
			for (int i = 0; i < coloredData.get(id).size(); i++) {
				data.add(coloredData.get(id).get(i)[x], coloredData.get(id).get(i)[y]);
			}
			dataSeries[z] = new DataSeries("Data Series", data, 0, 1);
			z++;
		}

		XYPlot plot = new XYPlot(dataSeries);

		// Style the plot
		double insetsTop = 20.0, insetsLeft = 60.0, insetsBottom = 60.0, insetsRight = 40.0;
		plot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
		plot.getTitle().setText("Clustering");

		// Style the plot area
		plot.getPlotArea().setBorderColor(new Color(0.0f, 0.3f, 1.0f));
		plot.getPlotArea().setBorderStroke(new BasicStroke(2f));

		for (int i = 0; i < dataSeries.length; i++) {
			PointRenderer points = new DefaultPointRenderer2D();
			points.setShape(new Rectangle2D.Double(-2.5, -2.5, 5, 5));
			points.setColor(colors[i % colors.length]);
			plot.setPointRenderer(dataSeries[i], points);
		}

		// Display on screen
		frame.getContentPane().add(new InteractivePanel(plot), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(frame.getContentPane().getMinimumSize());
		frame.setBounds((int) ((width / 2)), (int) (height / 7), (int) (width / 2), (int) (height / 1.5));

		frame.setVisible(true);
	}

}
