package globals;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PImage;
import processing.core.PVector;
import buildings.Building;
import buildings.BuildingManager;

public class SceneManager {
	Main p5;

	PVector masterTranslate;

	PImage mapBack;
	
	BuildingManager buildingManager;
	
	ArrayList<Camera> cameras;
	Camera camera;
	float cameraAltitude;


	public SceneManager() {
		p5 = getP5();

		p5.sphereDetail(4, 2);
		
		masterTranslate = new PVector(0,0,0);
		mapBack = p5.loadImage("BuenosAires_alpha.png");

		createCameras();
		camera = getCamera("MAIN");
		p5.ortho();

		p5.println(camera.getCamPosition());
		p5.println(camera.getCamTarget());
		camera.moveTo(new PVector(1000, cameraAltitude, 1000), 5f);
		
		buildingManager = new BuildingManager();
		buildingManager.setGrowingAreaRadius(750);


	}

	public void update() {
		camera.update();
		p5.camera(camera.getCamPosition().x, camera.getCamPosition().y, camera.getCamPosition().z, camera.getCamTarget().x, camera.getCamTarget().y, camera.getCamTarget().z, 0, 1, 0);
		p5.lights();
	}

	public void render(){
		
		drawCityMap();
		drawAxisGizmo();
		drawCameras();
		
		p5.translate(masterTranslate.x, masterTranslate.y, masterTranslate.z);
		// rotateX(map(mouseY,0,height,0,TWO_PI));
		
		buildingManager.render();
		
	}



	private void drawCameras() {
		for (Camera cam : cameras) {
			cam.drawCameraGizmo();
		}

		// DRAW BUILDING GROWING AREA
		p5.pushMatrix();
		p5.pushStyle();
		p5.noFill();
		p5.stroke(0, 255, 0);
		p5.rectMode(p5.CENTER);

		p5.translate(camera.getCamTarget().x, -2, camera.getCamTarget().z);
		p5.rotateX(p5.HALF_PI);
		p5.rect(0, 0, buildingManager.getGrowingAreaRadius() * 2, buildingManager.getGrowingAreaRadius() * 2);

		p5.popStyle();
		p5.popMatrix();
	}

	private void createCameras() {

		cameraAltitude = -1000;
		cameras = new ArrayList<Camera>();

		PVector sceneCenter = new PVector();

		// CAM LEFT
		PVector cam01Pos = new PVector(0, cameraAltitude, 0);
		PVector leftCamOffset = PVector.sub(cam01Pos, new PVector(1000, -cameraAltitude, 1000));
		Camera cam01 = new Camera(cam01Pos, leftCamOffset);
		cam01.setName("LEFT");
		cameras.add(cam01);

		// CAM MAIN
		PVector cam02Pos = new PVector(500, cameraAltitude, 500);
		PVector mainCamOffset = PVector.sub(cam02Pos, new PVector(0, 0, 0));
		Camera cam02 = new Camera(cam02Pos, mainCamOffset);
		cam02.setName("MAIN");
		cameras.add(cam02);

	}

	private Camera getCamera(String camName) throws NullPointerException {

		for (Camera cam : cameras) {
			if (cam.getName().equals(camName)) {
				return cam;
			}
		}
		p5.println("----------- No Cameras found with name:" + camName);
		throw new NullPointerException();

	}

	private void drawAxisGizmo() {
		p5.pushMatrix();
		p5.translate(masterTranslate.x, masterTranslate.y, masterTranslate.z);
		// X
		p5.fill(255, 0, 0);
		p5.stroke(255, 0, 0);
		p5.line(0, 0, 0, 200, 0, 0);
		// box(100);

		// Y
		p5.fill(0, 255, 0);
		p5.stroke(0, 255, 0);
		p5.line(0, 0, 0, 0, 200, 0);

		// Z
		p5.fill(0, 0, 255);
		p5.stroke(0, 0, 255);
		p5.line(0, 0, 0, 0, 0, 200);

		p5.popMatrix();
	}

	private void drawCityMap() {

		p5.pushStyle();
		p5.tint(255);
		// p5.imageMode(p5.CENTER);

		p5.pushMatrix();

		p5.translate(masterTranslate.x, masterTranslate.y, masterTranslate.z);
		p5.rotateX(p5.HALF_PI);
		p5.image(mapBack, 0, 0);

		p5.popMatrix();

		p5.popStyle();
	}

	public void growBuildings() {

		buildingManager.triggerGrowBuildings(camera);

	}

	public void setCameraPos(PVector camPos) {
		camera.setCameraPosition(camPos);
	}

	public float getCameraAltitude() {
		return cameraAltitude;
	}

	public void setCameraAltitude(float alt) {
		cameraAltitude = alt;
	}

	public void switchToMainCamera() {
		camera = getCamera("MAIN");
	}

	public void switchToLeftCamera() {
		camera = getCamera("LEFT");
	}

	public PImage getMap() {
		return mapBack;
	}

	void onKeyPressed(char key) {

		if (key == '1') {
			switchToLeftCamera();
		}
		if (key == '2') {
			switchToMainCamera();
		}

		if (key == 'm') {
			camera.moveTo(new PVector(1000, cameraAltitude, 1000), 5f);
		}
		if (key == 'c') {
			camera.moveTo(new PVector(5000, cameraAltitude, 5000), 10f);
		}

		if (key == 'b') {
			growBuildings();
		}
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
