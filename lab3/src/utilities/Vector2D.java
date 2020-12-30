package utilities;

import game2.Constants;

public class Vector2D {

	public double x, y;

	// constructor for zero vector
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}

	// constructor for vector with given coordinates
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// constructor that copies the argument vector 
	public Vector2D(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}

	// set coordinates
	public Vector2D set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	// set coordinates based on argument vector 
	public Vector2D set(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}

	// compare for equality (note Object type argument) 
	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector2D) {
			Vector2D other = (Vector2D) o; 
			return ((other.x == this.x) && (other.y == this.y));
		}
		else {
			return false;
		}
	} 

	// String for displaying vector as text 
	public String toString() {
		return "("+this.x+":"+this.y+")";
	}
			
	//  magnitude (= "length") of this vector 
	public double mag() {
		return Math.hypot(this.x, this.y);
	}

	// angle between vector and horizontal axis in radians in range [-PI,PI] 
	// can be calculated using Math.atan2 
	public double angle() {
		// this is Theta, from Cartesian to Polar
		return Math.atan2(this.y,this.x);
	}

	// angle between this vector and another vector in range [-PI,PI] 
	public double angle(Vector2D other) {
		// test????
		
		
		double angle = Math.atan2(-(other.y - this.y), other.x - this.x);
		return angle;
//		double xa = Math.abs(this.x - other.x);
//		double xy = Math.abs(this.y - other.y);
//		double ans = Math.acos(xa/xy);
//		return ans;
		
//		 angle = acos((v1*v2)/abs(abs(v1)*abs(v2))

//		double xa = Math.hypot(this.x, other.x);
//		double ya = Math.hypot(this.y, other.y);
//		double v1 = Math.abs(this.x * this.y);
//		double v2 = Math.abs(other.x * other.y);
//		return Math.acos((v1*v2)/(xa*ya));
		
//		if (this.equals(other)) {
//			return 0.0;
//		}
//		else {
//			double angleNew = -(Math.atan2(this.det(other), this.dot(other)));
//			//double angleNew = Math.atan2(Math.abs(other.y-this.y), Math.abs(other.x - this.x));
//			if (angleNew < -Math.PI)
//				angleNew += 2 * Math.PI;
//			if (angleNew > Math.PI)
//				angleNew -= 2 * Math.PI;
//			return angleNew;
//		}
	}

	// add argument vector 
	public Vector2D add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}

	// add values to coordinates 
	public Vector2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	// weighted add - surprisingly useful
	public Vector2D addScaled(Vector2D v, double fac) {
		this.x += fac*v.x;
		this.y += fac*v.y;
		return this;
	}

	// subtract argument vector 
	public Vector2D subtract(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}

	// subtract values from coordinates 
	public Vector2D subtract(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	// multiply with factor 
	public Vector2D mult(double fac) {
		this.x *= fac;
		this.y *= fac;
		return this;
	}

	// rotate by angle given in radians 
	public Vector2D rotate(double angle) {
		double xTemp = x;
		double yTemp = y;
		this.x = Math.cos(angle)*xTemp - Math.sin(angle)*yTemp; 
		this.y = Math.sin(angle)*xTemp + Math.cos(angle)*yTemp;
		return this;
	}

	// "dot product" ("scalar product") with argument vector 
	public double dot(Vector2D v) {
		//xa*xb + ya*yb
		return ((this.x * v.x) + (this.y * v.y));
	}
	
	// "Determinant"
	public double det(Vector2D v) {
		//xa*xb + ya*yb
		return ((this.x * v.x) - (this.y * v.y));
	}

	// distance to argument vector 
	public double dist(Vector2D v) {
		return Math.hypot((this.x - v.x), (this.y - v.y));
	}

	// normalise vector so that magnitude becomes 1 
	public Vector2D normalise() {
		double mag = this.mag();
		if (mag ==0) {
			this.x = this.y = 0;
		}
		else {
			this.x /= mag;
			this.y /= mag;
		}
		return this;
	}

	// wrap-around operation, assumes w> 0 and h>0
	// remember to manage negative values of the coordinates
	public Vector2D wrap(double w, double h) {
		// does this work?
		//w = Constants.DT * w;
		//h = Constants.DT * h;
		x = (x + w) % w;
		y = (y + h) % h;
		return this;
	}

	// construct vector with given polar coordinates  
	public static Vector2D polar(double angle, double mag) {
		return new Vector2D(mag * Math.cos(angle),mag * Math.sin(angle));	
	}
}
