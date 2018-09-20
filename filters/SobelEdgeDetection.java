package filters;

import java.util.Arrays;

import javax.swing.JOptionPane;

import imagelab.ImageFilter;
import imagelab.ImgProvider;

public class SobelEdgeDetection implements ImageFilter {

	// Attribute to store the modified image
	ImgProvider filteredImage;
	private static final int[][] VERTICAL_FILTER = {{-1,0,1},{-2,0,2},{-1,0,1}};
	private static final int[][] HORIZONTAL_FILTER = {{1,2,1},{0,0,0},{-1,-2,-1}};
	private static final int[][] CONVOLVE_X = {{0,0,0},{0,1,0},{1,1,1}};
	private static final int[][] CONVOLVE_Y = {{0,0,0},{1,1,0},{0,1,0}};
	private static boolean auto = false;

	public void filter (ImgProvider ip) {
		int[][] filterX = VERTICAL_FILTER;
		int[][] filterY = HORIZONTAL_FILTER;
		int threshold;
		
		if (auto = false)
			threshold = Integer.parseInt(JOptionPane.showInputDialog("What is the threshold?"));
		else
			threshold = 250; //replace with calculate threshold eventually
		
		// Grab the pixel information and put it into a 2D array
//		short[][] im = polychromeFilter(ip);
		short[][] im = ip.getBWImage();

		// Make variables for image height and width
		int height = im.length;
		int width  = im[0].length;

		// Create a new array to store the modified image
		short[][] newImage = new short[height][width];

		//loop over all (row, col) in im[][]
		for (int row = 0; row < height-filterX.length; row++) {
			for (int col = 0; col < width-filterX[0].length; col++) {
				short averageX = 0;
				short averageY = 0;

				//loop over a 3x3 "block"
				//get out the pixel value
				//get out the weight from the mask
				//multiply them and add to average
				for (int i = row; i < row + filterX.length; i++) 
					for (int j = col; j < col + filterX[i-row].length; j++) {
						averageX += im[i][j] * filterX[i-row][j-col];
						averageY += im[i][j] * filterY[i-row][j-col];
					}
				
				short average = (short) (Math.pow(averageX * averageX + averageY * averageY, .5));
//				new pix val = divide by total weight of the mask
//				assign value to newImage[]
				
				if (average > threshold) {
					newImage[row][col] = 255;
				}
				else {
					newImage[row][col] = 0;
				}
			}
		}
		
//		newImage = thinLines(newImage);
		
		// Create a new ImgProvider and set the filtered image to our new image
		filteredImage = new ImgProvider();
		filteredImage.setBWImage(newImage);

		// Show the new image in a new window with title "Flipped Horizontally"
		filteredImage.showPix("Edge Detection!");			
	}
	
	public static short[][] thinLines(short[][] img) {
		int[][] filterX = CONVOLVE_X;
		int[][] filterY = CONVOLVE_Y;
		
		for (int i = 0; i < 4; i++) {
			img = convolve(img, filterX);
			img = convolve(img, filterY);
//			img = rotate90(img);
			filterX = rotate90(filterX);
			filterY = rotate90(filterY);
		}
		return img;
	}

	public static int[][] rotate90(int[][] img) {
		// create a new array for new pixels
		int height = img.length;
		int width = img[0].length;
		
		int[][] newImg = new int[width][height];
		
		// copy pixel values into new array
		// "do something to them"
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				newImg[col][row] = img[row][col];
			}
		}
		return flipCols(newImg);
	}
	
	public static short[][] rotate90(short[][] img) {
		// create a new array for new pixels
		int height = img.length;
		int width = img[0].length;
		
		short[][] newImg = new short[width][height];
		
		// copy pixel values into new array
		// "do something to them"
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				newImg[col][row] = img[row][col];
			}
		}
		return flipCols(newImg);
	}
	
	public static int[][] flipCols(int[][] arr1) {
		int height= arr1.length;
		int width = arr1[0].length;
		int[][] flipped = new int[height][width];
		
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				flipped[row][col] = arr1[row][width-1-col];
			}
		}
		return flipped;
	}
	
	public static short[][] flipCols(short[][] arr1) {
		int height= arr1.length;
		int width = arr1[0].length;
		short[][] flipped = new short[height][width];
		
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				flipped[row][col] = arr1[row][width-1-col];
			}
		}
		return flipped;
	}
	
	public static short[][] convolve(short[][] im, int[][] filter) {
		// Grab the pixel information and put it into a 2D array
 
		// Make variables for image height and width
		int height = im.length;
		int width  = im[0].length;
 
		// Create a new array to store the modified image
		short[][] newImage = new short[height][width];
		
		short filterWeight = 0;
		
		for (int i = 0; i <  filter.length; i++) 
			for (int j = 0; j <  filter[i].length; j++)
				filterWeight += filter[i][j];
		
		//loop over all (row, col) in im[][]
		for (int row = 0; row <  height-filter.length; row++) {
			for (int col = 0; col <  width-filter[0].length; col++) {
				short average = 0;
				
				//loop over a 3x3 "block"
				//get out the pixel value
				//get out the weight from the mask
				//multiply them and add to average
				for (int i = row; i <  row + filter.length; i++) 
					for (int j = col; j <  col + filter[i-row].length; j++) 
						average += im[i][j] * filter[i-row][j-col];
				
				//new pix val = divide by total weight of the mask
				//assign value to newImage[]
				if (filterWeight != 0) {
					average /= filterWeight;
				}
				newImage[row+filter.length/2][col+filter.length/2] = average;
			}
		}
		return newImage;
	}
	
	public ImgProvider getImgProvider() {
		return filteredImage;
	}

	// This is what users see in the Filter menu
	public String getMenuLabel() {
		return "Sobel Edge Detection (BW)";
	}

}
