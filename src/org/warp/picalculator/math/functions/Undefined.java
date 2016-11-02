package org.warp.picalculator.math.functions;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;

public class Undefined implements Function {

	public Undefined(Function parent) {
		setParent(parent);
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
	public boolean isSolved() throws Error {
		return true;
	}

	private int width, height, line;
	private Function parent;
	private boolean small;
	
	@Override
	public void generateGraphics() {
		width = Display.Render.glGetStringWidth("UNDEFINED");
		height = Utils.getFontHeight(small);
		line = height/2;
	}

	@Override
	public void draw(int x, int y) {
		Display.Render.glSetFont(Utils.getFont(small));
		Display.Render.glDrawStringLeft(x, y, "UNDEFINED");
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
	public Function setParent(Function parent) {
		this.parent = parent;
		return this;
	}

	@Override
	public Function getParent() {
		return parent;
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

}
