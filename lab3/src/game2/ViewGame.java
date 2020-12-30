package game2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import utilities.Sprite;

import static game2.Constants.WORLD_SIZE;
import static game2.Constants.*;

public class ViewGame extends JPanel {
	// background colour
	public static final Color BG_COLOR = Color.black;

	Image im = Sprite.SPACE6; 
	AffineTransform bgTransf; 
	
	public Game game;
	  
	public ViewGame(Game x) {
	    double imWidth = im.getWidth(null); 
	    double imHeight = im.getHeight(null); 
	    double stretchx = (imWidth > Constants.FRAME_WIDTH? 1 : Constants.FRAME_WIDTH/imWidth); 
	    double stretchy = (imHeight > Constants.FRAME_HEIGHT? 1 : Constants.FRAME_HEIGHT/imHeight);
	    bgTransf = new AffineTransform();
	    bgTransf.scale(stretchx, stretchy);
	    
	    this.game = x;
			
	}
	
	// intersect around playerShip
	public boolean GameWorldIntersect(GameObject other){
		int psx = (int)Game.playerShip.position.x;
		int psy = (int)Game.playerShip.position.y;
		int x1 = psx - FRAME_WIDTH/2;
		int y1 = psy - FRAME_HEIGHT/2;
		
		// ships render area
		Area obj1 = new Area(new Rectangle2D.Double(x1,y1,FRAME_WIDTH,FRAME_HEIGHT));
		// obj area
		Area obj2 = new Area(new Ellipse2D.Double(other.position.x-other.radius, other.position.y-other.radius, other.radius*2, other.radius*2));
		
		obj1.intersect(obj2);
		return !obj1.isEmpty();
	}
		
	
	@Override
	public void paintComponent(Graphics g0) {
			
		Graphics2D g = (Graphics2D) g0;
		// paint the background
		g.drawImage(im, bgTransf,null);

		
		
		// one Frame Camera World
//		synchronized (Game.class) { 			// synchronized Blocks
//			for (GameObject obj: Game.GO) {
//				obj.draw(g);
//			
//			}
//		}
//		
//		// paint particles
//		if (!Game.ParticleList.isEmpty()) {
//			// paint particles
//			for (GameObject obj: Game.ParticleList) {
//				obj.draw(g);
//			}
//		}
		
		
		
		// data members
		int psx = (int)Game.playerShip.position.x;
		int psy = (int)Game.playerShip.position.y;
		int x1 = psx - FRAME_WIDTH/2;
		int y1 = psy - FRAME_HEIGHT/2;
		// Mini-map data members
		int ps = 5; //paint size
		int scale = 20;
		int MiniMapWidth = FRAME_WIDTH/scale;
		int MiniMapHeight = FRAME_HEIGHT/scale;			
		AffineTransform t0 = g.getTransform();
			
		
		// draw background GRID net
		g.translate(FRAME_WIDTH/2-psx, FRAME_HEIGHT/2-psy);
		double boxSide = 80;
		int linesH = (int)(FRAME_HEIGHT*WORLD_SIZE/boxSide);
		int linesW = (int)(FRAME_WIDTH*WORLD_SIZE/boxSide);
		// Transparent color builder
		Color c = Color.cyan;	
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
		for (int i=0; i<linesW+1; i++) {
			g.drawLine((int)(i*boxSide), 0, (int)(i*boxSide), FRAME_HEIGHT*WORLD_SIZE);
		}
		for (int i=0; i<linesH+1; i++) {
			g.drawLine(0, (int)(i*boxSide), FRAME_WIDTH*WORLD_SIZE, (int)(i*boxSide));
		}
		g.setTransform(t0);
		
		
		// moving world camera
		synchronized (Game.class) { 			// synchronized Blocks			
			for (GameObject obj: Game.GO) {
				
				// paint object if close to ship
				if (GameWorldIntersect(obj)) {

					// translate to top left scope of ships range
					g.translate(FRAME_WIDTH/2-psx, FRAME_HEIGHT/2-psy);
					obj.draw(g);
					g.setTransform(t0);
					
				}
				
				// paint minimap center bottom
				g.translate(FRAME_WIDTH/2-MiniMapWidth*(WORLD_SIZE/2), FRAME_HEIGHT-MiniMapHeight*(WORLD_SIZE+1));
				if (obj instanceof Asteroid) {
					g.setColor(Color.white);
					g.drawOval((int)obj.position.x/scale-ps/2, (int)obj.position.y/scale-ps/2, ps, ps);
				}
				if (obj instanceof ShipEnemy) {
					g.setColor(Color.red);
					g.drawOval((int)obj.position.x/scale-ps, (int)obj.position.y/scale-ps, 2*ps, 2*ps);
				}
				if (obj instanceof ShipPlayer) {
					g.setColor(Color.cyan);
					g.drawOval((int)obj.position.x/scale-ps, (int)obj.position.y/scale-ps, 2*ps, 2*ps);
					g.drawRect((int)obj.position.x/scale -MiniMapWidth/2, (int)obj.position.y/scale -MiniMapHeight/2, MiniMapWidth, MiniMapHeight);
				}
				g.setTransform(t0);
			}
		}
		
		// paint particles
		synchronized (Game.class) {
			if (!Game.ParticleList.isEmpty()) {
				// paint particles
				for (GameObject obj: Game.ParticleList) {
					g.translate(FRAME_WIDTH/2-psx, FRAME_HEIGHT/2-psy);
					obj.draw(g);
					g.setTransform(t0);
				}
			}
		}
		
		
		// draw minimap border
		g.translate(FRAME_WIDTH/2-MiniMapWidth*(WORLD_SIZE/2), FRAME_HEIGHT-MiniMapHeight*(WORLD_SIZE+1));
		g.setColor(Color.white);
		g.drawRect(0, 0, MiniMapWidth*WORLD_SIZE, MiniMapHeight*WORLD_SIZE); 	// worldMap 4 times the screensize
		g.setTransform(t0);
		
		
		
		// draw score 
		Color w = Color.white;
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 80));
		g.fillRect(FRAME_WIDTH/20-10, FRAME_HEIGHT/20-20, 200, 28);
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(Font.BOLD, currentFont.getSize() * 1.6F);
		g.setColor(Color.white);
		g.setFont(newFont);
		g.drawString("Score: "+this.game.score,FRAME_WIDTH/20, FRAME_HEIGHT/20);
		
		
		// draw Map Border
		g.translate(FRAME_WIDTH/2-psx, FRAME_HEIGHT/2-psy);
		
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
		g.fillRect(-25, 0, FRAME_WIDTH*WORLD_SIZE+50, -25); //top
		g.fillRect(-25, FRAME_HEIGHT*WORLD_SIZE, FRAME_WIDTH*WORLD_SIZE+50, 25); // bot	
		g.fillRect(0, -25, -25, FRAME_HEIGHT*WORLD_SIZE+50); //left	
		g.fillRect(FRAME_WIDTH*WORLD_SIZE, -25, 25, FRAME_HEIGHT*WORLD_SIZE+50); //right
	
		g.setColor(Color.black);
		g.fillRect(-30, -25, FRAME_WIDTH*WORLD_SIZE + 60, -5); // top		
		g.fillRect(-30, FRAME_HEIGHT*WORLD_SIZE+25, FRAME_WIDTH*WORLD_SIZE+60, 5); // bot	
		g.fillRect(-25, -25, -5, FRAME_HEIGHT*WORLD_SIZE+50); //left		
		g.fillRect(FRAME_WIDTH*WORLD_SIZE+25, -25, 5, FRAME_HEIGHT*WORLD_SIZE+50); //right

		g.setTransform(t0);
		
		
		
		// paint GUI
//		Image scaleGUIB = Sprite.resize(Sprite.GUIXX3,0.8,0.8).getImage();
//		int MBGUIBH = FRAME_WIDTH/2 - scaleGUIB.getWidth(null)/2;
//		int MBGUIBW = FRAME_HEIGHT - 2*scaleGUIB.getHeight(null)/2;
//		g.drawImage(scaleGUIB, MBGUIBH, MBGUIBW, null);
//		
		
//		Image scaleMB3 = Sprite.resize(Sprite.GUI_MB_3,2,2).getImage();
//		int MB3H = FRAME_WIDTH/2 - scaleMB3.getWidth(null)/2;
//		int MB3W = FRAME_HEIGHT - 2*scaleMB3.getHeight(null)/2;
//		g.drawImage(scaleMB3, MB3H, MB3W, null); 
//		
//		
//		Image scaleMB2 = Sprite.resize(Sprite.GUI_MB_2,2.2,2.2).getImage();
//		int MB2H = FRAME_WIDTH/2 - scaleMB2.getWidth(null)/2;
//		int MB2W = FRAME_HEIGHT - 2*scaleMB2.getHeight(null)/2;
//		g.drawImage(scaleMB2, MB2H, MB2W, null);
//				
	}

	@Override
	public Dimension getPreferredSize() {
		return Constants.FRAME_SIZE;
	}
}