package org.warp.picalculator.gui.expression;

import org.warp.picalculator.gui.GraphicalElement;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;
import org.warp.picalculator.math.parser.features.interfaces.Feature;

public abstract class Block implements GraphicalElement {
	
	protected boolean small;
	protected int width;
	protected int height;
	protected int line;
	
	/**
	 * 
	 * @param r Graphic Renderer class.
	 * @param x Position relative to the window.
	 * @param y Position relative to the window.
	 * @param small 
	 */
	public abstract void draw(GraphicEngine ge, Renderer r, int x, int y, Caret caret);

	public abstract boolean putBlock(Caret caret, Block newBlock);

	public abstract boolean delBlock(Caret caret);

	@Override
	public abstract void recomputeDimensions();

	public abstract int computeCaretMaxBound();
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLine() {
		return line;
	}

	public boolean isSmall() {
		return small;
	}

	public abstract void setSmall(boolean small);
	
	public abstract int getClassID();
}
