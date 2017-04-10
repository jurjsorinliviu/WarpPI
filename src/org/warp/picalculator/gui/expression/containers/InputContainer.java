package org.warp.picalculator.gui.expression.containers;

import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.warp.picalculator.gui.GraphicalElement;
import org.warp.picalculator.gui.expression.Block;
import org.warp.picalculator.gui.expression.BlockContainer;
import org.warp.picalculator.gui.expression.Caret;
import org.warp.picalculator.gui.expression.CaretState;
import org.warp.picalculator.gui.expression.layouts.InputLayout;
import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Renderer;

import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class InputContainer implements GraphicalElement, InputLayout, Serializable {
	private static final long serialVersionUID = 923589369317765667L;
	private final BlockContainer root;
	private Caret caret;
	private static final float CARET_DURATION = 0.5f;
	private float caretTime;
	private int maxPosition = 0;
	private boolean parsed = false;

	public InputContainer() {
		caret = new Caret(CaretState.VISIBLE_ON, 0);
		root = new BlockContainer(false, false);
	}

	public InputContainer(boolean small) {
		caret = new Caret(CaretState.VISIBLE_ON, 0);
		root = new BlockContainer(small, false);
	}

	public InputContainer(boolean small, int minWidth, int minHeight) {
		caret = new Caret(CaretState.VISIBLE_ON, 0);
		root = new BlockContainer(small, false);
	}

	public void typeChar(char c) {
		final Block b = parseChar(c);
		if (b != null) {
			caret.resetRemaining();
			if (root.putBlock(caret, b)) {
				caret.setPosition(caret.getPosition() + 1);
				maxPosition = root.computeCaretMaxBound();
				root.recomputeDimensions();
			}
		}
		caretTime = 0;
		caret.turnOn();
	}

	public void typeChar(String c) {
		typeChar(c.charAt(0));
	}

	public void del() {
		caret.resetRemaining();
		if (root.delBlock(caret)) {
			root.recomputeDimensions();
		}
		if (caret.getPosition() > 0) {
			caret.setPosition(caret.getPosition() - 1);
			maxPosition = root.computeCaretMaxBound();
		}
		caret.turnOn();
		caretTime = 0;
	}

	public void moveLeft() {
		final int curPos = caret.getPosition();
		if (curPos > 0) {
			caret.setPosition(curPos - 1);
		}
		caret.turnOn();
		caretTime = 0;
	}

	public void moveRight() {
		final int curPos = caret.getPosition();
		if (curPos + 1 < maxPosition) {
			caret.setPosition(curPos + 1);
		}
		caret.turnOn();
		caretTime = 0;
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
	 * @return true if something changed
	 */
	public boolean beforeRender(float delta) {
		boolean somethingChanged = false;
		caretTime += delta;
		if (caretTime >= CARET_DURATION) {
			while (caretTime >= CARET_DURATION) {
				caretTime -= CARET_DURATION;
				caret.flipState();
				somethingChanged = true;
			}
		}

		return somethingChanged;
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
		caret.resetRemaining();
		root.draw(ge, r, x, y, caret);
	}

	public void clear() {
		caret = new Caret(CaretState.VISIBLE_ON, 0);
		root.clear();
		maxPosition = root.computeCaretMaxBound();
		recomputeDimensions();
	}

	public boolean isEmpty() {
		return maxPosition <= 1;
	}

	public int getCaretMaxPosition() {
		return maxPosition;
	}

	public void setCaretPosition(int pos) {
		if (pos > 0 && pos < maxPosition) {
			caret.setPosition(pos);
		}
		caret.turnOn();
		caretTime = 0;
	}

	public void setParsed(boolean parsed) {
		this.parsed = parsed;
	}

	public boolean isAlreadyParsed() {
		return parsed;
	}

	/**
	 * <strong>WARNING! DO NOT MODIFY THIS ARRAY!!!</strong>
	 * 
	 * @return an arraylist representing the content
	 */
	public ObjectArrayList<Block> getContent() {
		return root.getContent();
	}
}
