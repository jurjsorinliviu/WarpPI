package org.warp.picalculator;

import org.warp.device.Keyboard;
import org.warp.device.PIDisplay;
import org.warp.picalculator.screens.EquationScreen;

import com.pi4j.wiringpi.Gpio;

public class Main {
	public static int[] screenPos = new int[] { 55, 0 };
	public static final int[] screenSize = new int[] { 480, 320 };
	public static final int screenScale = 1;
	public static PIDisplay d;
	public static Main instance;

	public Main() throws InterruptedException {
		instance = this;
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		beforeStart();
		d = new PIDisplay(new EquationScreen());
		d.run("Calculator");
		Utils.debug.println("Shutdown...");
		beforeShutdown();
		Utils.debug.println("");
		Utils.debug.println("Closed");
		System.exit(0);
	}
	
	public void beforeStart() {
		if (System.getProperty("os.name").equals("Linux")) {
			Gpio.wiringPiSetupPhys();
			Gpio.pinMode(12, Gpio.PWM_OUTPUT);
		} else {
			screenPos = new int[]{0,0};
			Utils.debugOn = true;
		}
		PIDisplay.setBrightness(0.5f);
	}

	
	public void afterStart() {
		Keyboard.startKeyboard();
	}
	
	public void beforeShutdown() {
		Keyboard.stopKeyboard();
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			Termine t = new Termine("9999.9");
			Termine r = t.calcola();
			System.out.println(t.toString());
			System.out.println(r.toString());
		} catch (Errore e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Main();
	}
}
