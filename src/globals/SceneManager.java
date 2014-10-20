package globals;

import interactionViz.UserLine;

import java.util.ArrayList;
import java.util.Iterator;

import particlePeople.ParticleManager;
import processing.core.PImage;
import processing.core.PVector;
import buildings.Building;
import buildings.BuildingManager;

public class SceneManager {
	Main p5;

	PVector masterTranslate;

	// PImage mapBack;
	CityMap cityMap;

	ArrayList<Comuna> comunas;
	int activeComuna;
	Comuna comuna0, comuna1;
	BuildingManager buildingManager;

	ArrayList<Camera> cameras;
	Camera camera;
	float cameraAltitude;
	PVector cameraPos0;
	PVector cameraPos1;

	// UserLine line01;

	// boolean[] activeAreas;

	ParticleManager particleManager;

	// ArduinoManager arduino;

	public SceneManager() {
		p5 = getP5();

		p5.sphereDetail(4, 2);
		
		cameraAltitude = -600;

		masterTranslate = new PVector(0, 0, 0);

		comunas = new ArrayList<Comuna>();
		createComunas("ViviendasComunas.csv");
		activeComuna = 0;
		comuna0 = comunas.get(activeComuna);
		comuna1 = comunas.get(1);

		cityMap = new CityMap();

		createCameras();
		camera = getCamera("MAIN");
		// p5.ortho();
		cameraPos0 = new PVector(0, cameraAltitude, 0);
		cameraPos1 = new PVector(1000, cameraAltitude, 0);



		p5.println(camera.getCamPosition());
		p5.println(camera.getCamTarget());
		camera.moveTo(new PVector(0, cameraAltitude, 0), 5f);

		buildingManager = new BuildingManager();
		buildingManager.setGrowingAreaRadius(300);

		// line01 = new UserLine();
		// line01.initialize(new PVector(0, 0, 0), new PVector(500, 0, 500));

		/*
		 * activeAreas = new boolean[6]; for (int i = 0; i < activeAreas.length;
		 * i++) { activeAreas[i] = false; }
		 */

		particleManager = new ParticleManager();
		particleManager.setDeathAttractorCenter(camera.getCamPosition());

		// arduino = new ArduinoManager();
	}

	// KINDA LIKE A STRUCT
	public static class ComunasGlobalStats {

		public static int totalViviendasMin = 100000;
		public static int totalViviendasMax = 0;

		public static int habitadasMin = 100000;
		public static int habitadasMax = 0;

		public static int deshabitadasMin = 100000;
		public static int deshabitadasMax = 0;

		public static int colectivasMin = 100000;
		public static int colectivasMax = 0;
	}

	public void update() {
		camera.update();
		p5.camera(camera.getCamPosition().x, camera.getCamPosition().y, camera.getCamPosition().z, camera.getCamTarget().x, camera.getCamTarget().y, camera.getCamTarget().z, 0, 1, 0);
		// p5.lights();
		cityMap.updated(camera.getCamTarget());

		particleManager.update();
		
		// ENABLING BUILDING DELETION ACCORDING TO CAMERAPOS AND ACTIVE COMUNA (DON'T ERASE PREV BUILDINGS UNTIL YOU'VE ALMOST REACHED ACTIVE COMUNA)
		if (isCameraMoving()) {
			buildingManager.enableRemoval(false);
		} else {
			buildingManager.enableRemoval(true);
		}

		// particleManager.setSpawnAreas(arduino.getManijaStates());

		/*
		 * for (int i = 0; i < activeAreas.length; i++) { if
		 * (arduino.getManijaStates()[i]) { activeAreas[i] = true; } }
		 */

	}

	public void render() {

		// drawCityMap();
		//drawComunas();
		drawAxisGizmo();
		drawCameras();

		p5.translate(masterTranslate.x, masterTranslate.y, masterTranslate.z);
		// rotateX(map(mouseY,0,height,0,TWO_PI));

		// buildingManager.shrinkBuildings(line01);
		buildingManager.render();

		// line01.render();

		particleManager.render();

	}

	private void createComunas(String dataPath) {

		String[] lines = p5.loadStrings(dataPath);

		for (int i = 0; i < lines.length; i++) {
			// CREATE Comuna OBJECT
			Comuna newComuna = new Comuna();

			String[] comunaData = p5.split(lines[i], ',');

			int id = Integer.valueOf(comunaData[0]);
			int viviendasTotal = Integer.parseInt(comunaData[1]);
			int habitadas = Integer.parseInt(comunaData[2]);
			int deshabitadas = Integer.parseInt(comunaData[3]);
			int colectivas = Integer.parseInt(comunaData[4]);
			PImage comunaImage = p5.loadImage("comunas/Comuna" + (i + 1) + ".jpg");

			newComuna.setData(id, viviendasTotal, habitadas, deshabitadas, colectivas);
			newComuna.setImage(comunaImage);

			comunas.add(newComuna);

		}

		// FOR COMUNA STATISTICS - BEGIN --------------

		int[] allViviendasTotal = new int[lines.length];
		int[] allHabitadas = new int[lines.length];
		int[] allDeshabitadas = new int[lines.length];
		int[] allColectivas = new int[lines.length];

		for (int i = 0; i < allViviendasTotal.length; i++) {
			allViviendasTotal[i] = comunas.get(i).getViviendasTotal();
		}
		for (int i = 0; i < allHabitadas.length; i++) {
			allHabitadas[i] = comunas.get(i).getHabitadas();
		}
		for (int i = 0; i < allDeshabitadas.length; i++) {
			allDeshabitadas[i] = comunas.get(i).getDeshabitadas();
		}
		for (int i = 0; i < allColectivas.length; i++) {
			allColectivas[i] = comunas.get(i).getColectivas();
		}

		ComunasGlobalStats.totalViviendasMin = p5.min(allViviendasTotal);
		ComunasGlobalStats.totalViviendasMax = p5.max(allViviendasTotal);
		ComunasGlobalStats.habitadasMin = p5.min(allHabitadas);
		ComunasGlobalStats.habitadasMax = p5.max(allHabitadas);
		ComunasGlobalStats.deshabitadasMin = p5.min(allDeshabitadas);
		ComunasGlobalStats.deshabitadasMax = p5.max(allDeshabitadas);
		ComunasGlobalStats.colectivasMin = p5.min(allColectivas);
		ComunasGlobalStats.colectivasMax = p5.max(allColectivas);

		// FOR COMUNA STATISTICS - END --------------

		// PRINT OUT
		for (Comuna comuna : comunas) {
			p5.println(comuna.getId() + " - " + comuna.getViviendasTotal() + " - " + comuna.getHabitadas() + " - " + comuna.getDeshabitadas() + " - " + comuna.getColectivas());
		}
		p5.print("-");

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

		//cameraAltitude = -600;
		cameras = new ArrayList<Camera>();

		PVector sceneCenter = new PVector();

		// CAM LEFT
		PVector cam01Pos = new PVector(0, cameraAltitude, 0);
		PVector leftCamOffset = PVector.sub(cam01Pos, new PVector(1000, -cameraAltitude, 1000));
		Camera cam01 = new Camera(cam01Pos, leftCamOffset);
		cam01.setName("LEFT");
		cameras.add(cam01);

		// CAM MAIN
		PVector cam02Pos = new PVector(0, cameraAltitude, 0);
		PVector mainCamOffset = PVector.sub(cam02Pos, new PVector(0, 0, 1)); // SI
																				// LA
																				// UBICO
																				// JUSTO
																				// ENCIMA,
																				// FLIPA..!!
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
		// p5.image(mapBack, 0, 0);
		cityMap.render();

		p5.popMatrix();

		p5.popStyle();
	}

	private void drawComunas() {
		
		// ACTIVE COMUNA
		p5.pushMatrix();

		// p5.tint(255);

		p5.translate(cameraPos0.x, 0, cameraPos0.z);
		p5.imageMode(p5.CENTER);

		p5.rotateX(p5.HALF_PI);
		p5.image(comuna0.getImage(), 0, 0);
		p5.text(comuna0.getId(), 0, -2);

		p5.popMatrix();

		// NEXT COMUNA
		p5.pushMatrix();

		// p5.tint(255);

		p5.translate(cameraPos1.x, 0, cameraPos1.z);
		p5.imageMode(p5.CENTER);

		p5.rotateX(p5.HALF_PI);
		p5.image(comuna1.getImage(), 0, 0);
		p5.text(comuna1.getId(), 0, -2);

		p5.popMatrix();

	}
	
	private void switchComunas(){
		
		// CHANGE NEXT COMUNA
		if (activeComuna == 0) {
			comuna1 = comunas.get(p5.floor(p5.random(comunas.size())));
			camera.moveTo(cameraPos1, 10f);
			
			particleManager.setAttractorCenter(cameraPos1);

			activeComuna = 1;
			
		} else {
			comuna0 = comunas.get(p5.floor(p5.random(comunas.size())));
			camera.moveTo(cameraPos0, 10f);
			
			particleManager.setAttractorCenter(cameraPos0);

			activeComuna = 0;

		}
		
		growBuildings();
		
		particleManager.killParticles();

	}
	
	private boolean isCameraMoving(){
		if((camera.getCamPosition().x > (cameraPos1.x - 1)  && activeComuna == 1) || (camera.getCamPosition().x < (cameraPos0.x + 1) && activeComuna == 0)){
			return false;
		} else { 
			return true;
		}
	}

	public void growBuildings() {
		
		PVector growPosition;
		if(activeComuna == 0){
			growPosition = cameraPos0; 
		} else {
			growPosition = cameraPos1;
		}
		buildingManager.triggerGrowBuildings(growPosition);

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
		return cityMap.getMap();
	}

	public void onKeyPressed(char key) {

		if (key == '1') {
			switchToLeftCamera();
		}
		if (key == '2') {
			switchToMainCamera();
		}

		if (key == 'm') {
			switchComunas();
			//camera.moveTo(cameraPos1, 5f);
		}
		if (key == 'c') {
			camera.moveTo(cameraPos0, 10f);
		}
		if (key == 'n') {
			camera.moveTo(new PVector(0, cameraAltitude, 0), 10f);
		}

		if (key == 'b') {
			growBuildings();
		}

		if (key == 'i') {
			cityMap.triggerMask();
		}
	}

	public void onMousePressed() {

		particleManager.enableSpawn(true);
		// TRIGGER USER LINE
		/*
		 * if (p5.mouseX > 900) { PVector firstPoint = new
		 * PVector(camera.getCamTarget().x + 600, 0, camera.getCamTarget().z);
		 * PVector lastPoint = camera.getCamTarget().get();
		 * line01.initialize(firstPoint, lastPoint); // p5.println(p5.mouseX); }
		 * else if (p5.mouseX < 100) { PVector firstPoint = new
		 * PVector(camera.getCamTarget().x - 600, 0, camera.getCamTarget().z);
		 * PVector lastPoint = new PVector(camera.getCamTarget().x,
		 * camera.getCamPosition().y, camera.getCamTarget().z);
		 * line01.initialize(firstPoint, lastPoint); }
		 */

	}

	/*
	 * public void setActiveAreas(boolean[] states){ activeAreas = states; }
	 */

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
