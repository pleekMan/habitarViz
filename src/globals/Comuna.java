package globals;

import processing.core.PImage;

public class Comuna {
	Main p5;

	int id;
	int viviendasTotal;
	int habitadas, deshabitadas;
	int colectivas;
	
	PImage comunaImage;

	public Comuna() {
		p5 = getP5();
		
		id = viviendasTotal = habitadas = deshabitadas = colectivas = 0;
	}

	public void setData(int _id, int _viviendasTotal, int _habitadas, int _deshabitadas, int _colectivas) {
		id = _id;
		viviendasTotal = _viviendasTotal;
		habitadas = _habitadas;
		deshabitadas = _deshabitadas;
		colectivas = _colectivas;
	}
	public void setImage(PImage _image){
		comunaImage = _image;
	}
	
	
	public int getId() {
		return id;
	}

	public int getViviendasTotal(){
		return viviendasTotal;
	}

	public int getHabitadas() {
		return habitadas;
	}
	
	public int getDeshabitadas() {
		return deshabitadas;
	}

	public int getColectivas() {
		return colectivas;
	}

	public PImage getImage() {
		return comunaImage;
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
