import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;

public class Eightbit {

	public static void main(String[] args) throws IOException {
		BigInteger firstkey = new BigInteger("64093183429674317436216217603162789640760949127144357087603836463176215574790");
		SHA256G sha = new SHA256G();
		JpgToPng converter = new JpgToPng();
		
		
		if (args[1].equals("0")) {
			System.out.println("Encrypting...");
			converter.converterJpgToPNG(args[0]);
		}else if(args[1].equals("1")) {
			System.out.println("Decrypting...");
		}
		final int BSHIFT = 0xFF;
		try {
			BufferedImage image;
			int width;
			int height;

			File input = new File(args[0]);
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
			// Store pixels
			byte[] t = new byte[width * height * 3];
			String[] output = new String[width * height * 3];
			byte[] tout = new byte[width * height * 3];
			BufferedWriter writer = new BufferedWriter(
					new FileWriter("D:\\Documents\\eclipse-workspaces\\java\\sbrtNbits\\hex2.txt"));
			int index = 0;
			// fill the table t with RGB values;
			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {

					Color c = new Color(image.getRGB(j, i));

					// As byte is SIGNED in Java overflow will occur for values > 127
					byte r = (byte) c.getRed();
					byte g = (byte) c.getGreen();
					byte b = (byte) c.getBlue();

					t[index++] = r;
					t[index++] = g;
					t[index++] = b;
				}
			}
			
			
			
			
			for (int i = 0; i < t.length; i++) {
				String aux = String.format("%8s", Integer.toBinaryString(t[i] & BSHIFT)).replace(' ', '0');
				if(i < 4000) {
					writer.write(t[i]+"\n");
				}
				
				output[i] = XORbits(aux, firstkey.toString(2));
				
				
			}

			for (int i = 0; i < output.length; i++) {
				tout[i] = (byte) Integer.parseInt(output[i], 2);
			
			
			}
			
			
		/*
			
			for (int i = 0; i < tout.length; i++) {
				String aux = String.format("%8s", Integer.toBinaryString(tout[i] & BSHIFT)).replace(' ', '0');
				output[i] = XORbits(aux, key.toString(2));
				
				
			}

			for (int i = 0; i < output.length; i++) {
				t[i] = (byte) Integer.parseInt(output[i], 2);
				
			}
			*/
			
			

			BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			index = 0;
			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {

					// Need to deal with values < 0 so binary AND with 0xFF
					// Java 8 provides Byte.toUnsignedInt but I am from the old school ;-)
					int r = tout[index++] & BSHIFT ; 
					int g = tout[index++] & BSHIFT; 
					int b = tout[index++] & BSHIFT;

					Color newColor = new Color(r, g, b);
					newImage.setRGB(j, i, newColor.getRGB());

				}
			}
			
			  String OutnameImg = args[0].replace(".jpeg", "") + "_8bit_agoravai.jpeg";
			  System.out.println(OutnameImg);
			
			  File outputImage = new File(OutnameImg);
		      ImageIO.write(newImage, "png", outputImage);
		      if(args[1].equals("1")) {
					converter.converterPNGtoJpg(args[0]);
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	} // End of public static void main()

	private static boolean bitOf(char in) {
		return (in == '1');
	}

	private static char charOf(boolean in) {
		return (in) ? '1' : '0';
	}

	private static String XORbits(String pkg, String key) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < pkg.length(); i++) {
			sb.append(charOf(bitOf(pkg.charAt(i)) ^ bitOf(key.charAt(i))));

		}

		String result = sb.toString();
		return result;
	}

}
