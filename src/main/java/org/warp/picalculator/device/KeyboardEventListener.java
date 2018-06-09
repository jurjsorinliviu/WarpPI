package org.warp.picalculator.device;

public interface KeyboardEventListener {
	public abstract boolean keyPressed(Key k);

	public abstract boolean keyReleased(Key k);
}
