package game2;

import static game2.Constants.DT;
import static game2.Constants.FRAME_HEIGHT;
import static game2.Constants.FRAME_WIDTH;
import static game2.Constants.WORLD_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Random;

import utilities.Sprite;
import utilities.Vector2D;

public class PowerUp extends GameObject {
	
	public static final int SCALESHIP = 5;
	public static int SHIPW = Sprite.PU.getWidth(null)/SCALESHIP;
    public static int SHIPH = Sprite.PU.getHeight(null)/SCALESHIP;
    public static final int RADIUS = (SHIPW+SHIPH) / 4;

    private Vector2D direction = new Vector2D(90*Math.PI/180,90*Math.PI/180);
    private int type;
    private Color col;
    
    
	public PowerUp(Vector2D p, Vector2D v, int type) {
		super(p, v, PowerUp.RADIUS);
		
		switch (type) {
		case 0:
			//shield
			this.type = 0;
			this.col = Color.blue;
			break;
		case 1:
			//more bullets
			this.type = 1;
			this.col = Color.red;
			break;
		case 2:
			//Spawn helper drone
			this.type = 2;
			this.col = Color.green;
			break;
		case 3:
			// Golden reward Charge
			this.type = 3;
			this.col = Color.yellow;
			break;
		}	
	}
	
	// random
	public PowerUp(Vector2D p, Vector2D v) {
		super(p, v, (SHIPW+SHIPH) / 4);
		int rand = new Random().nextInt(4);
		switch (rand) {
			case 0:
				//shield
				this.type = 0;
				this.col = Color.blue;
				break;
			case 1:
				//more bullets
				this.type = 1;
				this.col = Color.red;
				break;
			case 2:
				//Spawn helper drone
				this.type = 2;
				this.col = Color.green;
				break;
			case 3:
				// Golden reward Charge
				this.type = 3;
				this.col = Color.yellow;
				break;
		}
	}
	
	
	public void update() {
		this.position.addScaled(velocity, DT);
		this.position.wrap(FRAME_WIDTH*WORLD_SIZE, FRAME_HEIGHT*WORLD_SIZE);	
		this.direction.rotate(0.1*Math.PI*DT);
		this.collisionTimeCount -= DT; // super
	}
	
	
	
	// testing spawns powerups randomly
	public PowerUp() {
		super(new Vector2D(((new Random().nextDouble())* (FRAME_WIDTH*WORLD_SIZE)),((new Random().nextDouble())* (FRAME_HEIGHT*WORLD_SIZE))),  new Vector2D((-100) + ((new Random().nextDouble())*(2*100)),(-100) + ((new Random().nextDouble())*(2*100))), (SHIPW+SHIPH) / 4 );
		int rand = new Random().nextInt(4);
		switch (rand) {
			case 0:
				//shield
				this.type = 0;
				this.col = Color.blue;
				break;
			case 1:
				//more bullets
				this.type = 1;
				this.col = Color.red;
				break;
			case 2:
				//Spawn helper drone
				this.type = 2;
				this.col = Color.green;
				break;
			case 3:
				// Golden reward Charge
				this.type = 3;
				this.col = Color.yellow;
				break;
		}
		
	}
	
	
	private void SendPowerUp(ShipPlayer x) {
		
		switch (this.type) {
		case 0:
			//shield
			x.HP += 10;
			if (x.HP > 30) x.HP = 30;
			break;
		case 1:
			//more bullets
			if (x.bulletAmount < 30 ) x.bulletAmount += 1;
			break;
		case 2:
			//Spawn helper drone
			if (!Game.playerShip.leftDrone) {
				Game.playerShip.leftDrone = true;
				ShipDrone sd = new ShipDrone(Game.playerShip, new AiHelpPlayer(), 1);
				Game.playerShip.shipDrone = sd;
			}
			else if (!Game.playerShip.rightDrone) {
				Game.playerShip.rightDrone = true;
				ShipDrone sd = new ShipDrone(Game.playerShip, new AiHelpPlayer(), 2);
				Game.playerShip.shipDrone = sd;
			}
			break;
		case 3:
			// Golden reward
			Game.score += 500;
			break;
		}
	}

	@Override
	public void hit(GameObject x) {
		
		if (x instanceof ShipEnemy) {
			this.dead = true;
		}
		
		if (x instanceof Bullet) {
			Bullet b = (Bullet)x;
			if (b.friendlyFire) this.dead = true;
		}
		
		if (x instanceof Asteroid) {
			this.velocity.mult( - 0.6 );
		}
		
		if (x instanceof PowerUp) {
			this.velocity.mult( - 0.6 );
		}
		
		if (x instanceof ShipPlayer) {
			SendPowerUp((ShipPlayer)x);
			this.dead = true;
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		Sprite ship = new Sprite(Sprite.PU, this.position,this.direction,SHIPW,SHIPH,this.radius);
		ship.draw(g);
		
		// paint circle
		AffineTransform t0 = g.getTransform();
		
		g.translate(this.position.x-SHIPW/2, this.position.y-SHIPH/2);
		g.setColor(this.col);
		g.drawOval(0, 0, SHIPW, SHIPH);
		
		g.setTransform(t0);
	}

}
