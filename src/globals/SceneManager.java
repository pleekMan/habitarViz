package globals;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PImage;
import processing.core.PVector;
import buildings.Building;

public class SceneManager {
	Main p5;

	PVector masterTranslate;

	PImage mapBack;

	ArrayList<Building> buildings;
	ArrayList<Camera> cameras;
	Camera camera;
	float cameraAltitude;

	float growingAreaRadius;

	public SceneManager() {
		p5 = getP5();

		p5.sphereDetail(4, 2);

		createCameras();
		camera = getCamera("MAIN");
		p5.ortho();

		p5.println(camera.getCamPosition());
		p5.println(camera.getCamTarget());
		camera.moveTo(new PVector(1000, cameraAltitude, 1000), 5f);

		masterTranslate = new PVector();
		mapBack = p5.loadImage("BuenosAires_alpha.png");

		buildings = new ArrayList<Building>();

		Building building0 = new Building();
		building0.setPosition(masterTranslate);
		buildings.add(building0);
		/*
		 * for (int i = 0; i < 5; i++) { Building building = new Building(this);
		 * building.setPosition(new PVector(random(width), random(height),
		 * random(-300))); buildings.add(building); }
		 */

		growingAreaRadius = 750;
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
		
		// USING AN ITERATOR TO GO THROUGH THE BUILDINGS.
		// IT'S REMOVE() METHOD AVOIDS CONCURRENT MODIFICATION AN ARRAYLIST SIZE
		Iterator<Building> buildingIterator = buildings.iterator();
		
		while(buildingIterator.hasNext()){
			Building actualBuilding = buildingIterator.next();
			
			actualBuilding.setScale(p5.norm(p5.mouseY, 0, p5.height));
			actualBuilding.render();
			
			if(checkBuildingOffScreen(actualBuilding)){
				buildingIterator.remove();
				//buildings.remove(actualBuilding);
			}
		}
		
	}

	private boolean checkBuildingOffScreen(Building building) {
		
		float screenX = p5.screenX(building.getPosition().x, building.getPosition().y, building.getPosition().z);
		float screenY = p5.screenY(building.getPosition().x, building.getPosition().y, building.getPosition().z);
		p5.println("ScreenX: " + screenX + " / ScreenY: " + screenY);
		
		p5.pushMatrix();
		p5.translate(building.getPosition().x + 50, building.getPosition().y, building.getPosition().z);
		p5.rotateY(p5.QUARTER_PI * 0.5f);
		p5.textSize(20);
		p5.text(screenX + " | " + screenY, 0, 0);
		p5.popMatrix();
		
		if(screenX < 0 || screenX > p5.width || screenY < 0 || screenY > p5.height){
			return true;
		} else {
			return false;
		}
		
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
		p5.rect(0, 0, growingAreaRadius * 2, growingAreaRadius * 2);

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

		int count = (int) p5.random(10, 20);
		float offset = 500f;

		for (int i = 0; i < count; i++) {

			float minX = camera.getCamTarget().x - growingAreaRadius;
			float maxX = camera.getCamTarget().x + growingAreaRadius;
			float minZ = camera.getCamTarget().z - growingAreaRadius;
			float maxZ = camera.getCamTarget().z + growingAreaRadius;

			float newX = p5.random(minX, maxX);
			float newZ = p5.random(minZ, maxZ);

			Building building = new Building();
			building.setPosition(new PVector(newX, 0, newZ));
			buildings.add(building);
		}

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
