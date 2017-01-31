package org.warp.picalculator.device.chip;

import com.pi4j.wiringpi.Gpio;

public class SerialToParallel {
	private final int RCK; //Storage register clock pin (latch pin)
	private final int SCK; //Shift register clock pin
	private final int SER; //Serial data input

	public SerialToParallel(int RCK_pin, int SCK_pin, int SER_pin) {
		RCK = RCK_pin;
		SCK = SCK_pin;
		SER = SER_pin;
	}

	public void write(boolean[] data) {
		if (data.length != 8) {
			return;
		} else {
			Gpio.digitalWrite(RCK, Gpio.LOW);

			for (int i = 7; i >= 0; i--) {
				Gpio.digitalWrite(SCK, Gpio.LOW);
				Gpio.digitalWrite(SER, data[i]);
				Gpio.digitalWrite(SCK, Gpio.HIGH);
			}

			Gpio.digitalWrite(RCK, Gpio.HIGH);
		}
	}
}
