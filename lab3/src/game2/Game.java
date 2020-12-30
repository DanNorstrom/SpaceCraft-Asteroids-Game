package game2;

import static game2.Constants.DT;
import static game2.Constants.WORLD_SIZE;
import static game2.Constants.FRAME_HEIGHT;
import static game2.Constants.FRAME_WIDTH;

import java.util.ArrayList;
import java.util.Random;

import utilities.Vector2D;

public class Game {
	public static final int N_INITIAL_ASTEROIDS = 100;
  
	// Game Objects
	public static ArrayList<GameObject> GO;
	public static ArrayList<GameObject> ParticleList;
  
	// score management
	public static int score; 
  
	// UI Variables
	public int asteroidCount;
	public int waveCount = 1;
	
	// Player Controller
	public Keys ctrl;
	public static ShipPlayer playerShip;

	// game loop
	public boolean alive;
	public boolean pause;
	
	// level design
	public double time = 0;

	public Game() {
		ParticleList = new ArrayList<GameObject>();
		
		GO = new ArrayList<GameObject>();
		for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
			// spawn new asteroids at the game world border
			Vector2D vel = new Vector2D((-Asteroid.MAX_SPEED) + ((new Random().nextDouble())*(2*Asteroid.MAX_SPEED)),(-Asteroid.MAX_SPEED) + ((new Random().nextDouble())*(2*Asteroid.MAX_SPEED)));
			double rad = 10+100*(new Random().nextDouble());
			
			Vector2D pos= new Vector2D(); 	// empty assign
			int range = new Random().nextInt(4);
			switch(range) {
				case 0:
					//top side
					pos = new Vector2D(new Random().nextInt((FRAME_WIDTH*WORLD_SIZE)), new Random().nextInt((int)(FRAME_HEIGHT*WORLD_SIZE/2.4)));
					break;
				case 1:
					// bot side
					pos = new Vector2D(new Random().nextInt(FRAME_WIDTH*WORLD_SIZE), FRAME_HEIGHT*WORLD_SIZE-new Random().nextInt((int)(FRAME_HEIGHT*WORLD_SIZE/2.4)));
					break;
				case 2:
					// left side
					pos = new Vector2D(new Random().nextInt((int)(FRAME_WIDTH*WORLD_SIZE/2.4)), new Random().nextInt(FRAME_HEIGHT*WORLD_SIZE));
					break;
				case 3:
					// right side
					pos = new Vector2D(FRAME_WIDTH*WORLD_SIZE-new Random().nextInt((int)(FRAME_WIDTH*WORLD_SIZE/2.4)), new Random().nextInt(FRAME_HEIGHT*WORLD_SIZE));
					break;
			}
			
			Game.GO.add(new Asteroid(pos,vel,rad));
			asteroidCount += 1;
		}
		// add ship controlled by User keys
		ctrl = new Keys();
		playerShip = new ShipPlayer(ctrl);
		GO.add(playerShip);
		
		// add Ai enemy ship
		//GO.add(new EnemyShip( new AiSeekAndDestroy() ) ); 
		this.alive = true;
		this.pause = false;
	}


	public void update() throws Exception{
	  
		this.time += DT;
		ArrayList<GameObject> Alive = new ArrayList<>();
	  
		// Update Game State
		synchronized (Game.class) {
			for (GameObject obj: GO) {
				obj.update();
				
				// delete asteroids stuck on spawn
				if (obj instanceof Asteroid) {
					if (((Asteroid) obj).stuck){
						asteroidCount -= 1;
						continue;
					}
				}
				
				// keep if not dead
				if (!obj.dead) Alive.add(obj);
			  
				// enemy ship
				if ((obj instanceof ShipEnemy)) {
					ShipEnemy x = ((ShipEnemy) obj);
					
					// spawn ship bullets
					if (x.bullet != null) {
						Alive.add(x.bullet);
						x.bullet = null;
					}
					
					if (x.dead) {
						// 40% chance to spawn powerups
						int rand = new Random().nextInt(10);
						if (rand >=3) Alive.add(new PowerUp(x.position, x.velocity.mult(0.2)));
						
					}
				}
				
				// for Asteroids
				if (obj.dead && (obj instanceof Asteroid)) {
					asteroidCount -= 1;
					// split asteroids on impact and spawn children
					Asteroid ast = (Asteroid) obj;
					if (ast.size >= 4) {
						ArrayList<GameObject> childList = ast.spawnChildren();
											
						// add count for spawned children
						for (GameObject child: childList) {
							asteroidCount += 1;
							Alive.add(child);
						}
						childList.clear();
					}
				}
		 
				// for ship
				if (obj instanceof ShipPlayer) {
					
					ShipPlayer s = (ShipPlayer)obj;
					// spawn ship bullets
					if (s.bullet.size() > 0) {
						for (Bullet b: s.bullet) {
							Alive.add(b);
						}
						s.bullet.clear();
					}
					
					// add drone powerup if recieved
					if (s.shipDrone != null) {
						Alive.add(s.shipDrone);
						s.shipDrone = null;
					}
					
					// if ship is dead, quit game round, return to menu
					if (s.dead) {
						this.alive = false;
					}
				}
				
				// dead drones open ship slot for new drone powerup
				if (obj instanceof ShipDrone) {
					ShipDrone sd = (ShipDrone)obj;
					if (sd.dead) {
						int s = sd.droneSide;
						if (s == 1 ) Game.playerShip.leftDrone = false;
						if (s == 2 ) Game.playerShip.rightDrone = false;
					}
					// spawn drone bullets
					else if(sd.bullet != null) {
						Alive.add(sd.bullet);
						sd.bullet = null;
					}
				}
				
			
			}
			// Remove dead objects
			GO.clear();
			GO = Alive;   
			
			// send data to ai if there is any
			
			// check if the game is paused "ESC pressed"
			if (ctrl.action().pause) {
				this.pause = true;
				ctrl.action().pause = false; // extract action once
			}
			
			// spawn asteroids until 100 is present
			if (asteroidCount < N_INITIAL_ASTEROIDS) {
				
				// spawn new asteroids at the game world border
				Vector2D vel = new Vector2D((-Asteroid.MAX_SPEED) + ((new Random().nextDouble())*(2*Asteroid.MAX_SPEED)),(-Asteroid.MAX_SPEED) + ((new Random().nextDouble())*(2*Asteroid.MAX_SPEED)));
				double rad = 10+30*(new Random().nextDouble());
				
				Vector2D pos= new Vector2D(); 	// empty assign
				int range = new Random().nextInt(4);
				switch(range) {
					case 0:
						pos = new Vector2D(new Random().nextInt((FRAME_WIDTH*WORLD_SIZE)), 1);
						break;
					case 1:
						pos = new Vector2D(new Random().nextInt(FRAME_WIDTH*WORLD_SIZE), FRAME_HEIGHT*WORLD_SIZE-1);
						break;
					case 2:
						pos = new Vector2D(1, new Random().nextInt(FRAME_HEIGHT*WORLD_SIZE));
						break;
					case 3:
						pos = new Vector2D(FRAME_WIDTH*WORLD_SIZE-1, new Random().nextInt(FRAME_HEIGHT*WORLD_SIZE));
						break;
				}
				
				Game.GO.add(new Asteroid(pos,vel,rad));
				asteroidCount += 1;
			}
			
			// add one enemy ship every 3sec
			if (time/waveCount >= 3) {
				waveCount += 1;
				Game.GO.add(new ShipEnemy( new AiSeekAndDestroy() ) ); 
			}
		}
		
		// particle list generated mainly from collisions
		ArrayList<GameObject> AliveParticle = new ArrayList<>();
		synchronized (Game.class) {
			for (GameObject obj: ParticleList) {
				obj.update();
				if (!obj.dead) AliveParticle.add(obj);
			}
			Game.ParticleList.clear();
			Game.ParticleList = AliveParticle;
		}
		
		// Collision detection
		for (int i = 0; i < GO.size(); i++) {
			for (int j = i+1; j < GO.size(); j++) {
				// collision check adn return boolean
				if (GO.get(i).collisionHandling(GO.get(j))) {
					// animation for every collision
					ParticleList.add(new ParticleHit(GO.get(i).position, GO.get(i).radius));
				}
			}
		}
	}
}