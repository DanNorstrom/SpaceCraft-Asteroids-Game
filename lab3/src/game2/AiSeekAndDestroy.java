package game2;

import java.util.ArrayList;

import utilities.Vector2D;

public class AiSeekAndDestroy implements ControllerAi {
	  ActionAi action;
	  
	  private Vector2D shipPos;
	  private Vector2D shipDir;
	  private Vector2D shipVel;
	  //private ArrayList<GameObject> GOS;
	  
	  
	  public AiSeekAndDestroy() {
	    action = new ActionAi();
	  }
	  
//	  // call to update actions
//	  public void updateAction(ArrayList<GameObject> GO) {
//		 this.GOS = GO; 	// copy to avoid sync blocks or conccurancyE
//	  }
	  
	  
	  // depraceated, Ship not abstract
	  public ActionAi action() {
		  return null;
	  }
	  
	  
	  // return commands based on ship state
	  public ActionAi action(ShipEnemy es) {
	
		  // locate target ship
		  for (GameObject obj: Game.GO) {
			  if (obj instanceof ShipPlayer) {
				  // target enemy ship, or move towards it
				  this.shipPos = obj.position;
				  this.shipVel = obj.velocity;
				  this.shipDir = ((ShipPlayer)obj).direction;
			  }
		  }
		  
		  // Ai ship rotation
		  double asd = es.direction.angle();
		  
		  // angle between ships
		  double sAps = es.position.angle(shipPos); 	
		  
		  // distance to target
		  double dist = es.position.dist(shipPos);
		    
		  
//		  // convert to degrees 0-360
//		  double esd = 0;
//		  double ss = 0;
//		  
//		  if (asd >= 0) esd = Math.toDegrees(asd);
//		  if (sAps >= 0) ss = Math.toDegrees(sAps);
//		  
//		  if (asd < 0) esd = 360 + Math.toDegrees(asd);
//		  if (sAps < 0) ss = 360 + Math.toDegrees(sAps);
//		  
//		  // steering
//		  if (((ss+5)<esd) && ((ss-5)<esd)) this.action.turn = 0; // turn left
//		  else if ((ss+5)<esd) this.action.turn = -1; // turn right
//		  else if ((ss-5)<esd) this.action.turn = 1;
//
//		  System.out.println("relation:  " +esd+"   Erot: "+ss); 
		  
		  
		  // testing
		  //System.out.println("relation:  " +sAps+"   Erot: "+asd+"  sum: "+(sAps+asd)); 	  
		  
		  
		  // Hunting Mode
		  if (es.HP > 2) {
			  
			  // steering
			  if (sAps+asd > 0.06) this.action.turn = -1; // turn left
			  else if (sAps+asd < 0.04) this.action.turn = 1; // turn right
			  else this.action.turn = 0;
			  
			  // move closer if far away
			  if (dist > 500) this.action.thrust = 1;
			  else this.action.thrust = 0;
			  
			  if (dist < 600) {
				  this.action.shoot = true;
			  }
		  }
		  
		  // Fleeing Mode
		  else if (es.HP <=2) {
			  if ((sAps+asd) > 2.5) this.action.turn = -1; // turn left
			  else if ((sAps+asd) < 2.5) this.action.turn = 1; // turn right
			  else this.action.turn = 0;
			  this.action.thrust = 1;
		  }
		  
		  
		  
		  // Dodge Asteroids, shoot some
		  for (GameObject obj: Game.GO) {
			  if (obj instanceof Asteroid) {
				  if (es.position.dist(obj.position) < 150){
					  //System.out.println(es+" Avoiding "+obj);
					  
					  // angle between ships
					  double sAas = es.position.angle(obj.position);
					  
					  // small angle left
					  if ((sAas+asd) > 0.05) {
						  this.action.turn = 1; // turn right
						  this.action.shoot = true;
					  }
					  // small angle right
					  else if ((sAas+asd) <= 0.05) {
						  this.action.turn = -1; // turn right
						  this.action.shoot = true;
					  }
					  // big angle increase speed
					  if ((sAas+asd) > 0.6) {
						  this.action.thrust = 1;
						  this.action.shoot = false;
					  }
				  }
			  }  
		  }
		  return action; 	   
	  }
}
