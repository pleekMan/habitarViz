package globals;

import java.io.File;
import java.util.ArrayList;

import buildings.Building;
import processing.core.*;
import processing.opengl.*;
import controlP5.*;
import de.looksgood.ani.Ani;

import processing.serial.*;
import cc.arduino.*;

import supercollider.*;
import supercollider.Group;

import oscP5.*;

public class Main extends PApplet {

	SceneManager scene;

	ControlP5 controllers;
	Slider2D camPosControl;

	Ani ani;

	Arduino arduino;
	int manopla01Pin = 0;

	Group group;
	Synth granulador;

	int threshold = 50;
	int a0 = 0;

	public void setup() {

		size(1000, 1000, P3D);
		setPAppletSingleton();

		Ani.init(this);
		setAniSingleton();

		scene = new SceneManager();

		controllers = new ControlP5(this);
		createControllers();

		textSize(50);

		println(Arduino.list());
		arduino = new Arduino(this, Arduino.list()[4], 57600);
		arduino.pinMode(manopla01Pin, Arduino.INPUT);

		group = new Group();
		group.create();

		granulador = new Synth("buf_grain_test");
		granulador.addToTail(group);

	}

	public boolean sketchFullScreen() {
		// return true;
		return false;
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { Main.class.getName() });
		// PApplet.main(new String[] { "--present", Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}

	private void setAniSingleton() {
		AniSingleton.getInstance().setAni(ani);
	}

	public void draw() {
		// background(143, 141, 126);
		background(255);

		fill(255,0,0);
		rect(0, 0, arduino.analogRead(manopla01Pin * 10), 50);

		if (arduino.analogRead(0) > threshold) {
			a0 = arduino.analogRead(0);
		} else {
			a0 = 0;
		}

		granulador.set("grainSize", map(a0, 0, 420, 0.1f, 100.0f));
		granulador.set("amp", map(a0, 0, 1023, 1.f, 0f));
		text(1 - map(a0, 0, 420, 1.f, 0f), arduino.analogRead(manopla01Pin * 10), 60);

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

		scene.onMousePressed();
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
