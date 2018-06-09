package org.warp.picalculator.device;

import org.warp.picalculator.ConsoleUtils;
import org.warp.picalculator.StaticVars;
import org.warp.picalculator.deps.DGpio;
import org.warp.picalculator.gui.HardwareDisplay;

public class PIHardwareDisplay implements HardwareDisplay {

	@Override
	public void initialize() {}

	@Override
	public void shutdown() {}

	@Override
	public void setBrightness(double value) {
		if (StaticVars.debugOn == false) {
			DGpio.pwmWrite(12, (int) Math.ceil(value * 1024f));
//			SoftPwm.softPwmWrite(12, (int)(Math.ceil(brightness*10)));
		} else {
			ConsoleUtils.out.println(1, "Brightness: " + value);
		}
	}

}
