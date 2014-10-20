package particlePeople;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Attractor {
	
	Main p5;
	
	float radius, angle;
	float radiusVel, angleVel;

	PVector position;
	PVector center;

	public Attractor(PVector _center) {

		p5 = getP5();
		
		center = _center;
		position = center.get();

		radius = 10;
		radiusVel = 0.2f;

		angle = p5.random(p5.TWO_PI);
		angleVel = p5.random(-0.1f, 0.1f);
	}

	public void update() {

		radius += radiusVel;
		angle += angleVel;

		if (radius > 100) {
			radius = 100;
			radiusVel *= -1;
		}
		if (radius < 10) {
			radius = 10;
			radiusVel *= -1;
		}

		position.set(center.x + (radius * p5.cos(angle)), center.y + (radius * p5.sin(angle)));
	}

	public void render() {

		p5.stroke(255, 0, 255);
		p5.noFill();
		p5.ellipse(position.x, position.y, 5, 5);
	}

	public PVector getPosition() {
		return position;
	}
	
	public void setCenter(PVector _center){
		center.set(_center.x, _center.z, _center.y); // FLIPPING AXES
		center.z = center.z - 500;
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
