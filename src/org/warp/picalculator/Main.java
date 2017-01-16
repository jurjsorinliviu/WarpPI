package org.warp.picalculator;

import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.screens.KeyboardDebugScreen;
import org.warp.picalculator.screens.LoadingScreen;

import com.pi4j.wiringpi.Gpio;

public class Main {
	public static int[] screenPos = new int[] { 0, 0 };
	public static final int[] screenSize = new int[] { 480, 320 };
	public static final int screenScale = 1;
	public static final boolean zoomed = true;
	public static Main instance;
	public static boolean haxMode = true;

	public Main() throws InterruptedException {
		instance = this;
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		Thread.currentThread().setName("Main thread");
		beforeStart();
		new PIDisplay(new LoadingScreen());
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
		Utils.debugThirdScreen = Utils.debugOn & false;
		PIDisplay.setBrightness(0.5f);
	}

	
	public void afterStart() {
		Keyboard.startKeyboard();
	}
	
	public void beforeShutdown() {
		Keyboard.stopKeyboard();
	}

	public static void main(String[] args) throws InterruptedException {
		new Main();
	}
}
