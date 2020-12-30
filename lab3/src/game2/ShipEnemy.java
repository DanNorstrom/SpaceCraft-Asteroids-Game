package game2;

import static game2.Constants.DT;
import static game2.Constants.WORLD_SIZE;
import static game2.Constants.FRAME_HEIGHT;
import static game2.Constants.FRAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Random;
import java.awt.Image;

import utilities.SoundManager;
import utilities.Sprite;
import utilities.Vector2D;

public class ShipEnemy extends GameObject{
	// Constats.delay GREATLY afflicts and acts as sever lagg for action() implementation
	
	public static final int SCALESHIP = 4;
    public static final int SHIPW = Sprite.PLAYER_SHIP_1S.getWidth(null)/SCALESHIP;
    public static final int SHIPH = Sprite.PLAYER_SHIP_1S.getHeight(null)/SCALESHIP;
    public static final int RADIUS = ((SHIPW+SHIPH) / 4) +2;
    
   // rotation velocity in radians per second 
    public static final double STEER_RATE = 0.5* Math.PI;  

    // acceleration when thrust is applied
    public static final double MAG_ACC = 400; 
    
    // max speed
    public static final double MAX_SPEED = 200; 

    // constant speed loss factor 
    public static final double DRAG = 0.0075;
                                        
    public static final Color COLOR = Color.cyan;
    
    private double thrustTime = 0;
    private double bulletAnimation = 0;
    private double bulletCD = 0;
    private double dmgCD = 0;
    
    public Bullet bullet = null; 
    public int HP = 5;
    
    // direction in which the nose of the ship is pointing 
    // this will be the direction in which thrust is applied 
    // it is a unit vector representing the angle by which the ship has rotated 
    public Vector2D direction;
    // controller which provides an Action object in each frame 
    public AiSeekAndDestroy ctrl;

    public ShipEnemy(AiSeekAndDestroy ctrl) {
    	super(null,null,RADIUS );
    	this.ctrl = ctrl; 
    	this.direction = new Vector2D(90*Math.PI/180,90*Math.PI/180); // isnt implemented correctly in draw, it changes speed
    	
    	//override for super Constructor, code moved out of game.java
		// spawn new ships at the game world border
		this.velocity = new Vector2D((-Asteroid.MAX_SPEED) + ((new Random().nextDouble())*(2*Asteroid.MAX_SPEED)),(-Asteroid.MAX_SPEED) + ((new Random().nextDouble())*(2*Asteroid.MAX_SPEED)));
		
		int range = new Random().nextInt(4);
		switch(range) {
			case 0:
				this.position = new Vector2D(new Random().nextInt((FRAME_WIDTH*WORLD_SIZE)), 1);
				break;
			case 1:
				this.position = new Vector2D(new Random().nextInt(FRAME_WIDTH*WORLD_SIZE), FRAME_HEIGHT*WORLD_SIZE-1);
				break;
			case 2:
				this.position = new Vector2D(1, new Random().nextInt(FRAME_HEIGHT*WORLD_SIZE));
				break;
			case 3:
				this.position = new Vector2D(FRAME_WIDTH*WORLD_SIZE-1, new Random().nextInt(FRAME_HEIGHT*WORLD_SIZE));
				break;
		}	
    }

    @Override
    public void update() {
    	ActionAi action = ctrl.action(this); // copy so internal changes and iterations dont effect oneanother?
    	
		//this.position.set((this.position.x + FRAME_WIDTH) % FRAME_WIDTH, (this.position.y + FRAME_HEIGHT) % FRAME_HEIGHT);
		this.position.wrap(FRAME_WIDTH*WORLD_SIZE, FRAME_HEIGHT*WORLD_SIZE);
    	
    	// Movement
    	if (action.thrust == 1) { // gain speed   		
	    		if (Math.hypot(this.velocity.x,this.velocity.y) > MAX_SPEED) {
	    			// max speed reached
	    			this.velocity.mult(0.9-DRAG);
	    		}
	    		else {
	    			this.velocity.addScaled(this.direction, MAG_ACC*DT);
	    			thrustTime = 1;
	    			// this.direction.normalise()
	    		}
	    		// play sound
	    		SoundManager.startThrust();
    	}
    	if (action.thrust == 0) { // reduce speed constantly
    		this.velocity.mult(1-DRAG);
    		thrustTime -= DT; 				//alot of calculations. better then having it in draw
    		// play sound
    		SoundManager.stopThrust();
    	}
		if (action.turn == -1) { //Turn left
		    this.direction.rotate(-STEER_RATE*DT);
		}
		if (action.turn == 1) { //Turn Right
		    this.direction.rotate(STEER_RATE*DT);
		}
		// update ship position
		this.position.addScaled(velocity, DT);
		// Shooting
		if (action.shoot) {
			mkBullet(); 
			action.shoot = false; 
		}
		
		bulletAnimation -= DT; 	// iterate animation time for bullet energy
		bulletCD -=DT;
		dmgCD -=DT;
		this.collisionTimeCount -= DT; // super
    }
    
	@Override
	public void hit(GameObject obj) {
		if (dmgCD <= 0) {
			if (obj instanceof Asteroid) {
				int size = ((Asteroid) obj).size;
				this.HP -= size;
				//this.velocity.mult(1-size*3); 			// fly backwards!
				this.velocity.mult(1 -size*0.1 -0.4);
				this.dmgCD = 0.1;
			}
			
			
			if (obj instanceof Bullet) {
				Bullet b = (Bullet) obj;
				this.HP -= b.dmg;
				//this.velocity.mult(1-size*3); 			// fly backwards!
				this.velocity.mult(1 -(b.dmg)*0.1 -0.4);
				
				// add score if killed by player or drone
				if ((b.type == 1) && (this.HP <= 0)){
					Game.score += 100;
				}
			}
			
			// bounce on ships
			if (obj instanceof ShipEnemy) {
				this.HP -= 1;
				this.velocity.mult(-2);
				this.dmgCD = 0.1;
				SoundManager.play(SoundManager.bangMedium);
			}
			if (obj instanceof ShipPlayer) {
				this.HP -= 1;
				this.velocity.mult(-2);
				this.dmgCD = 0.1;
				SoundManager.play(SoundManager.bangMedium);
			}
			
			if (this.HP <= 0) {
				Game.ParticleList.add(new ParticleRedSharingan(this.position,this.radius));
				this.dead = true;
				
				//dead sound
				SoundManager.play(SoundManager.bangLarge);
			}
		}
	}
	
	private void mkBullet() {
		
		// Cooldown ended, fire a bullet
		if ((this.bullet == null) && bulletCD <=0 ) {
			bullet = new Bullet(new Vector2D(position), new Vector2D(velocity), new Vector2D(direction), 1, 25, 2);
			// add fireing sound
			SoundManager.fire();
			this.bulletAnimation = 0.2; 	// ask for bullet animations
			this.bulletCD = bullet.ats;
		}
		
	}
	
    
	@Override
    public void draw(Graphics2D g) {
		
		AffineTransform t0 = g.getTransform();
		
		
		// draw bullet charging animation first
	  	g.translate(position.x, position.y);
		g.rotate(direction.angle() - Math.PI/2);
		if (this.bulletAnimation >=0) { // there is a bullet, paint some graphics!
			  
			  int ri = 3; 	// inner circle
			  int shipD = 60;
			  int lines = 40;
			  double radA = (360/lines)*Math.PI/180;

			  for (int i = -lines/2; i<0; i++) { 	// nova beam
				  
				  int rand = new Random().nextInt(4);
				  switch (rand) {
					  case 1:
						  g.setColor(Color.red);
						  break;
					  case 2:
						  g.setColor(Color.yellow);
						  break;
					  case 3:
						  g.setColor(Color.white);
						  break;	  
				  }
				  
				  int ro=0;
				  if (i < -lines/4) ro = 5*(Math.abs(i+lines/2));
				  if (i >= -lines/4) ro = 5*(Math.abs(i+1));
				  g.drawLine((int)(ri*Math.cos(i*radA)), shipD+(int)(ri*Math.sin(i*radA)), (int)(ro*Math.cos(i*radA)), shipD+(int)(ro*Math.sin(i*radA)));			
			  }
		}
		g.setTransform(t0);
		

		// draw thrust
	  	g.translate(position.x, position.y);
		g.rotate(direction.angle() - Math.PI/2);
		if (this.thrustTime >= 0) { 	// we're still animating thrust!
			for (int i = 0; i<=20; i++) { 	// nova beam
				if (Math.abs(i) <=8) g.setColor(Color.yellow);
				else if (Math.abs(i) <=12) g.setColor(Color.red);
				else if (Math.abs(i) <=20) g.setColor(Color.white);
			
				g.drawLine((int)Math.pow(-1,i)*i, -40, 0, (int) ((-40 + (-1)*Math.abs(Math.random()*50))));
			}
		}
		g.setTransform(t0);
	  
		// draw ship
		Sprite ship = new Sprite(Sprite.ENEMY_SHIP_1S, this.position,this.direction,SHIPH,SHIPH,this.radius);
		ship.draw(g);
	}
}