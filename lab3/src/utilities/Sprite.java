package utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.ImageIcon;

public class Sprite {
	public Image image;
	public Vector2D position, direction;
	public double width , height, radius;
	
	public Sprite(Image i, Vector2D p, Vector2D d, double w, double h, double rad) {
		this.image = i;
		this.position = p;
		this.direction = d;
		this.width = w;
		this.height = h;
		this.radius = rad;
	}
	
	public void draw(Graphics2D g ) {
		double imW = image.getWidth(null);
		double imH = image.getHeight(null);
		AffineTransform t = new AffineTransform();
		t.rotate(direction.angle() + Math.PI/2, 0, 0 );
		t.scale(width / imW , height / imH);
		t.translate(-imW / 2.0, -imH /2.0);
		AffineTransform t0 = g.getTransform();
		g.translate(position.x , position.y);
		
		
		//g.setColor(Color.RED); // hitbox indicator
		//g.fillOval((int) (-this.radius), (int)(-this.radius),(int) (this.radius*2),(int) (this.radius*2));
		
		
		g.drawImage(image , t , null);
		g.setTransform(t0);
		
		//g.fillOval((int) t.getTranslateX(), (int)t.getTranslateY(),(int) this.radius,(int)this.radius);
	}

	public static ImageIcon resize(Image pic, double ww, double hh) {
		
		double w = pic.getWidth(null);
		double h = pic.getHeight(null);
		Image newimg = pic.getScaledInstance((int)(w/ww), (int)(h/hh),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		return new ImageIcon(newimg);  // transform it back
	}
	
	
	public static Image ASTEROID1, MILKYWAY1, MILKYWAY2, ship1, SPACE6, asteroid1;
	public static Image GUI_B_B, GUI_B_R, GUI_B_G, GUI_BACK, GUI_B_NG, GUI_B_Q, GUI_B_RG, GUI_B_HS, GUI_B_O;
	public static Image PLAYER_SHIP_1, PLAYER_SHIP_1S, ENEMY_SHIP_1S, Drone_ship2;
	public static Image GUI_MB_1,GUI_MB_2,GUI_MB_3, GUIXX3, GUI_VSS, GUI_VSSLEFT, GUI_VSSTEXT, GUI_menu, GUI_resume, GUI_MM;
	public static Image A1,A2,A3,A4,A5,A6,PU,GUI_restart, GUI_cancel;
	static {
	  try {
	    ASTEROID1 = ImageManager.loadImage("asteroid1");
	    MILKYWAY1 = ImageManager.loadImage("milkyway1");
	    MILKYWAY2 = ImageManager.loadImage("milkyway2");
	    
	    ship1 = ImageManager.loadImage("blue1");
	    
	    asteroid1 = ImageManager.loadImage("asteroid1");
	    
	    SPACE6 = ImageManager.loadImage("space8");
	    
	    MILKYWAY2 = ImageManager.loadImage("milkyway2");
	    
	    GUI_B_B = ImageManager.loadImage("handcraft/GUI_B_B");
	    GUI_B_R = ImageManager.loadImage("handcraft/GUI_B_R");
	    GUI_B_G = ImageManager.loadImage("handcraft/GUI_B_G");
	    GUI_BACK = ImageManager.loadImage("handcraft/GUI_Back3.0");
	    
	    GUI_B_NG = ImageManager.loadImage("handcraft/GUI_NG");
	    GUI_B_Q = ImageManager.loadImage("handcraft/GUI_Q");
	    GUI_B_RG = ImageManager.loadImage("handcraft/GUI_RG");
	    GUI_B_HS = ImageManager.loadImage("handcraft/GUI_HS");
	    GUI_B_O = ImageManager.loadImage("handcraft/GUI_O");
	    
	    GUI_VSS = ImageManager.loadImage("handcraft/GUI_ViewScore3.3CleanB");
	    GUI_VSSLEFT = ImageManager.loadImage("handcraft/GUI_ViewScore3.4CleanB");
	    GUI_VSSTEXT = ImageManager.loadImage("handcraft/GUI_ViewScoreText1");
	    
	    GUI_MM = ImageManager.loadImage("handcraft/GUI_MainMenu2T");
	    GUI_restart = ImageManager.loadImage("handcraft/GUI_VSB2.1_restart");
	    GUI_cancel = ImageManager.loadImage("handcraft/GUI_VSB2.1_cancelR");
	    
	    GUI_menu = ImageManager.loadImage("handcraft/GUI_VSB2.1_menuR");
	    GUI_resume = ImageManager.loadImage("handcraft/GUI_VSB2.1_resume");
	    
	    PLAYER_SHIP_1 = ImageManager.loadImage("handcraft/Ship_Player_1");
	    PLAYER_SHIP_1S = ImageManager.loadImage("handcraft/Ship_Player_1S");
	    
	    ENEMY_SHIP_1S = ImageManager.loadImage("handcraft/Ship_Enemy_1S");
	    Drone_ship2 = ImageManager.loadImage("handcraft/Drone_ship2");
	    
	    GUI_MB_1 = ImageManager.loadImage("handcraft/GUI_MB1");
	    GUI_MB_2 = ImageManager.loadImage("handcraft/GUI_MB2");
	    GUI_MB_3 = ImageManager.loadImage("handcraft/GUI_MB3");
	    GUIXX3 = ImageManager.loadImage("handcraft/guixx3");
	    
	    A1 = ImageManager.loadImage("handcraft/ast1");
	    A2 = ImageManager.loadImage("handcraft/ast2");
	    A3 = ImageManager.loadImage("handcraft/ast3");
	    A4 = ImageManager.loadImage("handcraft/ast4");
	    A5 = ImageManager.loadImage("handcraft/ast5");
	    A6 = ImageManager.loadImage("handcraft/ast6");
	    
	    PU = ImageManager.loadImage("handcraft/Chest1");
	    
	    
	    
	  } catch (IOException e) { e.printStackTrace(); }
	}
}