package filters;

import imagelab.ImageFilter;
import imagelab.ImgProvider;

public class ConvolveCW implements ImageFilter{

	// Attribute to store the modified image
	ImgProvider filteredImage;
	private static final int[][] GAUSSIAN_BLUR = {{1,2,1}, {2,4,2}, {1,2,1}};
	private static final int[][] VANILLA_AVERAGE = {{1,1,1}, {1,1,1}, {1,1,1}};
	private static final int[][] FILTER1 = {{1,2,3,2,1}, {2,3,4,3,2}, {1,2,3,2,1}, {2,4,2,1,1}, {1,2,3,2,1}};
	public static final int[][] EMBOSS = {{-1, 0, -1}, {0,4,0}, {-1, 0, -1}};

	public void filter (ImgProvider ip) {
		int[][] filter = EMBOSS;
		
		// Grab the pixel information and put it into a 2D array
		short[][] red = ip.getRed();
		short[][] green = ip.getGreen();
		short[][] blue = ip.getBlue();
		short[][] transp = ip.getAlpha();

		// Make variables for image height and width
		int height = red.length;
		int width  = red[0].length;

		// Create a new array to store the modified image
		short[][] newRed = new short[height][width];
		short[][] newGreen = new short[height][width];
		short[][] newBlue = new short[height][width];
		short[][] newTransp = new short[height][width];

		

		short filterWeight = 0;

		for (int i = 0; i < filter.length; i++) 
			for (int j = 0; j < filter[i].length; j++)
				filterWeight += filter[i][j];

		//loop over all (row, col) in im[][]
		for (int row = 0; row < height-filter.length; row++) {
			for (int col = 0; col < width-filter[0].length; col++) {
				short averageRed = 0;
				short averageGreen = 0;
				short averageBlue = 0;
				short averageTransp = 0;

				//loop over a 3x3 "block"
				//get out the pixel value
				//get out the weight from the mask
				//multiply them and add to average
				for (int i = row; i < row + filter.length; i++) 
					for (int j = col; j < col + filter[i-row].length; j++) {
						averageRed += red[i][j] * filter[i-row][j-col];
						averageGreen += green[i][j] * filter[i-row][j-col];
						averageBlue += blue[i][j] * filter[i-row][j-col];
						averageTransp += transp[i][j] * filter[i-row][j-col];
					}

				//new pix val = divide by total weight of the mask
				//assign value to newImage[]
				if (filterWeight != 0) {
					averageRed /= filterWeight;
					averageGreen /= filterWeight;
					averageBlue /= filterWeight;
					averageTransp /= filterWeight;
				}
				
				newRed[row+filter.length/2][col+filter.length/2] = averageRed;
				newGreen[row+filter.length/2][col+filter.length/2] = averageGreen;
				newBlue[row+filter.length/2][col+filter.length/2] = averageBlue;
				newTransp[row+filter.length/2][col+filter.length/2] = averageTransp;
			}
		}

		// Create a new ImgProvider and set the filtered image to our new image
		filteredImage = new ImgProvider();
		filteredImage.setColors(newRed, newGreen, newBlue, newTransp);

		// Show the new image in a new window with title "Flipped Horizontally"
		filteredImage.showPix("Convolution");			
	}

	public ImgProvider getImgProvider() {
		return filteredImage;
	}

	// This is what users see in the Filter menu
	public String getMenuLabel() {
		return "Convolution (C)";
	}
	
}
