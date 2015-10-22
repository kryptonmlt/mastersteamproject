package org.masters.cluster.visualizer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;

public class Styling extends JFrame {
	public Styling() {
		// Create data
		DataTable data = new DataTable(Double.class, Double.class, Double.class);

		final int POINT_COUNT = 1000;
		java.util.Random rand = new java.util.Random();
		for (int i = 0; i < POINT_COUNT; i++) {
			double x = rand.nextGaussian();
			double y1 = rand.nextGaussian() + x;
			double y2 = rand.nextGaussian() - x;
			data.add(x, y1, y2);
		}

		// Create series
		DataSeries series1 = new DataSeries("Series 1", data, 0, 1);
		DataSeries series2 = new DataSeries("Series 2", data, 0, 2);
		XYPlot plot = new XYPlot(series1, series2);

		// Style the plot
		double insetsTop = 20.0, insetsLeft = 60.0, insetsBottom = 60.0, insetsRight = 40.0;
		plot.setInsets(new Insets2D.Double(insetsTop, insetsLeft, insetsBottom, insetsRight));
		plot.getTitle().setText("Nice scatter");

		// Style the plot area
		plot.getPlotArea().setBorderColor(new Color(0.0f, 0.3f, 1.0f));
		plot.getPlotArea().setBorderStroke(new BasicStroke(2f));

		// Style data series
		PointRenderer points1 = new DefaultPointRenderer2D();
		points1.setShape(new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0));
		points1.setColor(new Color(0.0f, 0.3f, 1.0f, 0.3f));
		plot.setPointRenderer(series1, points1);

		PointRenderer points2 = new DefaultPointRenderer2D();
		points2.setShape(new Rectangle2D.Double(-2.5, -2.5, 5, 5));
		points2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.3f));
		plot.setPointRenderer(series2, points2);

		// Style axes
		plot.getAxisRenderer(XYPlot.AXIS_X).setLabel("X");
		plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel("Y");
		plot.getAxisRenderer(XYPlot.AXIS_X).setTickSpacing(1.0);
		plot.getAxisRenderer(XYPlot.AXIS_Y).setTickSpacing(2.0);
		plot.getAxisRenderer(XYPlot.AXIS_X).setIntersection(-Double.MAX_VALUE);
		plot.getAxisRenderer(XYPlot.AXIS_Y).setIntersection(-Double.MAX_VALUE);

		// Display on screen
		getContentPane().add(new InteractivePanel(plot), BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(getContentPane().getMinimumSize());
		setSize(504, 327);
	}

	public static void main(String[] args) {
		Styling df = new Styling();
		df.setVisible(true);
	}
}
