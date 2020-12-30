package utilities;

import static game2.Constants.FRAME_WIDTH;
import static game2.Constants.FRAME_HEIGHT;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageManager {

	// this may need modifying
	public final static String path = "images/";
	public final static String ext = ".png";

	public static Map<String, Image> images = new HashMap<String, Image>();

	public static Image getImage(String s) {
		return images.get(s);
	}

	// loads and scales all images to current resolution
	public static Image loadImage(String fname) throws IOException {
		BufferedImage img = null;
		img = ImageIO.read(new File(path + fname + ext));
		
		// Image scaling based on screen resolution
		Image img2 = img.getScaledInstance((int)((img.getWidth()*(FRAME_WIDTH/2560.0))), (int)(img.getHeight()*(FRAME_HEIGHT/1440.0)), java.awt.Image.SCALE_SMOOTH);
		
		images.put(fname, img2);
		return img2; 
	}

	public static Image loadImage(String imName, String fname) throws IOException {
		BufferedImage img = null;
		img = ImageIO.read(new File(path + fname + ext));
		images.put(imName, img);
		return img; 
	}

	public static void loadImages(String[] fNames) throws IOException {
		for (String s : fNames)
			loadImage(s);
	}

	public static void loadImages(Iterable<String> fNames) throws IOException {
		for (String s : fNames)
			loadImage(s);
	}

}
