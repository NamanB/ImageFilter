package filters;
 
import imagelab.ImageFilter;
import imagelab.ImgProvider;
 
public class BWConvolve implements ImageFilter {
 
	// Attribute to store the modified image
	ImgProvider filteredImage;
	private static final int[][] GAUSSIAN_BLUR = {{1,2,1}, {2,4,2}, {1,2,1}};
	private static final int[][] VANILLA_AVERAGE = {{1,1,1}, {1,1,1}, {1,1,1}};
	private static final int[][] FILTER1 = {{1,2,3,2,1}, {2,3,4,3,2}, {1,2,3,2,1}, {2,4,2,1,1}, {1,2,3,2,1}};
	public static final int[][] EMBOSS = {{-1, 0, -1}, {0,4,0}, {-1, 0, -1}};
 
	public void filter (ImgProvider ip) {
		int[][] filter = GAUSSIAN_BLUR;
		
		// Grab the pixel information and put it into a 2D array
		short[][] im = ip.getBWImage();
 
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
		
		// Create a new ImgProvider and set the filtered image to our new image
		filteredImage = new ImgProvider();
		filteredImage.setBWImage(newImage);
 
		// Show the new image in a new window with title "Flipped Horizontally"
		filteredImage.showPix("Convolution");			
	}
 
	public ImgProvider getImgProvider() {
		return filteredImage;
	}
 
	// This is what users see in the Filter menu
	public String getMenuLabel() {
		return "Convolution (BW)";
	}
 
}