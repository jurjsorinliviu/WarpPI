package org.warp.picalculator;

import java.io.IOException;

import org.warp.picalculator.deps.DGpio;
import org.warp.picalculator.deps.DSystem;
import org.warp.picalculator.device.HardwareDevice;
import org.warp.picalculator.device.HardwareTouchDevice;
import org.warp.picalculator.device.InputManager;
import org.warp.picalculator.device.Keyboard;
import org.warp.picalculator.device.PIHardwareDisplay;
import org.warp.picalculator.device.PIHardwareTouchDevice;
import org.warp.picalculator.gui.CalculatorHUD;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.HUD;
import org.warp.picalculator.gui.HardwareDisplay;
import org.warp.picalculator.gui.screens.LoadingScreen;
import org.warp.picalculator.gui.screens.Screen;
import org.warp.picalculator.math.rules.RulesManager;

public class Main {
	public static Main instance;
	public static String[] args;

	public Main(String[] args) throws InterruptedException, Error, IOException {
		this(new LoadingScreen(), new PIHardwareDisplay(), new CalculatorHUD(), args);
	}

	public Main(Screen screen, HardwareDisplay disp, HUD hud, String[] args) throws InterruptedException, Error, IOException {
		System.out.println("WarpPI Calculator");
		instance = this;
		Main.args = args;
//		ClassUtils.classLoader = this.getClass();
		beforeStart();
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		PlatformUtils.setThreadName(Thread.currentThread(), "Main thread");
		DisplayManager dm = new DisplayManager(disp, hud, screen, "WarpPI Calculator by Andrea Cavalli (@Cavallium)");
		Keyboard k = new Keyboard();
		HardwareTouchDevice touch = new PIHardwareTouchDevice(false,false,false);
		InputManager im = new InputManager(k, touch);
		HardwareDevice hardwareDevice = new HardwareDevice(dm, im);
		hardwareDevice.setup(() -> {
			try {
				HardwareDevice.INSTANCE.getDisplayManager().setBrightness(0.2f);
				RulesManager.initialize();
				RulesManager.warmUp();
				if (screen instanceof LoadingScreen) {
					((LoadingScreen) screen).loaded = true;
				}
				HardwareDevice.INSTANCE.getDisplayManager().waitForExit();
			} catch (InterruptedException | Error e) {
				e.printStackTrace();
			}
			ConsoleUtils.out.println(1, "Shutdown...");
			beforeShutdown();
			ConsoleUtils.out.println(1, "");
			ConsoleUtils.out.println(1, "Closed.");
			DSystem.exit(0);
		});
	}

	public void beforeStart() throws IOException {
		boolean isRaspi = false;
		try {
			isRaspi = DGpio.getBoardType() != DGpio.UnknownBoardType;
		} catch (final Exception e) {}
		if (Utils.isRunningOnRaspberry() && !Utils.isInArray("-noraspi", args) && isRaspi) {
			DGpio.wiringPiSetupPhys();
			DGpio.pinMode(12, DGpio.PWM_OUTPUT);
		} else {
			StaticVars.screenPos = new int[] { 0, 0 };
			StaticVars.debugOn = true;
		}
		Utils.debugThirdScreen = StaticVars.debugOn & false;
		for (final String arg : args) {
			if (arg.equalsIgnoreCase("2x")) {
				StaticVars.debugWindow2x = true;
			}
			if (arg.equalsIgnoreCase("headless")) {
				Utils.headlessOverride = true;
			}
			if (arg.equalsIgnoreCase("headless-8")) {
				Utils.headlessOverride = true;
				Utils.forceEngine = "console-8";
			}
			if (arg.equalsIgnoreCase("headless-256")) {
				Utils.headlessOverride = true;
				Utils.forceEngine = "console-256";
			}
			if (arg.equalsIgnoreCase("headless-24bit")) {
				Utils.headlessOverride = true;
				Utils.forceEngine = "console-24bit";
			}
			if (arg.equalsIgnoreCase("cpu")) {
				Utils.forceEngine = "cpu";
			}
			if (arg.equalsIgnoreCase("gpu")) {
				Utils.forceEngine = "gpu";
			}
			if (arg.equalsIgnoreCase("fb")) {
				Utils.forceEngine = "fb";
			}
			if (arg.equalsIgnoreCase("nogui")) {
				Utils.forceEngine = "nogui";
			}
			if (arg.equalsIgnoreCase("html")) {
				Utils.forceEngine = "html";
			}
			if (arg.equalsIgnoreCase("verbose") || arg.equalsIgnoreCase("debug")) {
				StaticVars.outputLevel = ConsoleUtils.OUTPUTLEVEL_DEBUG_VERBOSE;
			}
			if (arg.equalsIgnoreCase("uncached")) {
				Utils.debugCache = true;
			}
			if (arg.equalsIgnoreCase("ms-dos")) {
				Utils.headlessOverride = true;
				Utils.msDosMode = true;
			}
		}
	}

	public void beforeShutdown() {
		Keyboard.stopKeyboard();
	}

	public static void main(String[] args) throws InterruptedException, Error, IOException {
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
