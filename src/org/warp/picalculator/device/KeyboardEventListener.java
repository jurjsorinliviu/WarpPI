package org.warp.picalculator.device;

import org.warp.picalculator.device.Keyboard.Key;

public interface KeyboardEventListener {
	public default boolean keyPressed(Key k) {
		return false;
	}

	public default boolean keyReleased(Key k) {
		return false;
	}
}
