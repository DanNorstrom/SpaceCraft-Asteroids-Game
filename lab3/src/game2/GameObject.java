package game2;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import utilities.Vector2D;
import static game2.Constants.*;

public abstract class GameObject {
	public Vector2D position;
	public Vector2D velocity;
	public boolean dead;
	public double radius;
	public double collisionTimeCount = 2;
	
	public GameObject(Vector2D p, Vector2D v, double r) {
		this.position = p;
		this.velocity = v;
		this.radius = r;
	}
	
	public abstract void hit(GameObject x);
	
	public void update(){
		this.position.addScaled(velocity, DT);
		this.position.set((this.position.x + FRAME_WIDTH) % FRAME_WIDTH, (this.position.y + FRAME_HEIGHT) % FRAME_HEIGHT);
	}
	
	public abstract void draw(Graphics2D g);
	
	public boolean overlap(GameObject other){
		Area obj1 = new Area(new Ellipse2D.Double(this.position.x-this.radius, this.position.y-this.radius, this.radius*2, this.radius*2));
		Area obj2 = new Area(new Ellipse2D.Double(other.position.x-other.radius, other.position.y-other.radius, other.radius*2, other.radius*2));

		obj1.intersect(obj2);
		return !obj1.isEmpty();
		
	}
	public boolean collisionHandling(GameObject other) {
		if (this.overlap(other)) { //this.getClass() != other.getClass() &&		
			this.hit(other);
			other.hit(this);
			
			// controls Collision animation from Game.Java
			if (this.collisionTimeCount <= 0) return true;
			else return false;		
		}
		else {
			return false;	
		}
	}
}
	

