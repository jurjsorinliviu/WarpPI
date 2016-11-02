package org.warp.picalculator.math.functions;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.device.graphicengine.Display;

public class EmptyNumber implements Function {

	@Override
	public String getSymbol() {
		return " ";
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSolved() throws Error {
		return false;
	}

	@Override
	public void generateGraphics() {

	}

	@Override
	public void draw(int x, int y) {
		Display.Render.glDrawStringLeft(x, y, " ");
	}

	@Override
	public int getWidth() {
		return Display.Render.glGetStringWidth(" ");
	}

	@Override
	public int getHeight() {
		return Display.Render.glGetCurrentFontHeight();
	}

	@Override
	public int getLine() {
		return Display.Render.glGetCurrentFontHeight()/2;
	}

	@Override
	public Function setParent(Function parent) {
		return null;
	}

	@Override
	public Function getParent() {
		return null;
	}

	@Override
	public void setSmall(boolean small) {

	}

}
