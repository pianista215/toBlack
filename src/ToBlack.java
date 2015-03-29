import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Convert a colour image to an black/white pattern.
 * Based on the tutorial of : sentdex
 * https://www.youtube.com/watch?v=nych18rsXKU&index=5&list=WL
 * @author Pianista
 *
 */
public class ToBlack {
	
	/**
	 * Sufix for files generated
	 */
	private static final String SUFIX = "post_";
	
	/**
	 * Deep copy of a Buffered Image
	 * @param bi
	 * @return
	 */
	private BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	/**
	 * Convert the file provided in a black/white pattern
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private BufferedImage toBlack(File file) throws IOException{
		
		BufferedImage img = readImage(file);
		
		//Copy original
		BufferedImage copy = deepCopy(img);
		
		int[] averageArray = new int[img.getWidth()*img.getHeight()];
		
		//Obtain the average (thresold)
		//In the original tutorial is made by lambdas in python
		for(int i=0;i<img.getWidth();i++){
			for(int j=0;j<img.getHeight();j++){
				int rgb = img.getRGB(i, j);
				
				Color color = new Color(rgb);
				int avg = (color.getRed()+color.getGreen()+color.getBlue())/3;
				
				averageArray[j*img.getWidth() +i] = avg;
			}
		}
		
		//balance (average of the matrix of averages)
		int sum = 0;
		for(int i=0;i<averageArray.length;i++){
			sum += averageArray[i];
		}
		
		int balance = sum/averageArray.length;
		
		//Finally if the average is greater than balance -> white, else -> black
		for(int i=0;i<img.getWidth();i++){
			for(int j=0;j<img.getHeight();j++){
				if(averageArray[j*img.getWidth() +i]>balance){
					Color c = new Color(255,255,255,255);
					copy.setRGB(i, j, c.getRGB());
				} else{
					Color c = new Color(0,0,0,255);
					copy.setRGB(i, j, c.getRGB());
				}
			}
		}
		
		return copy;
		
	}
	
	/**
	 * Return as BufferedImage the file specified
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private BufferedImage readImage(File file) throws IOException{
		BufferedImage img = ImageIO.read(file);
		return img;
	}
	
	/**
	 * Change as needed
	 */
	private static final String FOLDER = "D:\\Investigacion\\ToBlack\\ToBlack\\images\\";

	/**
	 * Main entry
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		File t1 = new File(FOLDER+"bwhite.png");
		File t2 = new File(FOLDER+"yellow1.png");
		File t3 = new File(FOLDER+"yellow2.png");
		File t4 = new File(FOLDER+"logo.png");
		
		ToBlack test = new ToBlack();
		BufferedImage res1 = test.toBlack(t1);
		BufferedImage res2 = test.toBlack(t2);
		BufferedImage res3 = test.toBlack(t3);
		BufferedImage res4 = test.toBlack(t4);
		
		File o1 = new File(FOLDER+SUFIX+"bwhite.png");
		File o2 = new File(FOLDER+SUFIX+"yellow1.png");
		File o3 = new File(FOLDER+SUFIX+"yellow2.png");
		File o4 = new File(FOLDER+SUFIX+"logo.png");
		
		ImageIO.write(res1, "png", o1);
		ImageIO.write(res2, "png", o2);
		ImageIO.write(res3, "png", o3);
		ImageIO.write(res4, "png", o4);
		
	}

}
