package org.warp.picalculator.math.functions;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.math.Calculator;

public class Undefined implements Function {

	protected final Calculator root;

	public Undefined(Calculator root) {
		this.root = root;
	}

	@Override
	public String getSymbol() {
		return "undefined";
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		return null;
	}

	@Override
	public boolean isSolved() {
		return true;
	}

	private int width, height, line;
	private boolean small;

	@Override
	public void generateGraphics() {
		width = Utils.getFont(small).getStringWidth("UNDEFINED");
		height = Utils.getFontHeight(small);
		line = height / 2;
	}

	@Override
	public void draw(int x, int y) {
		Utils.getFont(small).use(DisplayManager.engine);
		DisplayManager.renderer.glDrawStringLeft(x, y, "UNDEFINED");
	}

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

	@Override
	public Calculator getRoot() {
		return root;
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Undefined) {
			return true;
		}
		return false;
	}

}
