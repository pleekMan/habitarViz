package particlePeople;

import de.looksgood.ani.Ani;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Particle {

	Main p5;

	PVector posicion;
	PVector velocidad;
	PVector aceleracion;
	float damp;

	int radio = 7;

	int colorcito;

	Attractor attractor;

	public Particle(PVector startPosition) {

		p5 = getP5();

		posicion = startPosition;
		velocidad = new PVector(0, 0);
		// velocidad = PVector.fromAngle(random(PI) - HALF_PI);
		// velocidad.mult(10);
		// aceleracion = new PVector(0, 0);
		aceleracion = PVector.fromAngle(p5.random(p5.PI) - p5.HALF_PI);
		aceleracion.mult(10);
		damp = 0.0f;

		colorcito = p5.color(p5.random(255), p5.random(255), p5.random(255));
	}

	public void update() {

		// Direccion entre el mouse y la pelotita
		PVector direccion = PVector.sub(attractor.getPosition(), posicion);
		// normalizo la dirrecion, asi se pierde la magnitud
		direccion.normalize();

		// Escalo la normal segun me prefiera
		direccion.mult(10);
		p5.text(damp, 10, 10);

		// acelero el objeto
		aceleracion = direccion;

		damp = p5.norm(p5.dist(posicion.x, posicion.y, attractor.getPosition().x, attractor.getPosition().y), 0, p5.width);
		// limite de aceleracion
		velocidad.add(aceleracion);
		velocidad.mult(damp);

		velocidad.limit(10);

		// cambio de posicion
		posicion.add(velocidad);
		posicion.add(p5.random(-1, 1), p5.random(-1, 1), p5.random(0));
	}

	public void render() {
		p5.noStroke();
		// stroke(colorcito);
		// strokeWeight(2);
		p5.fill(255, 128, 0);
		// noFill();
		p5.ellipse(posicion.x, posicion.y, radio, radio);
	}

	public void assignAttractor(Attractor _attractor) {
		attractor = _attractor;
	}
	
	public void setSpawnPosition(PVector startPos){
		posicion = startPos;
	}
	
	public PVector getPosition(){
		return posicion;
	}
	

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
