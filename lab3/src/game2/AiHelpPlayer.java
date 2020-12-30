package game2;

import java.util.ArrayList;

import utilities.Vector2D;

public class AiHelpPlayer implements ControllerAi {
	  ActionAi action;
	  
	  private ShipEnemy target;
	  private double closestEnemy = 900;
	  
	  
	  public AiHelpPlayer() {
	    action = new ActionAi();
	  }	  
	  
	  // depraceated, Ship not abstract
	  public ActionAi action() {
		  return null;
	  }
	  	  
	  // return commands based on ship state
	  public ActionAi action(ShipDrone es) {
	
		  //reset closest enemy
		  this.closestEnemy = 900;
		  
		  // locate target ship
		  synchronized (Game.class) {
			  for (GameObject obj: Game.GO) {
				  if (obj instanceof ShipEnemy) {
					  if (es.position.dist(obj.position) < closestEnemy) {
						  // target enemy ship, or move towards it
						  this.target = (ShipEnemy)obj;
						  this.closestEnemy = es.position.dist(obj.position);
					  }
				  }
			  }
		  }
		  
		  // aim at ship if nearby
		  if (closestEnemy < 900) {
			  // Ai ship rotation
			  double asd = es.direction.angle();
			  
			  // angle between ships (ShipAnglePlayerShip)
			  double sAps = es.position.angle(target.position); 	
			  
			  // distance to target
			  double dist = es.position.dist(target.position);
			    
	  
			  // steering
			  if (sAps+asd > 0.06) this.action.turn = -1; // turn left
			  else if (sAps+asd < 0.04) this.action.turn = 1; // turn right
			  else this.action.turn = 0;
			  
			  if (dist < 600) {
				  this.action.shoot = true;
			  }			 
		  }
		  
		  return action; 
	  }
}
