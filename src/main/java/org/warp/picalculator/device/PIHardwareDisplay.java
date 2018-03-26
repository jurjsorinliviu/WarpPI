package org.warp.picalculator.device;

import org.warp.picalculator.StaticVars;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.HardwareDisplay;
import com.pi4j.wiringpi.Gpio;

public class PIHardwareDisplay implements HardwareDisplay {

	@Override
	public void initialize() {}

	@Override
	public void shutdown() {}

	@Override
	public void setBrightness(double value) {
		if (StaticVars.debugOn == false) {
			Gpio.pwmWrite(12, (int) Math.ceil(value * 1024f));
//			SoftPwm.softPwmWrite(12, (int)(Math.ceil(brightness*10)));
		} else {
			Utils.out.println(1, "Brightness: " + value);
		}
	}

}
