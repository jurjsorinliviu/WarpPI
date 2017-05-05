package org.warp.picalculator.device;

import org.warp.picalculator.device.Keyboard.Key;

public interface KeyboardEventListener {
	public abstract boolean keyPressed(Key k);

	public abstract boolean keyReleased(Key k);
}
