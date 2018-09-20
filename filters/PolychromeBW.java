package filters;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import imagelab.ImageFilter;
import imagelab.ImgProvider;

public class PolychromeBW implements ImageFilter {

	private ImgProvider filteredImage;

	@Override
	public void filter(ImgProvider ip) {
		// get the pixel information
		short[][] pixels = ip.getBWImage();

		// create a new array for new pixels
		int height = pixels.length;
		int width = pixels[0].length;
		short[][] newPixels = new short[height][width];
		
		int numSplitPoints = Integer.parseInt(JOptionPane.showInputDialog("How many extra tones?"));
		int[] splitPoints = new int[numSplitPoints+2];
		
		for (int i = 1; i < numSplitPoints+1; i++) {
			int splitPoint;
			
			do {
				splitPoint = Integer.parseInt(JOptionPane.showInputDialog("Enter a split point between 1 and 255"));
			} while (!(splitPoint >= 0 && splitPoint < 255));
			
			splitPoints[i] = splitPoint;
		}
		splitPoints[splitPoints.length-1] = 255;
		Arrays.sort(splitPoints);
		
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				short val = sortVal(splitPoints, pixels[row][col]);
				newPixels[row][col] = val;
			}
		}
		
		filteredImage = new ImgProvider();
		filteredImage.setBWImage(newPixels);

		filteredImage.showPix("Cool!");

	}

	private short sortVal(int[] splitPoints, short val) {
		for (int i = 1; i < splitPoints.length; i++) {
			if (val <= splitPoints[i] && val >= splitPoints[i-1])
				return (short) ((Math.abs(splitPoints[i] - val) > Math.abs(splitPoints[i-1] - val)) 
						? splitPoints[i-1] : splitPoints[i]);
		}
		return 0;
	}

	@Override
	public ImgProvider getImgProvider() {
		return filteredImage;
	}

	@Override
	public String getMenuLabel() {
		return "Polychrome (BW)";
	}

}
