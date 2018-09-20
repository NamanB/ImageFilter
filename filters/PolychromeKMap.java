package filters;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import imagelab.ImageFilter;
import imagelab.ImgProvider;

public class PolychromeKMap implements ImageFilter {

	private ImgProvider filteredImage;

	@Override
	public void filter(ImgProvider ip) {
		// get the pixel information
		short[][] red = ip.getRed();
		short[][] green = ip.getGreen();
		short[][] blue = ip.getBlue();
		short[][] transp = ip.getAlpha();

		int numCenters = Integer.parseInt(JOptionPane.showInputDialog("How many colors?"));

		Cluster[] clusters = new Cluster[numCenters];
		Point[][] points = new Point[red.length][red[0].length]; // create point array from pixels

		for (int row = 0; row < red.length; row++) {
			for (int col = 0; col < red[row].length; col++) {
				points[row][col] = new Point(red[row][col], green[row][col], blue[row][col], transp[row][col]);
			}
		}

		createClusters(clusters);

		do {
			for (int row = 0; row < red.length; row++)
				for (int col = 0; col < red[row].length; col++) {
					int clusterIndex = closestClusterIndex(clusters, points[row][col]);
					clusters[clusterIndex].addPoint(points[row][col]);
				}
			if (areValidClusters(clusters)) {
				recalculateCenters(clusters);
				clearClusters(clusters);
			} else {
				createClusters(clusters);
			}
		} while (!haveCentersMoved(clusters));

		short[][] newRed = new short[red.length][red[0].length];
		short[][] newGreen = new short[red.length][red[0].length];
		short[][] newBlue = new short[red.length][red[0].length];
		short[][] newTransp = new short[red.length][red[0].length];

		for (int row = 0; row < red.length; row++)
			for (int col = 0; col < red[row].length; col++) {
				int clusterIndex = closestClusterIndex(clusters, points[row][col]);

				newRed[row][col] = clusters[clusterIndex].getCenter().getRed();
				newGreen[row][col] = clusters[clusterIndex].getCenter().getGreen();
				newBlue[row][col] = clusters[clusterIndex].getCenter().getBlue();
				newTransp[row][col] = clusters[clusterIndex].getCenter().getTransp();
			}

		filteredImage = new ImgProvider();
		filteredImage.setColors(newRed, newGreen, newBlue, newTransp);

		filteredImage.showPix("Simplified");

	}

	/**
	 * Checks if clusters have points in them
	 * @param clusters an array of the clusters to check
	 * @return if the clusters have points in them
	 */
	private boolean areValidClusters(Cluster[] clusters) {
		for (Cluster cluster : clusters) {
			if (cluster.getSize() == 0)
				return false;
		}
		return true;
	}

	/**
	 * Creates empty with random centers
	 * @param clusters
	 */
	private void createClusters(Cluster[] clusters) {
		for (int i = 0; i < clusters.length; i++) {
			short randomRed = (short) (Math.random() * 256);
			short randomGreen = (short) (Math.random() * 256);
			short randomBlue = (short) (Math.random() * 256);
			short randomTransp = (short) (Math.random() * 256);
			clusters[i] = new Cluster(new Point(randomRed, randomGreen, randomBlue, randomTransp));
		}
	}

	private void clearClusters(Cluster[] clusters) {
		for (Cluster cluster : clusters)
			cluster.clear();
	}

	private void recalculateCenters(Cluster[] clusters) {
		for (Cluster cluster : clusters)
			cluster.calculateCenter();
	}

	private int closestClusterIndex(Cluster[] clusters, Point point) {
		double smallestDist = point.distanceTo(clusters[0].getCenter());
		int closestIndex = 0;

		for (int i = 1; i < clusters.length; i++) {
			double dist = point.distanceTo(clusters[i].getCenter());
			if (dist < smallestDist) {
				smallestDist = dist;
				closestIndex = i;
			}
		}
		return closestIndex;
	}

	private Cluster[][] getPixelInfo(ImgProvider ip) {
		short[][] red = ip.getRed();
		short[][] green = ip.getGreen();
		short[][] blue = ip.getBlue();
		short[][] transp = ip.getAlpha();

		Cluster[][] pixels = new Cluster[red.length][red[0].length];

		for (int row = 0; row < red.length; row++) {
			for (int col = 0; col < red[row].length; col++) {
				pixels[row][col] = new Cluster(
						new Point(red[row][col], green[row][col], blue[row][col], transp[row][col]));
			}
		}
		return pixels;
	}

	@Override
	public ImgProvider getImgProvider() {
		return filteredImage;
	}

	@Override
	public String getMenuLabel() {
		return "PolychromeK-Mapping";
	}

	public static boolean haveCentersMoved(Cluster[] clusters) {
		for (Cluster cluster : clusters)
			if (cluster.hasMoved())
				return true;

		return false;
	}

	public class Cluster {
		private ArrayList<Point> points;
		private Point center;
		private Point oldCenter;
		private int size;

		public Cluster(Point center) {
			this.center = center;
			this.points = new ArrayList<Point>();
			this.size = 0;
		}

		public boolean hasMoved() {
			return !(center == oldCenter);
		}

		public void addPoint(Point p) {
			points.add(p);
			size++;
		}

		public void clear() {
			points.clear();
			size = 0;
		}

		public Point getCenter() {
			return this.center;
		}

		public void calculateCenter() {
			short redSum = 0, blueSum = 0, greenSum = 0, transpSum = 0;
			for (Point p : this.points) {
				redSum += p.getRed();
				greenSum += p.getGreen();
				blueSum += p.getBlue();
				transpSum += p.getTransp();
			}

			short redAvg = (short) (redSum / this.size);
			short blueAvg = (short) (blueSum / this.size);
			short greenAvg = (short) (greenSum / this.size);
			short transpAvg = (short) (transpSum / this.size);

			this.oldCenter = this.center;
			this.center = new Point(redAvg, blueAvg, greenAvg, transpAvg);
		}

		public int getSize() {
			return this.points.size();
		}

	}

	public class Point {
		private short red, blue, green, transp;

		public Point(short red, short blue, short green, short transp) {
			this.red = red;
			this.blue = blue;
			this.green = green;
			this.transp = transp;
		}

		public short getRed() {
			return red;
		}

		public short getBlue() {
			return blue;
		}

		public short getGreen() {
			return green;
		}

		public short getTransp() {
			return transp;
		}

		public void setRed(short red) {
			this.red = red;
		}

		public void setBlue(short blue) {
			this.blue = blue;
		}

		public void setGreen(short green) {
			this.green = green;
		}

		public void setTransp(short transp) {
			this.transp = transp;
		}

		public double distanceTo(Point other) {
			double redDist = calculateDistance(this.red, other.getRed());
			double greenDist = calculateDistance(this.green, other.getGreen());
			double blueDist = calculateDistance(this.blue, other.getBlue());
			double transpDist = calculateDistance(this.transp, other.getTransp());

			return (Math.pow(redDist * redDist + greenDist * greenDist + blueDist * blueDist + transpDist * transpDist,
					.5));
		}

		public double calculateDistance(short pointA, short pointB) {
			return (Math.abs(pointA - pointB));
		}

	}

}
