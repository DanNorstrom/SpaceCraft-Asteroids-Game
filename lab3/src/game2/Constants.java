package game2;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Constants {
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
	
	public static final int FRAME_HEIGHT = screenSize.height;
	public static final int FRAME_WIDTH = screenSize.width; 
	public static final Dimension FRAME_SIZE = new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
	
	public static final int WORLD_SIZE = 4; // 4 times height/width
	
	// sleep time between two frames 
	// Constats.delay GREATLY afflicts and acts as sever lagg for action() implementation
	public static final int DELAY = 30;  // in milliseconds
	public static final double DT = DELAY / 1000.0;  // in seconds
	//DT Shows how often we update. if Something should do something 5times per second
	// that is the same thing as a update() of 5*DT
	// MAINTAINS gamespeed WHILE reducing FPS
}