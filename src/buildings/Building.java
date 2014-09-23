package buildings;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;
import shapes3d.utils.*;
import shapes3d.animation.*;
import shapes3d.*;

public class Building {

	Path extrusionPath;
	Contour contour;
	ContourScale contourScale;
	Extrusion extrusion;

	Main p5;

	float scaling;
	PVector position;

	public Building() {
		p5 = getP5();

		extrusionPath = new P_LinearPath(new PVector(0, -200, 0), new PVector(0, 0, 0));
		contour = getBuildingContour();
		contourScale = new CS_ConstantScale();
		// Create the texture coordinates for the end
		contour.make_u_Coordinates();
		// Create the extrusion
		extrusion = new Extrusion(p5, extrusionPath, 1, contour, contourScale);
		// extrusion.setTexture("wall.png", 1, 1);
		extrusion.drawMode(S3D.TEXTURE);
		// Extrusion end caps
		// extrusion.setTexture("grass.jpg", S3D.E_CAP);
		// extrusion.setTexture("sky.jpg", S3D.S_CAP);
		extrusion.drawMode(S3D.TEXTURE, S3D.BOTH_CAP);

		scaling = 1.0f;
		position = new PVector();
		
	}

	public void render() {
		// extrusion.scale(map(mouseX, 0, width, 0, 1));
		p5.pushMatrix();
		p5.translate(position.x, position.y, position.z);
		p5.scale(1, scaling, 1);
		extrusion.draw();
		p5.popMatrix();
	}

	public void setScale(float _scale) {
		scaling = _scale;
	}

	public void setPosition(PVector pos) {
		position.set(pos);
		p5.println("New Building at: " + position);

	}
	
	public PVector getPosition(){
		return position;
	}

	public Contour getBuildingContour() {
		PVector[] c = new PVector[] { new PVector(-30, 30), new PVector(30, 30), new PVector(50, 10), new PVector(10, -30), new PVector(-10, -30), new PVector(-50, 10) };
		return new BuildingContour(c);
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}