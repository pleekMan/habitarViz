package interactionViz;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class UserLine {

	Main p5;

	PVector[] nodes;

	public UserLine() {
		p5 = getP5();

		nodes = new PVector[5];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new PVector();
		}
	}

	public void initialize(PVector first, PVector last) {
		// SET FIRST AND LAST POINT, AND CALCULATE NOISY IN BETWEENS

		nodes[0] = first;
		nodes[nodes.length - 1] = last;

		// float interpolationStep = 1 / (nodes.length - 1);

		for (int i = 1; i < nodes.length - 1; i++) {
			float x = p5.map(i, 0, nodes.length - 1, first.x, last.x);
			float y = p5.map(i, 0, nodes.length - 1, first.y, last.y);
			float z = p5.map(i, 0, nodes.length - 1, first.z, last.z);
			
			x += p5.random(-100,100);
			//y += p5.random(-100,100);
			z += p5.random(-100,100);


			nodes[i].set(x, y, z);
		}

	}

	public void render() {
		// p5.noFill();
		p5.stroke(255, 0, 0);

		for (int i = 0; i < nodes.length; i++) {

			// LINES
			p5.noFill();
			if (i != nodes.length - 1) {
				p5.line(nodes[i].x, nodes[i].y, nodes[i].z, nodes[i + 1].x, nodes[i + 1].y, nodes[i + 1].z);
			}

			// CIRCLES
			p5.pushMatrix();
			
			p5.translate(nodes[i].x, nodes[i].y, nodes[i].z);
			p5.rotateX(p5.HALF_PI);
			
			p5.noFill();
			p5.ellipse(0, 0, 20, 20);
			
			p5.translate(0,0,20);
			p5.fill(255, 0, 0);
			p5.text(i, 10, -10);
			
			p5.popMatrix();

		}
	}
	
	public PVector[] getPoints(){
		return nodes;
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
