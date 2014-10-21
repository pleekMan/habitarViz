package globals;

import java.util.Arrays;

import cc.arduino.*;

public class ArduinoManager {

	Main p5;

	Arduino arduino;
	int threshold = 300;
	boolean[] manijaStates;

	char previousKey;

	public ArduinoManager() {
		p5 = getP5();

		try {
			p5.println(Arduino.list());
			arduino = new Arduino(p5, Arduino.list()[0], 57600);
			for (int i = 0; i < 6; i++) {
				arduino.pinMode(i, Arduino.INPUT);
			}
		} catch (Exception e) {
			System.out.println("No Arduino found, Punk..!!");
			e.printStackTrace();
		}

		manijaStates = new boolean[6];
		Arrays.fill(manijaStates, false);
		previousKey = 'q';

	}

	public boolean[] getManijaStates() {

		for (int i = 0; i < manijaStates.length; i++) {
			manijaStates[i] = arduino.analogRead(i) > threshold ? true : false;
		}
		return manijaStates;
	}

	public boolean[] simulateManijaStates() {

		char key = p5.key;

		if (key != previousKey) {
			if (key == 'q') {
				manijaStates[0] = !manijaStates[0];
				p5.println(manijaStates);
			}
			if (key == 'w') {
				manijaStates[1] = !manijaStates[1];
				p5.println(manijaStates);

			}
			if (key == 'e') {
				manijaStates[2] = !manijaStates[2];
				p5.println(manijaStates);

			}
			if (key == 'a') {
				manijaStates[3] = !manijaStates[3];
				p5.println(manijaStates);

			}
			if (key == 's') {
				manijaStates[4] = !manijaStates[4];
				p5.println(manijaStates);

			}
			if (key == 'd') {
				manijaStates[5] = !manijaStates[5];
				p5.println(manijaStates);

			}
			previousKey = key;
		}


		// p5.println(" - " + key);

		return manijaStates;
	}

	public boolean manijasAreTouched() {

		boolean areBeingTouched = false;

		for (int i = 0; i < manijaStates.length; i++) {
			areBeingTouched |= manijaStates[i];
		}

		return areBeingTouched;
	}
	
	public void resetManijas(){
		Arrays.fill(manijaStates, false);
	}

	// P5 SINGLETON
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
