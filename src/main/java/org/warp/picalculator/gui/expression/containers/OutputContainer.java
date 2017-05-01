package org.warp.picalculator.gui.expression.containers;

import java.io.Serializable;

import org.warp.picalculator.gui.GraphicalElement;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.expression.CaretState;
import org.warp.picalculator.gui.expression.blocks.Block;
import org.warp.picalculator.gui.expression.blocks.BlockContainer;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class OutputContainer implements GraphicalElement, OutputLayout, Serializable {
	private static final long serialVersionUID = -5714825964892683571L;
	public final BlockContainer root;
	private final Caret caret = new Caret(CaretState.HIDDEN, 0);

	public OutputContainer() {
		root = new BlockContainer();
	}

	public OutputContainer(boolean small) {
		root = new BlockContainer(small);
	}

	public OutputContainer(boolean small, int minWidth, int minHeight) {
		root = new BlockContainer(small);
	}
	
	public void setContent(ObjectArrayList<Block> blocks) {
		root.clear();
		for (Block b : blocks) {
			root.appendBlockUnsafe(b);
		}
		recomputeDimensions();
	}

	@Override
	public void recomputeDimensions() {
		root.recomputeDimensions();
	}

	@Override
	public int getWidth() {
		return root.getWidth();
	}

	@Override
	public int getHeight() {
		return root.getHeight();
	}

	@Override
	public int getLine() {
		return root.getLine();
	}

	/**
	 * 
	 * @param delta
	 *            Time, in seconds
	 */
	public void beforeRender(double delta) {

	}

	/**
	 * 
	 * @param ge
	 *            Graphic Engine class.
	 * @param r
	 *            Graphic Renderer class of <b>ge</b>.
	 * @param x
	 *            Position relative to the window.
	 * @param y
	 *            Position relative to the window.
	 */
	public void draw(GraphicEngine ge, Renderer r, int x, int y) {
		root.draw(ge, r, x, y, caret);
	}

	public void clear() {
		root.clear();
		recomputeDimensions();
	}
}
