package globals;

import java.io.File;
import java.util.ArrayList;

import buildings.Building;
import processing.core.*;
import processing.opengl.*;
import controlP5.*;
import de.looksgood.ani.Ani;

public class Main extends PApplet {

	SceneManager scene;

	ControlP5 controllers;
	Slider2D camPosControl;

	Ani ani; 
	public void setup() {

		size(1000, 1000, P3D);
		setPAppletSingleton();
		
		Ani.init(this);
		setAniSingleton();

		scene = new SceneManager();

		controllers = new ControlP5(this);
		createControllers();
		
		
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { Main.class.getName() });
		// PApplet.main(new String[] { "--present", Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}
	
	private void setAniSingleton(){
		AniSingleton.getInstance().setAni(ani);
	}

	public void draw() {
		background(143, 141, 126);

		pushStyle();
		pushMatrix();

		scene.update();
		scene.render();

		popMatrix();
		popStyle();

	}

	// CONTROLLERS - BEGIN --------------------------

	private void createControllers() {

		camPosControl = new Slider2D(controllers, "Cam_Position");
		camPosControl.setPosition(10, 10);
		camPosControl.setSize(200, 200);
		camPosControl.setMinX(0);
		camPosControl.setMaxX(scene.getMap().width);
		camPosControl.setMinY(0);
		camPosControl.setMaxY(scene.getMap().height);
		// PImage mapThumb = createImage(100, 100, ARGB);
		// mapThumb.copy(scene.getMap(), 0, 0, scene.getMap().width,
		// scene.getMap().height, 0, 0, mapThumb.width, mapThumb.height);
		// camPosControl.setSize(mapThumb);
		// camPosControl.setImage(mapThumb);

	}

	public void Cam_Position() {
		
		PVector newPos = new PVector(camPosControl.arrayValue()[0], scene.getCameraAltitude(), camPosControl.arrayValue()[1]);
		scene.setCameraPos(newPos);
	}

	public void controlEvent(ControlEvent theEvent) {

		/*
		 * if (theEvent.isFrom(controllers.getController("Radial Extent"))) {
		 * //bufferManager.updateRadialExtent(theEvent.getValue()); } if
		 * (theEvent.isFrom(controllers.getController("Ringosity"))) {
		 * bufferManager
		 * .updateRingosity(theEvent.getController().getArrayValue()); }
		 */
	}

	// CONTROLLERS - END ----------------------------

	public void keyPressed() {
		/*
		 * for (Building building : buildings) { building.setPosition(new
		 * PVector(random(width), random(height), random(-300))); }
		 */

		scene.onKeyPressed(key);

		if (key == CODED) {
			if (keyCode == UP) {
			}
		}
	}

	public void mousePressed() {
	}

	public void mouseReleased() {
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
	}

	public void mouseMoved() {
	}
}
