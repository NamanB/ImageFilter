package filters;

import javax.swing.JOptionPane;

import imagelab.ImageFilter;
import imagelab.ImgProvider;

public class ColorNoise implements ImageFilter{
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
		
		// copy pixel values into new array
		// "do something to them"
		for (int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				newRed[row][col] = red[row][col];
				newGreen[row][col] = green[row][col];
				newBlue[row][col] = blue[row][col];
				newTransp[row][col] = transp[row][col];
			}
		}
		
		int noisePercent;
		
		do {
			noisePercent = Integer.parseInt(JOptionPane.showInputDialog("How much percent noise?"));
		} while (!(noisePercent >= 0 && noisePercent <= 100));
		
		int numPixels = (int) ((noisePercent/100.0) * (width * height));
		
		// create new image provider
		//set the image provider's pixels
		for (int pixelNum = 0; pixelNum < numPixels; pixelNum++) {
			int row = (int)(Math.random() * height);
			int col = (int)(Math.random() * width);
			short randRed = (short)(Math.random() * 255);
			short randGreen = (short)(Math.random() * 255);
			short randBlue = (short)(Math.random() * 255);
			newRed[row][col] = randRed;
			newGreen[row][col] = randGreen;
			newBlue[row][col] = randBlue;
		}
		
		//display
		
		filteredImage = new ImgProvider();
		filteredImage.setColors(newRed, newGreen, newBlue, newTransp);
		
		filteredImage.showPix("ColorNoise");

	}

	@Override
	public ImgProvider getImgProvider() {
		return filteredImage;
	}

	@Override
	public String getMenuLabel() {
		return "Noise (C)";
	}

}
