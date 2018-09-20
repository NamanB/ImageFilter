package filters;

import javax.swing.JOptionPane;

import imagelab.ImageFilter;
import imagelab.ImgProvider;

public class SoftPixelate implements ImageFilter {
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
		
		short[][] newRed = new short[height][width];
		short[][] newGreen = new short[height][width];
		short[][] newBlue = new short[height][width];
		short[][] newTransp = new short[height][width];
		
		int radius;
		
		do {
			radius = Integer.parseInt(JOptionPane.showInputDialog("Enter an odd radius between 1 and 9"));
		} while (radius < 1 || radius > 9 || radius % 2 != 1);
		
		// copy pixel values into new array
		// "do something to them"
		for (int row = radius; row+radius < height; row += radius*2) {
			for(int col = radius; col+radius < width; col+= radius*2) {
				short aveRed = 0, aveGreen = 0, aveBlue = 0, aveTransp = 0;
				
				for (int tempRow = row-radius; tempRow < row+radius; tempRow++) {
					for (int tempCol = col-radius; tempCol < col+radius; tempCol++) {
						aveRed += red[tempRow][tempCol];
						aveGreen += green[tempRow][tempCol];
						aveBlue += blue[tempRow][tempCol];
						aveTransp += transp[tempRow][tempCol];
					}
				}
				
				aveRed /= (radius*2)*(radius*2);
				aveGreen /= (radius*2)*(radius*2);
				aveBlue /= (radius*2)*(radius*2);
				aveTransp /= (radius*2)*(radius*2);
				
				for (int tempRow = row-radius; tempRow < row+radius; tempRow++) {
					for (int tempCol = col-radius; tempCol < col+radius; tempCol++) {
						newRed[tempRow][tempCol] = aveRed;
						newGreen[tempRow][tempCol] = aveGreen;
						newBlue[tempRow][tempCol] = aveBlue;
						newTransp[tempRow][tempCol] = aveTransp;
					}
				}
			}
		}
		
		//display
		
		filteredImage = new ImgProvider();
		filteredImage.setColors(newRed, newGreen, newBlue, newTransp);
		
		filteredImage.showPix("SoftPixelate");

	}

	@Override
	public ImgProvider getImgProvider() {
		return filteredImage;
	}

	@Override
	public String getMenuLabel() {
		return "SoftPixelate";
	}

}
