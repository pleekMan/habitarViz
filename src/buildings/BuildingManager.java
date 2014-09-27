package buildings;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PVector;
import globals.Camera;
import globals.Main;
import globals.PAppletSingleton;

public class BuildingManager {

	Main p5;

	ArrayList<Building> buildings;
	
	int buildingGrowCount;
	float growingAreaRadius;

	public BuildingManager() {
		p5 = getP5();

		buildings = new ArrayList<Building>();
		
		/*
		// BUILDING AT ORIGIN
		Building building0 = new Building();
		building0.setPosition(new PVector(0, 0, 0));
		buildings.add(building0);
		*/
		
		/*
		 * for (int i = 0; i < 5; i++) { Building building = new Building(this);
		 * building.setPosition(new PVector(random(width), random(height),
		 * random(-300))); buildings.add(building); }
		 */
		
		buildingGrowCount = 100;
		growingAreaRadius = 100;


	}

	public void update() {

	}

	public void render() {

		// USING AN ITERATOR TO GO THROUGH THE BUILDINGS.
		// IT'S REMOVE() METHOD AVOIDS CONCURRENT MODIFICATION ON ARRAYLIST SIZE
		// IF A ERASE A BUILDINNG ON THE FLY, IT WILL NOT THROW A NULL POINTER
		// EXCEPTION
		Iterator<Building> buildingIterator = buildings.iterator();

		while (buildingIterator.hasNext()) {
			Building actualBuilding = buildingIterator.next();

			//actualBuilding.setScale(p5.norm(p5.mouseY, 0, p5.height));
			actualBuilding.render();

			if (checkBuildingOffScreen(actualBuilding)) {
				buildingIterator.remove();
				// buildings.remove(actualBuilding);
			}
		}

	}
	
	public void triggerGrowBuildings(Camera camera) {

		int count = (int) p5.random(10, 20);
		float offset = 500f;

		for (int i = 0; i < buildingGrowCount; i++) {

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
	
	public void setGrowingAreaRadius(float radius){
		growingAreaRadius = radius;
	}
	public float getGrowingAreaRadius(){
		return growingAreaRadius;
	}
	
	private boolean checkBuildingOffScreen(Building building) {
		
		float screenX = p5.screenX(building.getPosition().x, building.getPosition().y, building.getPosition().z);
		float screenY = p5.screenY(building.getPosition().x, building.getPosition().y, building.getPosition().z);
		//p5.println("ScreenX: " + screenX + " / ScreenY: " + screenY);
		
		/* DRAW COORDINATES
		p5.pushMatrix();
		p5.translate(building.getPosition().x + 50, building.getPosition().y, building.getPosition().z + 50);
		p5.rotateY(p5.QUARTER_PI * 0.5f);
		p5.textSize(20);
		p5.fill(255,0,0);
		p5.text((int)screenX + " | " + (int)screenY + " | " + (int)building.getHeight(), 0, 0);
		p5.popMatrix();
		*/
		if(screenX < 0 || screenX > p5.width || screenY < 0 || screenY > p5.height){
			return true;
		} else {
			return false;
		}
		
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
