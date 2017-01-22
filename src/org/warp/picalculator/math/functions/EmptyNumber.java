package org.warp.picalculator.math.functions;

import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.PIDisplay;
import org.warp.picalculator.gui.graphicengine.cpu.CPUDisplay;
import org.warp.picalculator.math.Calculator;

public class EmptyNumber implements Function {

	public EmptyNumber(Calculator root) {
		this.root = root;
	}
	
	private final Calculator root;
	
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
	public boolean isSolved() {
		return false;
	}

	@Override
	public void generateGraphics() {

	}

	@Override
	public void draw(int x, int y) {
		PIDisplay.renderer.glDrawStringLeft(x, y, "␀");
	}

	@Override
	public int getWidth() {
		return PIDisplay.renderer.glGetStringWidth(Utils.getFont(small), "␀");
	}

	@Override
	public int getHeight() {
		return Utils.getFont(small).charH;
	}

	@Override
	public int getLine() {
		return Utils.getFont(small).charH/2;
	}

	@Override
	public Calculator getRoot() {
		return root;
	}

	private boolean small = false;
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof EmptyNumber;
	}
}
