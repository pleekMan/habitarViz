package globals;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class CityMap {

	Main p5;

	PImage cityImage;
	PGraphics cityMask;
	PImage finalCityImage;

	private boolean isMasking;
	float maskSize = 0;

	public CityMap() {
		p5 = getP5();

		cityImage = p5.loadImage("BuenosAires_alpha_Black.png");
		cityMask = p5.createGraphics(500, 500, p5.P2D);
		finalCityImage = p5.createImage(cityImage.width, cityImage.height, p5.ARGB);
		for (int i = 0; i < finalCityImage.pixels.length; i++) {
			finalCityImage.pixels[i] = p5.color(0,0);
		}
		
		cityMask.beginDraw();
		cityMask.background(0);
		cityMask.endDraw();

		isMasking = false;
		maskSize = 0;
	}

	public void updated(PVector camTargetPos) {

		if (maskSize < cityMask.width && isMasking) {

			maskSize++;
			
			cityMask.beginDraw();

			cityMask.fill(255);
			cityMask.ellipse(cityMask.width * 0.5f, cityMask.height * 0.5f, maskSize, maskSize);

			cityMask.endDraw();
			
			finalCityImage.loadPixels();
			
			//p5.println(maskSize);
			
			
			for (int y = 0; y < cityMask.height; y++) {
				//p5.println(y);
				for (int x = 0; x < cityMask.width; x++) {
					//p5.println(x);

					p5.println(x + (y * cityMask.width));

					if(p5.red(cityMask.pixels[x + (y * cityMask.width)]) >= 255){
						p5.println("- " + x + (y * cityMask.width));
						//int xOffset = (int)(x + (camTargetPos.x - 500));
						//int yOffset = (int)(y + (camTargetPos.z - 500));
						
						//finalCityImage.set(xOffset, yOffset, p5.color(cityImage.get(xOffset, yOffset)));
						//finalCityImage.set(x, y, cityImage.get(x, y));
						finalCityImage.pixels[x + (y * finalCityImage.width)] = p5.color(0,255);
					}
				}
			}
			finalCityImage.updatePixels();
			
		}
	}

	public void render() {

		
		
		
		p5.image(cityImage, 0, 0);
		
		p5.fill(255);
		p5.stroke(0);
		
		p5.pushMatrix();
		p5.translate(200, 200, 10);
		p5.rotateX(p5.HALF_PI);
		
		p5.beginShape();
		
		p5.vertex(0, 0, 0);
		p5.vertex(5000, 0,0);
		p5.vertex(5000, 0, 5000);
		p5.vertex(0, 0, 5000);
		
		p5.beginContour();
		
		p5.vertex(p5.mouseX + 25, 0, p5.mouseY + 25);
		p5.vertex(p5.mouseX + 25, 0,p5.mouseY + 75);
		p5.vertex(p5.mouseX + 75, 0, p5.mouseY + 75);
		p5.vertex(p5.mouseX + 75, 0, p5.mouseY + 25);
		
		p5.endContour();
		p5.endShape(p5.CLOSE);
		
		
		p5.popMatrix();
		//p5.pushMatrix();
		
		//p5.translate(0, 0, -10);
		//p5.rotateX(-p5.HALF_PI);

		//p5.image(cityMask, 0, 0);
		
		//p5.popMatrix();
	}

	public PImage getMap() {
		return cityImage;
	}

	public void triggerMask() {
		isMasking = true;
		maskSize = 0;

		cityMask.beginDraw();
		cityMask.background(0);
		cityMask.endDraw();

	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
