package globals;

import de.looksgood.ani.Ani;
import processing.core.PVector;

public class Camera {

	Main p5;

	//PVector camPos;
	float camX, camY, camZ;
	PVector targetPos;
	PVector targetPosOffset;
	String name;

	PVector moveToPos;
	Ani animation;
	float animationPos;
	
	boolean lockCamTarget;

	public Camera(PVector _camPos, PVector _target) {
		p5 = getP5();
		Ani.init(p5);
		
		//camPos = _camPos;
		camX = _camPos.x;
		camY = _camPos.y;
		camZ = _camPos.z;
		
		targetPos = _target;
		targetPosOffset = targetPos.get();
		name = "No Name";

		moveToPos = new PVector(0, 0, -100);
		animationPos = 0;
		//animation = new Ani(this, 2.0f, "animationPos", 1);
		Ani.to(this, 2.0f, "camX", -100);
		
		lockCamTarget = true;

		//animation.seek(0f);
		//animation.resume();
		//animation.start();
		//SceneManager.Ani.to(this, 2.0f, "animationPos", 1);
	}

	public void update() {
		
		if (lockCamTarget) {
			targetPos.set(camX - targetPosOffset.x, camY - + targetPosOffset.y, camZ - targetPosOffset.z);
			//p5.println(targetPos);
		}

	}


	
	public void moveTo(PVector target, float duration){
		Ani.to(this, duration, "camX", target.x);
		Ani.to(this, duration, "camY", target.y);
		Ani.to(this, duration, "camZ", target.z);
	}
	

	public void setName(String _name) {
		name = _name;
	}

	String getName() {
		return name;
	}
	
	public void setCameraPosition(PVector position){
		camX = position.x;
		camY = position.y;
		camZ = position.z;
	}

	public PVector getCamPosition() {
		PVector camPosition = new PVector(camX, camY,camZ);
		return camPosition;
	}

	public PVector getCamTarget() {
		return targetPos;
	}

	void drawCameraGizmo() {
		p5.noFill();

		p5.stroke(255, 255, 0);

		// CAM EYE GIZMO
		p5.pushMatrix();
		p5.translate(camX, camY,camZ);
		p5.box(30);
		p5.popMatrix();

		p5.line(camX, camY,camZ, targetPos.x, targetPos.y, targetPos.z);

		// CAM TARGET GIZMO
		p5.pushMatrix();
		p5.translate(targetPos.x, targetPos.y, targetPos.z);
		p5.sphere(20);
		p5.popMatrix();

	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
