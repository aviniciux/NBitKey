import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class TwoFiveSixBits {

	public static void main(String[] args) throws IOException {
		//First key generated by curve
		BigInteger firstkey = new BigInteger(
				"64093183429674317436216217603162789640760949127144357087603836463176215574790");
		SHA256G sha = new SHA256G();
		JpgToPng converter = new JpgToPng();
		
		//String OutnameImg = args[0].replace(".jpeg", "") + "_256bit.jpeg";
		String OutnameImg = args[0].replace(".jpg", "") + "_256bit.jpg";
		if (args[1].equals("0")) {
			System.out.println("Encrypting...");
			converter.converterJpgToPNG(args[0]);
		}else if(args[1].equals("1")) {
			System.out.println("Decrypting...");
			OutnameImg = OutnameImg.replace(".jpeg", "") + "_decrypted.jpeg";
		}
		final int BSHIFT = 0xFF;
		try {
			// Reading the image and get its attributes
			BufferedImage image;
			int width;
			int height;
			int sizebuffer;
			File input = new File(args[0]);
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
			// Store pixels
			byte[] t = new byte[width * height * 3];
		
			if ((width * height * 3)%32 == 0) {
				sizebuffer = (width * height * 3)/32;
			}else {
				sizebuffer = (width * height * 3)/32 + 1;
			}
			
			String[] output = new String[sizebuffer];
			byte[] tmp = new byte[32];
			byte[] tout = new byte[width * height * 3];
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
			int count = 0;
			int temp = 0;
			int k = 0;
			int chunk = 32; // chunk size to divide
			String aux = "";
			
			// Criptografia e Decriptografia
			for (int i = 0; i < t.length; i += chunk) {
				tmp = Arrays.copyOfRange(t, i, Math.min(t.length, i + chunk));

				for (int j = 0; j < tmp.length; j++) {
					aux = aux + String.format("%8s", Integer.toBinaryString(tmp[j] & BSHIFT)).replace(' ', '0');

				}
				
		
				output[k] = XORbits(aux, firstkey.toString(2));
				aux = "";
				k++;
				temp++;
				tmp = null;

			}
			
			//System.out.println(output[0]);
			
			//Parsing to Bytes
			int h = 0;
			for (int i = 0; i < output.length; i++) {
				for (int j = 0; j < output[i].length(); j += 8) {

					tout[h] = (byte) Integer.parseInt(output[i].substring(j, j + 8), 2);
					h++;
				}
			}
			 
			//Create the new image
			BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			index = 0;
			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {

					// Need to deal with values < 0 so binary AND with 0xFF // Java 8 provides
					// Byte.toUnsignedInt but I am from the old school ;-)
					int r = tout[index++] & BSHIFT;
					int g = tout[index++] & BSHIFT;
					int b = tout[index++] & BSHIFT;

					Color newColor = new Color(r, g, b);
					newImage.setRGB(j, i, newColor.getRGB());
				}
			}
			
			File outputImage = new File(OutnameImg);
			ImageIO.write(newImage, "png", outputImage);
			if(args[1].equals("1")) {
				converter.converterPNGtoJpg(args[0]);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static boolean bitOf(char in) {
		return (in == '1');
	}

	private static char charOf(boolean in) {
		return (in) ? '1' : '0';
	}

	private static String XORbits(String pkg, String key) {
		StringBuilder sb = new StringBuilder();
		
		if (key.length() < 256) {
			
			int oldsize = key.length();
			for(int i = 0; i < 256 - oldsize; i++) {
				
				key = "0" + key;
			}
			
			
		}
		
		for (int i = 0; i < pkg.length(); i++) {
			sb.append(charOf(bitOf(pkg.charAt(i)) ^ bitOf(key.charAt(i))));

		}

		String result = sb.toString();
		return result;
	}

}
