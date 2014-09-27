package buildings;

import de.looksgood.ani.Ani;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;
import shapes3d.utils.*;
import shapes3d.animation.*;
import shapes3d.*;

public class Building {

	Main p5;

	Path extrusionPath;
	Contour contour;
	ContourScale contourScale;
	Extrusion extrusion;

	float buildingHeight;
	float scaling;
	PVector position;
	int fillColor;

	// Ani scaleAnimation;

	public Building() {
		p5 = getP5();

		//fillColor = p5.color(p5.random(255), p5.random(255), p5.random(255));
		fillColor = p5.color(255);

		buildingHeight = p5.random(-1000, 0);
		extrusionPath = new P_LinearPath(new PVector(0, buildingHeight, 0), new PVector(0, 0, 0));
		contour = createBuildingContour();
		contourScale = new CS_ConstantScale();
		// contourScale = new CS_LinearScale(0f, 1f);

		// Create the texture coordinates for the end
		contour.make_u_Coordinates();
		// Create the extrusion
		extrusion = new Extrusion(p5, extrusionPath, 1, contour, contourScale);
		extrusion.drawMode(S3D.SOLID | S3D.WIRE);
		//extrusion.drawMode(S3D.WIRE);
		extrusion.fill(fillColor);
		extrusion.drawMode(S3D.SOLID, S3D.BOTH_CAP);

		// extrusion.setTexture("wall.png", 1, 1);
		// extrusion.drawMode(S3D.TEXTURE);
		// Extrusion end caps
		// extrusion.setTexture("grass.jpg", S3D.E_CAP);
		// extrusion.setTexture("sky.jpg", S3D.S_CAP);
		// extrusion.drawMode(S3D.TEXTURE, S3D.BOTH_CAP);

		scaling = 0.0f;
		position = new PVector();

		triggerGrow();

	}

	public void render() {
		// extrusion.scale(map(mouseX, 0, width, 0, 1));
		if (scaling > 0.01) {
			p5.pushMatrix();
			p5.translate(position.x, position.y, position.z);
			p5.scale(1, scaling, 1);
			p5.fill(fillColor);
			extrusion.draw();
			p5.popMatrix();
		}
	}

	public void triggerGrow() {
		float duration = p5.random(5, 20);
		float delay = p5.random(10);
		// Ani.to(theTarget, theDuration, theDelay, theFieldName, theEnd,
		// theEasing)
		Ani.to(this, duration, delay, "scaling", 1f, Ani.CUBIC_IN_OUT);
	}

	public void setScale(float _scale) {
		scaling = _scale;
	}

	public void setPosition(PVector pos) {
		position.set(pos);
		p5.println("New Building at: " + position);

	}

	public PVector getPosition() {
		return position;
	}

	public float getHeight() {
		return buildingHeight;
	}

	public Contour createBuildingContour() {
		if (p5.random(1) < 0.5f) {
			PVector[] c = { new PVector(-30, 20), new PVector(30, 20), new PVector(30, -20), new PVector(10, -20), new PVector(10, -10), new PVector(-10, -10), new PVector(-10, -20), new PVector(-30, -20) };
			return new BuildingContour(c);

		} else {
			PVector[] c = { new PVector(-30, 30), new PVector(30, 30), new PVector(50, 10), new PVector(10, -30), new PVector(-10, -30), new PVector(-50, 10) };
			return new BuildingContour(c);

		}
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}