package org.warp.picalculator;

import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.screens.LoadingScreen;
import org.warp.picalculator.gui.screens.Screen;

import com.pi4j.wiringpi.Gpio;

public class Main {
	public static int[] screenPos = new int[] { 0, 0 };
	public static final int[] screenSize = new int[] { 480, 320 };
	public static final boolean zoomed = true;
	public static Main instance;
	public static boolean haxMode = true;
	public static String[] args;
	public static final String calculatorName = "WarpPI";
	public static final String calculatorNameLOWER = "warppi";
	public static final String calculatorNameUPPER = "WARPPI";

	public Main(String[] args) throws InterruptedException {
		this(new LoadingScreen(), args);
	}

	public Main(Screen screen, String[] args) {
		instance = this;
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		Thread.currentThread().setName("Main thread");
		Main.args = args;
		beforeStart();
		new DisplayManager(screen);
		Utils.debug.println("Shutdown...");
		beforeShutdown();
		Utils.debug.println("");
		Utils.debug.println("Closed.");
		System.exit(0);
	}

	public void beforeStart() {
		if (Utils.isRunningOnRaspberry() && !Utils.isInArray("-noraspi", args)) {
			Gpio.wiringPiSetupPhys();
			Gpio.pinMode(12, Gpio.PWM_OUTPUT);
		} else {
			screenPos = new int[] { 0, 0 };
			Utils.debugOn = true;
		}
		Utils.debugThirdScreen = Utils.debugOn & false;
		DisplayManager.setBrightness(0.5f);
		Keyboard.startKeyboard();
	}

	public void beforeShutdown() {
		Keyboard.stopKeyboard();
	}

	public static void main(String[] args) throws InterruptedException {
		new Main(args);
	}
}
