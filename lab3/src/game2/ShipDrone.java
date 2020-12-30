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

public class ShipDrone extends GameObject{
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
 
    public int HP = 10;
    
    public int droneSide = 0; 	// whip can have 2 drones, 1=l, 2=r
    
    // direction in which the nose of the ship is pointing 
    // this will be the direction in which thrust is applied 
    // it is a unit vector representing the angle by which the ship has rotated 
    public Vector2D direction;
    // controller which provides an Action object in each frame 
    public AiHelpPlayer ctrl;

    public ShipDrone(ShipPlayer sp, AiHelpPlayer ctrl, int side) {
    	super(sp.position,  sp.velocity, RADIUS );
    	//super(new Vector2D(sp.position),  new Vector2D(sp.velocity), RADIUS );
    	this.ctrl = ctrl; 
    	this.direction = new Vector2D(90*Math.PI/180,90*Math.PI/180); // isnt implemented correctly in draw, it changes speed
    	this.droneSide = side;
    }

    @Override
    public void update() {
    	
    	// Aim and shoot from controller class
    	ActionAi action = ctrl.action(this);
   	
		if (action.turn == -1) { //Turn left
		    this.direction.rotate(-STEER_RATE*DT*5);
		}
		if (action.turn == 1) { //Turn Right
		    this.direction.rotate(STEER_RATE*DT*5);
		}
		
		// Shooting
		if (action.shoot) {
			mkBullet(); 
			action.shoot = false; 
		}
		
		// position of ship
		this.position = new Vector2D(Game.playerShip.position);
		// move behind ship
		this.position.add(new Vector2D(Game.playerShip.direction).mult(ShipPlayer.RADIUS).rotate(Math.pow(-1, droneSide)*10)); 	// distance spawned from the ship

		
		bulletAnimation -= DT; 	// iterate animation time for bullet energy
		bulletCD -=DT;
		dmgCD -=DT;
		this.collisionTimeCount -= DT; // super
    }
    
	@Override
	public void hit(GameObject obj) {
		if (dmgCD <= 0) {
			if (obj instanceof Asteroid) {
				this.HP -= 1;
				this.dmgCD = 0.1;
			}
			
			
			if (obj instanceof Bullet) {
				Bullet b = (Bullet) obj;
				if (b.friendlyFire) this.HP -= b.dmg;
			}
			
			// bounce on ships
			if (obj instanceof ShipEnemy) {
				this.HP -= 1;
				this.dmgCD = 0.1;
			}
			
			if (this.HP <= 0) {
				Game.ParticleList.add(new ParticleRedSharingan(this.position,2));
				this.dead = true;	
				//dead sound
				SoundManager.play(SoundManager.bangSmall);
			}
		}
	}
	
	private void mkBullet() {
		
		// Cooldown ended, fire a bullet
		if ((this.bullet == null) && bulletCD <=0 ) {
			bullet = new Bullet(new Vector2D(position), new Vector2D(velocity), new Vector2D(direction), 1, 25, 1);
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
			  int shipD = 20;
			  int lines = 10;
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
		
	  
		// draw ship
		Sprite ship = new Sprite(Sprite.Drone_ship2, this.position,this.direction,SHIPH/2,SHIPH/2,this.radius);
		ship.draw(g);
	}
}