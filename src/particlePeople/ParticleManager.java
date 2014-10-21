package particlePeople;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PVector;
import globals.Camera;
import globals.Main;
import globals.PAppletSingleton;

public class ParticleManager {

	Main p5;

	public ArrayList<Particle> particles;
	ArrayList<Attractor> attractors;

	Attractor deathAttractor;

	PVector screenCenter;

	boolean enableSpawn;
	boolean areDying;

	Camera camera;

	boolean[] spawnAreasControl;
	int spawnAreaCounter;
	PVector[] spawnPoints;
	

	public ParticleManager() {
		p5 = getP5();

		// PASAR TODO DE X-Y A X-Z

		particles = new ArrayList<Particle>();
		attractors = new ArrayList<Attractor>();

		screenCenter = new PVector(0, 0, 0);
		for (int i = 0; i < 5; i++) {
			Attractor newAttractor = new Attractor(screenCenter);
			attractors.add(newAttractor);
		}
		deathAttractor = new Attractor(screenCenter);

		enableSpawn = false;
		areDying = false;

		spawnAreasControl = new boolean[6];
		for (int i = 0; i < spawnAreasControl.length; i++) {
			spawnAreasControl[i] = false;
		}
		spawnAreaCounter = 0;
		
		spawnPoints = new PVector[6];
		for (int i = 0; i < spawnPoints.length; i++) {
			spawnPoints[i] = new PVector();
		}
		
		setSpawnPoints(new PVector(0,0,0));
	}

	public void update() {

		if (enableSpawn)
			spawn();

		// PARTICLES UPDATE
		for (int i = 0; i < particles.size(); i++) {

			Particle currentParticle = particles.get(i);

			if (!areDying) {
				if (p5.frameCount % 60 == 0) {
					reAssignAttractor(currentParticle);
				}
			}

			currentParticle.update();

		}

		// ATTRACTOR UPDATE
		for (int i = 0; i < attractors.size(); i++) {
			Attractor currentAttractor = attractors.get(i);
			currentAttractor.update();
		}
	}

	public void render() {

		p5.pushMatrix();

		p5.translate(0, 10, 0);
		p5.rotateX(-p5.HALF_PI);
		// PARTICLES RENDER
		for (int i = 0; i < particles.size(); i++) {
			Particle currentParticle = particles.get(i);
			currentParticle.render();
		}

		for (int i = 0; i < attractors.size(); i++) {
			Attractor currentAttractor = attractors.get(i);
			currentAttractor.render();
		}
		
		for (int i = 0; i < spawnPoints.length; i++) {
			p5.ellipse(spawnPoints[i].x, spawnPoints[i].y, 50, 50);;
			p5.text(i, spawnPoints[i].x, spawnPoints[i].y);;
		}

		p5.popMatrix();
	}

	public void enableSpawnPoints(boolean[] areas) {
		spawnAreasControl = areas;
	}

	private void spawn() {

		if (p5.frameCount % 10 == 0 && particles.size() < 200) {
			
			
			PVector spawnPoint = assignSpawnPoint();
			spawnPoint.z = 0;
			
			Particle particle = new Particle(spawnPoint);

			int attractorToAssign = p5.floor(p5.random(attractors.size()));
			particle.assignAttractor(attractors.get(attractorToAssign));

			particles.add(particle);
			
			spawnAreaCounter++;
		}
	}

	private PVector assignSpawnPoint() {
		
		for (int i = 0; i < 15; i++) {
			int selectedPoint = p5.floor(p5.random(spawnPoints.length));
			if (spawnAreasControl[selectedPoint] == true) {
				return spawnPoints[selectedPoint].get(); // IF I DON'T GET A COPY, THE ACTUAL SPAWN POINTS GOES WITH THE PARTICLE
			}
		}
		
		return new PVector();
	}

	private void reAssignAttractor(Particle particle) {
		particle.assignAttractor(attractors.get(p5.floor(p5.random(attractors.size()))));
	}
	

	public void setAttractorCenter(PVector center) {
		for (Attractor currentAttractor : attractors) {
			currentAttractor.setCenter(center);
		}
	}

	public void setDeathAttractorCenter(PVector center) {
		deathAttractor.setCenter(center);
	}

	public void enableSpawn(boolean state) {
		enableSpawn = state;
		//Arrays.fill(spawnAreasControl, false);
		areDying = !enableSpawn;
		//p5.println(":: Particle Spawn: " + enableSpawn);
	}

	public void killParticles() {
		enableSpawn = false;
		spawnAreaCounter = 0;
		areDying = true;
		
		particles.clear();
		/*
		for (Particle particle : particles) {
			particle.assignAttractor(deathAttractor);
		}
		*/
	}
	
	public void setCamera(Camera cam){
		camera = cam;
	}
	
	public void setSpawnPoints(PVector comunaCenter){
		
		int offset = 300;
		
		spawnPoints[0].set(comunaCenter.x - offset, comunaCenter.z - offset, comunaCenter.y);
		spawnPoints[1].set(comunaCenter.x, comunaCenter.z - offset, comunaCenter.y);
		spawnPoints[2].set(comunaCenter.x + offset, comunaCenter.z - offset, comunaCenter.y);
		spawnPoints[3].set(comunaCenter.x - offset, comunaCenter.z + offset, comunaCenter.y);
		spawnPoints[4].set(comunaCenter.x, comunaCenter.z + offset, comunaCenter.y);
		spawnPoints[5].set(comunaCenter.x + offset, comunaCenter.z + offset, comunaCenter.y);


	}
	
	public ArrayList<Particle> getParticles(){
		return particles;
	}

	public void onKeyPressed(char key) {

	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
