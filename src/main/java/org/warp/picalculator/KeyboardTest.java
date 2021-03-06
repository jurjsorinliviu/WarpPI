package org.warp.picalculator;

import java.io.IOException;

import org.warp.picalculator.device.PIHardwareDisplay;
import org.warp.picalculator.gui.CalculatorHUD;
import org.warp.picalculator.gui.screens.KeyboardDebugScreen;

public class KeyboardTest {

	public static void main(String[] args) throws InterruptedException, Error, IOException {
		new Main(new KeyboardDebugScreen(), new PIHardwareDisplay(), new CalculatorHUD(), args);
	}
}
