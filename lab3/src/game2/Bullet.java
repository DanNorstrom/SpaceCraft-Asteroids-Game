package game2;

import static game2.Constants.DT;
import static game2.Constants.WORLD_SIZE;
import static game2.Constants.FRAME_HEIGHT;
import static game2.Constants.FRAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import utilities.Vector2D;

public class Bullet extends GameObject {

	public double TLTL = 2;
	public int dmg;
	public double ats;
	
	private Vector2D direction;
	
	public boolean friendlyFire;
	public int type;
	
	
	public Bullet(Vector2D p, Vector2D v, Vector2D d, double x, int dist, int type) {
		super(p, v, x);
		this.direction = d;
		
		double mag = this.velocity.mag();
		if (mag <= 500) mag = 500;
		this.velocity = new Vector2D(direction);
		this.velocity.mult(mag);
		
		this.position.add(this.direction.mult(dist)); 	// distance spawned from the ship
		
		this.dead = false;
		
		this.type = type;
		
		if (type ==1) {
			this.dmg = 1;
			this.ats = 0.2;
			this.friendlyFire = false;
		}
		if (type ==2) {
			this.dmg = 1;
			this.ats = 0.2;
			this.friendlyFire = true;
		}
	}

	@Override
	public void hit(GameObject obj) { 
		this.dead = true;
	}
	
	@Override
	public void update() {
		this.TLTL -= DT;
		if (TLTL <= 0) this.dead = true;
		this.position.addScaled(this.velocity, DT);
		//this.position.set((this.position.x + FRAME_WIDTH) % FRAME_WIDTH, (this.position.y + FRAME_HEIGHT) % FRAME_HEIGHT);
		this.position.wrap(FRAME_WIDTH*WORLD_SIZE, FRAME_HEIGHT*WORLD_SIZE);
	}
	

	@Override
	public void draw(Graphics2D g) {
		AffineTransform at = g.getTransform();
		  	
		int[] XP = {-2,-2, -1, 0, 1, 2, 2};
		int[] YP = {-5, 3,  4, 5, 4 ,3,-5};
		
		int[] XP2 = {-1,-1, -0, 0, 0, 1, 1};
		int[] YP2 = {-4, 2,  3, 4, 3 ,2,-4};
		    	  
		g.translate(this.position.x - this.radius/2, this.position.y - this.radius/2);
  	  	double rot = direction.angle() - Math.PI/2; 	// dosent start facing north...
  	  	g.rotate(rot);
  	  	
  	  	if (this.type == 1) {
			g.setColor(Color.blue);
			g.fillPolygon(XP, YP, XP.length);
			g.setColor(Color.white);
			g.fillPolygon(XP2, YP2, XP2.length);
  	  	}
  	  	
  	  if (this.type == 2) {
			g.setColor(Color.red);
			g.fillPolygon(XP, YP, XP.length);
			g.setColor(Color.yellow);
			g.fillPolygon(XP2, YP2, XP2.length);
	  	}
				  
		g.setTransform(at);
		
	}
}
