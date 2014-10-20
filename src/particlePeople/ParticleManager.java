package particlePeople;

import java.util.ArrayList;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class ParticleManager {

	Main p5;

	ArrayList<Particle> particles;
	ArrayList<Attractor> attractors;

	Attractor deathAttractor;

	PVector screenCenter;

	boolean enableSpawn;
	boolean areDying;

	boolean[] spawnAreas;
	int spawnAreaCounter;

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

		spawnAreas = new boolean[6];
		for (int i = 0; i < spawnAreas.length; i++) {
			spawnAreas[i] = false;
		}
		spawnAreaCounter = 0;
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

		p5.translate(0, -10, 0);
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

		p5.popMatrix();
	}

	public void setSpawnAreas(boolean[] areas) {
		spawnAreas = areas;
	}

	private void spawn() {

		if (p5.frameCount % 10 == 0 && particles.size() < 200) {
			Particle particle = new Particle();

			int attractorToAssign = p5.floor(p5.random(attractors.size()));
			particle.assignAttractor(attractors.get(attractorToAssign));

			particles.add(particle);
		}
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
		areDying = false;
		p5.println(":: Particle Spawn Enabled :: ");
	}

	public void killParticles() {
		areDying = true;
		for (Particle particle : particles) {
			particle.assignAttractor(deathAttractor);
		}
	}

	public void onKeyPressed(char key) {

	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
