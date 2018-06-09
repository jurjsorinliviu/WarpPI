package org.warp.picalculator.gui;

public interface HardwareDisplay {
	public void initialize();

	public void shutdown();

	public void setBrightness(double value);
}
