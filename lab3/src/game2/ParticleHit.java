package game2;

import static game2.Constants.DT;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import utilities.Vector2D;

public class ParticleHit extends GameObject {

	public double TLTL = 1.5;
	public int time = 0;

	public ParticleHit(Vector2D p, double r) {
		super(p,null,r);
	}

	@Override
	public void update() {
		this.TLTL -= DT;
		this.time += 1;
		if (TLTL <= 0) this.dead = true;
	} 

	@Override
	public void draw(Graphics2D g) {
		// animate while TLTL
		// penta circles rotating a following target
		
		AffineTransform t0 = g.getTransform();
		
	  	g.translate(position.x, position.y);
	  	g.rotate(time*10);
		if (this.TLTL >=0) {
			  
			int ri = this.time; 	// inner circle
			int ro = (int)(this.time);
			int shipD = 60;
			int lines = 180;
			double radA = (360/lines)*Math.PI/180;

			for (int i = -lines; i<0; i++) {
				  
				int rand = new Random().nextInt(4);
				switch (rand) {
					case 0:
						g.setColor(Color.blue);
						break;
					case 1:
						g.setColor(Color.green);
						break;
					case 2:
						g.setColor(Color.yellow);
						break;
					case 3:
						g.setColor(Color.white);
						break;	  
				 	}
				g.drawLine((int)(ri*Math.cos(i*radA)), shipD+(int)(ri*Math.sin(i*radA)), (int)(ro*Math.cos(i*radA)), shipD+(int)(ro*Math.sin(i*radA)));			
			 	}
		}
		g.setTransform(t0);
	}

	@Override
	public void hit(GameObject x) {
		// do nothing: only graphical
	}
	
	}
