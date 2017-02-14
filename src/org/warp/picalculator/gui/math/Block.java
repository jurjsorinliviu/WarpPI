package org.warp.picalculator.gui.math;

public abstract class Block {
	/**
	 * 
	 * @param x Position relative to the window.
	 * @param y Position relative to the window.
	 * @param c Parent container.
	 */
	public abstract void draw(int x, int y, Container c);
}
