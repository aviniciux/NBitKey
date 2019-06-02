import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class JpgToPng {

	public void converterJpgToPNG(String image) {
	
		 try {
			 
		      File file = new File(image);
		      BufferedImage bi = ImageIO.read(file);
		 
		      ImageIO.write(bi, "png", new File(image));
		     
		 
		      System.out.println("Images were written succesfully.");
		 } catch (IOException e) {
		      System.out.println("Exception occured :" + e.getMessage());
		    }
	}
	
	public void converterPNGtoJpg(String image) {
		
		 try {
			 
		      File file = new File(image);
		      BufferedImage bi = ImageIO.read(file);
		 
		      ImageIO.write(bi, "jpg", new File(image));
		     
		 
		      System.out.println("Images were written succesfully.");
		 } catch (IOException e) {
		      System.out.println("Exception occured :" + e.getMessage());
		    }
	}

}
