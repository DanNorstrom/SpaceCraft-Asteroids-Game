package game2;

import static game2.Constants.DT;
import static game2.Constants.WORLD_SIZE;
import static game2.Constants.FRAME_HEIGHT;
import static game2.Constants.FRAME_WIDTH;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import utilities.SoundManager;
import utilities.Sprite;
import utilities.Vector2D;


public class Asteroid extends GameObject {
	public static final double MAX_SPEED = 300;
	
	public int size;
	public int HP;
	public double TLTL;
	
	public Vector2D direction;
	
	public Image img;
	
	private double AsteroidCollisionTimer = 0;
	public boolean stuck = false;
	
	public Asteroid() { // Random first call to populate screen
		super(new Vector2D(((new Random().nextDouble())* (FRAME_WIDTH*WORLD_SIZE)),((new Random().nextDouble())* (FRAME_HEIGHT*WORLD_SIZE))),  new Vector2D((-MAX_SPEED) + ((new Random().nextDouble())*(2*MAX_SPEED)),(-MAX_SPEED) + ((new Random().nextDouble())*(2*MAX_SPEED))), 10+100*(new Random().nextDouble()) );
		
		this.size = (int)this.radius / (int)10;
		this.HP = size;
		this.TLTL = size*20;
		
		this.direction = new Vector2D(((new Random().nextDouble())* (2*Math.PI)),((new Random().nextDouble())* (2*Math.PI)));
		
		int rand = new Random().nextInt(6);
			switch (rand) {
				case 0:
					this.img = Sprite.A1;
					break;
				case 1:
					this.img = Sprite.A2;
					break;
				case 2:
					this.img = Sprite.A3;
					break;
				case 3:
					this.img = Sprite.A4;
					break;
				case 4:
					this.img = Sprite.A5;
					break;
				case 5:
					this.img = Sprite.A6;
					break;
			}	
	}


	public Asteroid(Vector2D c, Vector2D v, double rad) {
		super(c,v,rad);
		this.size = (int)rad / (int)10;
		this.HP = size;
		this.TLTL = size*20;
		this.direction = new Vector2D(((new Random().nextDouble())* (2*Math.PI)),((new Random().nextDouble())* (2*Math.PI)));
		
		int rand = new Random().nextInt(6);
		switch (rand) {
			case 0:
				this.img = Sprite.A1;
				break;
			case 1:
				this.img = Sprite.A2;
				break;
			case 2:
				this.img = Sprite.A3;
				break;
			case 3:
				this.img = Sprite.A4;
				break;
			case 4:
				this.img = Sprite.A5;
				break;
			case 5:
				this.img = Sprite.A6;
				break;
		}

	}
	
	public Asteroid(double x, double y, double vx, double vy) {
		super(new Vector2D(x,y), new Vector2D(vx,vy), 10+30*(new Random().nextDouble()));
	}
	

	public void update() {
		this.position.addScaled(velocity, DT);
		//this.position.set((this.position.x + FRAME_WIDTH) % FRAME_WIDTH, (this.position.y + FRAME_HEIGHT) % FRAME_HEIGHT);
		this.position.wrap(FRAME_WIDTH*WORLD_SIZE, FRAME_HEIGHT*WORLD_SIZE);
		
		this.direction.rotate(0.1*Math.PI*DT);
		this.TLTL -= DT;
		if (TLTL <= 0) this.dead = true;
		
		this.AsteroidCollisionTimer -= DT;
		this.collisionTimeCount -= DT; // super
	}
	
	public ArrayList<GameObject> spawnChildren() {
		ArrayList<GameObject> children = new ArrayList<>();
		int childAmount = 2;
		
		// 2020 CONSTRAINT: Spawn more children if TLTL hits 0
		if (this.TLTL <= 0) childAmount += 1;
		
		for (int i=0; i<childAmount; i++) {
			Asteroid nc = new Asteroid(new Vector2D(this.position.add(this.radius*1.5*(Math.pow(-1, i+1)), this.radius*1.5*(Math.pow(-1, i)))), new Vector2D(this.velocity.rotate(30*(Math.pow(-1, i)))), (this.size -1)*10 );
			nc.img = this.img; // force children same type
			children.add(nc);
		}
		return children;
	}
	
	@Override
	public void draw(Graphics2D g) {
		Sprite asteroid = new Sprite(this.img, this.position,this.direction,this.radius*2, this.radius*2, this.radius);
		asteroid.draw(g);
		
		// old meteor
//		g.setColor(Color.red);
//		g.fillOval((int) (this.position.x - this.radius/2), (int) (this.position.y - this.radius/2), 2 * (int)this.radius, 2 * (int)this.radius);
	}
	
	public void hit(GameObject obj) {
		
		// allways breaks from ship collision
		if (obj instanceof ShipPlayer) {
			this.velocity.mult(-0.4);
			this.HP -=3;
			// score if collision
			if (this.HP <= 0) {
				Game.score += this.size*5;
			}
		}
		if (obj instanceof ShipEnemy) {
			this.velocity.mult(-0.4);
			this.HP -=3;
		}
		if (obj instanceof Asteroid) {
			this.velocity.mult(-1);
			this.position.addScaled(velocity, DT);
			if (AsteroidCollisionTimer <= 0) {
				this.AsteroidCollisionTimer = 4*DT; 	// check if stuck
			}
			else {
				this.stuck = true; 		// destroy if stuck
			}
		}
		if (obj instanceof Bullet) {
			Bullet b = ((Bullet) obj);
			this.HP -= b.dmg;
			
			// add score if killed by player or drone
			if ((b.type == 1) && (this.HP <= 0)){
				Game.score += this.size*10;
			}
		}
		
		if (HP <= 0) {
			this.dead = true;
			
			// play death sound for ast size
			if (this.size >2) SoundManager.play(SoundManager.bangLarge);
			if (this.size == 2) SoundManager.play(SoundManager.bangMedium);
			if (this.size <2) SoundManager.play(SoundManager.bangSmall);
		}
	}
}