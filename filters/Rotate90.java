package filters;

import javax.swing.JOptionPane;

import imagelab.ImageFilter;
import imagelab.ImgProvider;

public class Rotate90 implements ImageFilter {
	private ImgProvider filteredImage;

	@Override
	public void filter(ImgProvider ip) {
		// get the pixel information
		short[][] red = ip.getRed();
		short[][] green = ip.getGreen();
		short[][] blue = ip.getBlue();
		short[][] transp = ip.getAlpha();
		
		// create a new array for new pixels
		int height = red.length;
		int width = red[0].length;
		
		short[][] newRed = new short[width][height];
		short[][] newGreen = new short[width][height];
		short[][] newBlue = new short[width][height];
		short[][] newTransp = new short[width][height];
		
		// copy pixel values into new array
		// "do something to them"
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				newRed[col][row] = red[row][col];
				newGreen[col][row] = green[row][col];
				newBlue[col][row] = blue[row][col];
				newTransp[col][row] = transp[row][col];
			}
		}
		
		height= newRed.length;
		width = newRed[0].length;
		
		short[][] finalRed = new short[height][width];
		short[][] finalGreen = new short[height][width];
		short[][] finalBlue = new short[height][width];
		short[][] finalTransp = new short[height][width];
		
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				finalRed[row][col] = newRed[row][width-1-col];
				finalGreen[row][col] = newGreen[row][width-1-col];
				finalBlue[row][col] = newBlue[row][width-1-col];
				finalTransp[row][col] = newTransp[row][width-1-col];
			}
		}
		//display
		
		filteredImage = new ImgProvider();
		filteredImage.setColors(finalRed, finalGreen, finalBlue, finalTransp);
		
		filteredImage.showPix("Rotated");

	}
	

	@Override
	public ImgProvider getImgProvider() {
		return filteredImage;
	}

	@Override
	public String getMenuLabel() {
		return "Rotate90";
	}
}
