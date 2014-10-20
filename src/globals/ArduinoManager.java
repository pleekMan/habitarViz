package globals;

import cc.arduino.*;

public class ArduinoManager {

	Main p5;
	
	Arduino arduino;
	int threshold = 300;

	
	public ArduinoManager(){
		p5 = getP5();
		
		p5.println(Arduino.list());
		arduino = new Arduino(p5, Arduino.list()[4], 57600);
		for (int i = 0; i < 6; i++) {
			arduino.pinMode(i, Arduino.INPUT);
		}
		
	}
	
	public boolean[] getManijaStates() {
		boolean[] states = new boolean[6];
		for (int i = 0; i < states.length; i++) {
			states[i] = arduino.analogRead(i) > threshold ? true : false;
		}
		return states;
	}
	
	// P5 SINGLETON
		protected Main getP5() {
			return PAppletSingleton.getInstance().getP5Applet();
		}
}
