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
import java.util.ArrayList;
import java.util.Random;
import java.awt.Image;

import utilities.SoundManager;
import utilities.Sprite;
import utilities.Vector2D;

public class ShipPlayer extends GameObject{
	// Constats.delay GREATLY afflicts and acts as sever lagg for action() implementation
	
	public static final int SCALESHIP = 3;
    public static final int SHIPW = Sprite.PLAYER_SHIP_1S.getWidth(null)/SCALESHIP;
    public static final int SHIPH = Sprite.PLAYER_SHIP_1S.getHeight(null)/SCALESHIP;
    public static final int RADIUS = (SHIPW+SHIPH) / 4;
    
   // rotation velocity in radians per second 
    public static final double STEER_RATE = 3* Math.PI;  

    // acceleration when thrust is applied
    public static final double MAG_ACC = 1500; 
    
    // max speed
    public static final double MAX_SPEED = 1000; 

    // constant speed loss factor 
    public static final double DRAG = 0.0075;
                                        
    public static final Color COLOR = Color.cyan;
    
    private double thrustTime = 0;
    private double bulletAnimation = 0;
    private double shieldAnimationRotation = 0;
    private double ShieldRotationSpeed = 2; // per sec
    private double bulletCD = 0;
    private double dmgCD = 0;
 
    public int bulletAmount = 2;
    public boolean leftDrone = false;
    public boolean rightDrone = false;
    
    
    public ArrayList<Bullet> bullet = new ArrayList<Bullet>();
    public ShipDrone shipDrone= null;
    
    public int HP = 30;
    private double maxHP = 30;
    private boolean ImmortalLastShieldHit = true;
    
    // direction in which the nose of the ship is pointing 
    // this will be the direction in which thrust is applied 
    // it is a unit vector representing the angle by which the ship has rotated 
    public Vector2D direction;
    // controller which provides an Action object in each frame 
    private Controller ctrl;

    public ShipPlayer(Controller ctrl) {
    	super(new Vector2D(FRAME_WIDTH*WORLD_SIZE/2, FRAME_HEIGHT*WORLD_SIZE/2), new Vector2D(0,0), RADIUS);
    	this.ctrl = ctrl; 
    	
    	// handled later, just need initialization
    	this.direction = new Vector2D(90*Math.PI/180,90*Math.PI/180);
    	
    }

    @Override
    public void update() {
    	Action action = ctrl.action(); // copy so internal changes and iterations dont effect oneanother?
    	
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
		}
		
		bulletAnimation -= DT; 	// iterate animation time for bullet energy
		bulletCD -=DT;
		dmgCD -=DT;
		shieldAnimationRotation += DT;
		this.collisionTimeCount -= DT; // super
    }
    
	@Override
	public void hit(GameObject obj) {
		if (dmgCD <= 0) {
			if (obj instanceof Asteroid) {
				int size = ((Asteroid) obj).size;
				this.HP -= size;
				this.velocity.mult( - 0.6 );
				this.dmgCD = 0.1;
			}
			
			if (obj instanceof Bullet) {
				Bullet b = (Bullet) obj;
				if (b.friendlyFire) {
					this.HP -= b.dmg;
					this.velocity.mult(1 -(b.dmg)*0.1 -0.4);
				}
			}
			
			if (obj instanceof ShipEnemy) {
				this.HP -= 1;
				this.velocity.mult(-1);
				this.dmgCD = 0.1;
				SoundManager.play(SoundManager.bangLarge);
			}
			
			if (obj instanceof ShipPlayer) {
				this.HP -= 1;
				this.velocity.mult(-1);
				this.dmgCD = 0.1;
				SoundManager.play(SoundManager.bangLarge);
			}
			
			if (this.HP <= 1) {
				if (ImmortalLastShieldHit) ImmortalLastShieldHit = false;
				else this.dead = true;
				
				//dead sound
				SoundManager.play(SoundManager.bangLarge);
			}
		}
	}
	
	private void mkBullet() {
		
		// Cooldown ended, fire a bullet
		if ((this.bullet.size() <= 0) && bulletCD <=0 ) {
			
			
			for (int i=0; i<bulletAmount; i++) {
				bullet.add(new Bullet(new Vector2D(position), new Vector2D(velocity), new Vector2D(direction).rotate(Math.pow(-1, i)*((i/(int)2) + ((i) % 2))/20), 1, 35, 1));
			}
				
			// add fireing sound
			SoundManager.fire();
			this.bulletAnimation = 0.2; 	// ask for bullet animations
			this.bulletCD = bullet.get(0).ats;
		}		
	}
	
    
	@Override
    public void draw(Graphics2D g) {
		
		AffineTransform t0 = g.getTransform();
		
		
		// draw bullet first
	  	g.translate(position.x, position.y);
		g.rotate(direction.angle() - Math.PI/2);
		if (this.bulletAnimation >=0) { // there is a bullet, paint some graphics!
			  
			  int ri = 3; 	// inner circle
			  int shipD = 60;
			  int lines = 40;
			  double radA = (360/lines)*Math.PI/180;
			  for (int i = -lines/2; i<0; i++) { 	// nova beam
				  g.setColor(Color.white);
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
			for (int i = 0; i<=14; i++) { 	// nova beam
				if (Math.abs(i) <=6) g.setColor(Color.cyan);
				else if (Math.abs(i) <=8) g.setColor(Color.blue);
				else if (Math.abs(i) <=14) g.setColor(Color.white);
			
				g.drawLine((int)Math.pow(-1,i)*i, -60, 0, (int) ((-60 + (-1)*Math.abs(Math.random()*50))));
			}
		}
		g.setTransform(t0);
	  
		// draw ship
		Sprite ship = new Sprite(Sprite.PLAYER_SHIP_1S, this.position,this.direction,SHIPW,SHIPH,this.radius);
		ship.draw(g);
	  
		// draw power-shield
	  	g.translate(position.x, position.y);
	  	g.rotate(shieldAnimationRotation*ShieldRotationSpeed); // xdegrees per sec
		if (this.HP >=2) {
			  
			  int ri = ShipPlayer.RADIUS + 10; 	// inner circle
			  int ro = ShipPlayer.RADIUS + 15;
			  int lines = 180;
			  double radA = (360/lines)*Math.PI/180;

			  for (int i = 0; i<lines; i++) {
				  
				  if (i >= lines*(this.HP/maxHP)) {
					  g.setColor(Color.red);
				  }
				  else {
					  int rand = new Random().nextInt(4);
						switch (rand) {
							case 0:
								g.setColor(Color.blue);
								break;
							case 1:
								g.setColor(Color.lightGray);
								break;
							case 2:
								g.setColor(Color.cyan);
								break;
							case 3:
								g.setColor(Color.white);
								break;
						}
				  }
				  g.drawLine((int)(ri*Math.cos(i*radA)), (int)(ri*Math.sin(i*radA)), (int)(ro*Math.cos(i*radA)), (int)(ro*Math.sin(i*radA)));			
			  }
		}
		g.setTransform(t0);
	
	  
	  
	  // perfect for collision if we can spin this towards colliwion direction
//	  if (false) { // there is a bullet, paint some graphics!
//		  
//		  int ri = 3; 	// inner circle
//		  int ro = 9; 	// outer circle
//		  int shipD = 60;
//		  int lines = 40;
//		  double radA = (360/lines)*Math.PI/180;
//		  System.out.println("SHOOOOOTING");
//		  for (int i = -lines/2; i<0; i++) { 	// nova beam
//			  
//			  int ro2=0;
//			  if (i < -lines/4) ro2 = 50*(Math.abs(i+lines/2));
//			  if (i >= -lines/4) ro2 = 50*(Math.abs(i+1));
//			  
//			  if (Math.abs(i) <=4) g.setColor(Color.cyan);
//			  else if (Math.abs(i) <=6) g.setColor(Color.blue);
//			  else if (Math.abs(i) <=10) g.setColor(Color.white);
//			  g.drawLine((int)(ri*Math.cos(i*radA)), shipD+(int)(ri*Math.sin(i*radA)), (int)(ro2*Math.cos(i*radA)), shipD+(int)(ro2*Math.sin(i*radA)));			
//			}
//	  }
//	  g.setTransform(t0);

	  
//		Image image = Sprite.ship1;
//		double imW = image.getWidth(null);
//		double imH = image.getHeight(null);
//		
//		double width = 80;
//		double height = 120;
//		
//		// affine must move center of picture to x,y coordinate
//		AffineTransform t = new AffineTransform();
//		t.rotate(direction.angle() + Math.PI/2, 0, 0 );
//		t.scale(width / imW , height / imH);
//		t.translate(-imW / 2.0, -imH /2.0);
//		
//		// move center to ship location and paint
//		AffineTransform t0 = g.getTransform();
//		g.translate(position.x, position.y);	  
//		g.drawImage(image , t , null);
//		
//		//reset transform
//		g.setTransform(t0);

		
		
		// OLD DRAWN SHIP
//    	  AffineTransform at = g.getTransform();
//    	  Action action = ctrl.action();
//    	  
//
//    	  int[] XP = {-10, -10, -5, 0,  5,  10, 10};
//    	  int[] YP = {-13,  7,  13, 16, 13 ,7, -13};
//    	  
//    	     	  
//    	  g.translate(position.x, position.y);
//    	  double rot = direction.angle() - Math.PI/2; 	// dosent start facing north...
//    	  g.rotate(rot);
//    	  g.setColor(COLOR);
//    	  g.fillPolygon(XP, YP, XP.length);
//    	    	  

	}
}