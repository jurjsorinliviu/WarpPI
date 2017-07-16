package org.warp.picalculator;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Vector;

import org.nevec.rjm.BigIntegerMath;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.screens.LoadingScreen;
import org.warp.picalculator.gui.screens.Screen;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.functions.Number;

import com.pi4j.system.SystemInfo.BoardType;
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
		Utils.out.println(1, "Shutdown...");
		beforeShutdown();
		Utils.out.println(1, "");
		Utils.out.println(1, "Closed.");
		System.exit(0);
	}

	public void beforeStart() {
		boolean isRaspi = false; try {isRaspi = com.pi4j.system.SystemInfo.getBoardType() != BoardType.UNKNOWN;} catch (Exception e) {}
		if (Utils.isRunningOnRaspberry() && !Utils.isInArray("-noraspi", args) && isRaspi) {
			Gpio.wiringPiSetupPhys();
			Gpio.pinMode(12, Gpio.PWM_OUTPUT);
		} else {
			screenPos = new int[] { 0, 0 };
			Utils.debugOn = true;
		}
		Utils.debugThirdScreen = Utils.debugOn & false;
		for (String arg : args) {
			if (arg.contains("headless")) {
				Utils.headlessOverride = true;
			}
			if (arg.contains("console-8")) {
				Utils.headlessOverride = true;
				Utils.forceEngine = "console-8";
			}
			if (arg.contains("console-256")) {
				Utils.headlessOverride = true;
				Utils.forceEngine = "console-256";
			}
			if (arg.contains("console-24bit")) {
				Utils.headlessOverride = true;
				Utils.forceEngine = "console-24bit";
			}
			if (arg.contains("cpu")) {
				Utils.forceEngine = "cpu";
			}
			if (arg.contains("gpu")) {
				Utils.forceEngine = "gpu";
			}
			if (arg.contains("ms-dos")) {
				Utils.headlessOverride = true;
				Utils.msDosMode = true;
			}
		}
		DisplayManager.setBrightness(0.5f);
		Keyboard.startKeyboard();
	}

	public void beforeShutdown() {
		Keyboard.stopKeyboard();
	}

	public static void main(String[] args) throws InterruptedException {
		/*
		 * TEST: Comparing BigIntegerMath.divisors() vs programmingpraxis' Number.getFactors() function
		 * 
		long time1;
		long time2;
		final int max = 10000;
		final long HCN = 720720L;
		final long LCN = 104911L;
		final BigInteger[] bigintegers = new BigInteger[max];
		bigintegers[0] = BigInteger.valueOf(HCN);
		for (int i = 0; i < max; i++) {
			bigintegers[i] = bigintegers[0];
		}
		final Number[] numbers = new Number[max];
		final MathContext mc = new MathContext();
		numbers[0] = new Number(mc, HCN);
		for (int i = 0; i < max; i++) {
			numbers[i] = numbers[0];
		}
		Vector<BigInteger> empty = null;
		LinkedList<BigInteger> empty2 = null;
		
		time1 = System.currentTimeMillis();
		for(int i = 0; i < max; i++) {
			empty = BigIntegerMath.divisors(bigintegers[i]);
		}
		time2 = System.currentTimeMillis();
		System.out.println("BigIntegerMath HCN: "+(time2-time1)+" ("+empty.toString()+")");
		

		bigintegers[0] = BigInteger.valueOf(LCN);
		for (int i = 0; i < max; i++) {
			bigintegers[i] = bigintegers[0];
		}
		
		time1 = System.currentTimeMillis();
		for(int i = 0; i < max; i++) {
			empty = BigIntegerMath.divisors(bigintegers[i]);
		}
		time2 = System.currentTimeMillis();
		System.out.println("BigIntegerMath LCN: "+(time2-time1)+" ("+empty.toString()+")");
		
		time1 = System.currentTimeMillis();
		for(int i = 0; i < max; i++) {
			empty2 = numbers[i].getFactors();
		}
		time2 = System.currentTimeMillis();
		System.out.println("BigIntegerMath HCN: "+(time2-time1)+" ("+empty2.toString()+")");

		numbers[0] = new Number(mc, LCN);
		for (int i = 0; i < max; i++) {
			numbers[i] = numbers[0];
		}
		time1 = System.currentTimeMillis();
		for(int i = 0; i < max; i++) {
			empty2 = numbers[i].getFactors();
		}
		time2 = System.currentTimeMillis();
		System.out.println("BigIntegerMath LCN: "+(time2-time1)+" ("+empty2.toString()+")");
		if(true) {
			System.exit(0);;
		}
		*/
		new Main(args);
	}
}
