package org.warp.picalculator.device;

public interface HardwareTouchDevice {
	public boolean getInvertedXY();
	public boolean getInvertedX();
	public boolean getInvertedY();
	public default void setInvertedXY() {}
	public default void setInvertedX() {}
	public default void setInvertedY() {}
}
