package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.cpu.CPUDisplay;
import org.warp.picalculator.math.Calculator;

import com.rits.cloning.Cloner;

public abstract class FunctionTwoValues implements Function {
	public FunctionTwoValues(Calculator root, Function value1, Function value2) {
		this.root = root;
		variable1 = value1;
		variable2 = value2;
	}

	protected abstract Function NewInstance(Calculator root, Function value1, Function value2);

	protected final Calculator root;

	protected Function variable1 = null;
	protected Function variable2 = null;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Function getVariable1() {
		return variable1;
	}

	public void setVariable1(Function value) {
		variable1 = value;
	}

	@Override
	public Calculator getRoot() {
		return root;
	}

	public Function getVariable2() {
		return variable2;
	}

	public void setVariable2(Function value) {
		variable2 = value;
	}

	@Override
	public abstract String getSymbol();

	@Override
	public boolean isSolved() {
		return (variable1.isSolved() & variable2.isSolved()) ? !isSolvable() : false;
	}

	protected abstract boolean isSolvable();

	@Override
	public final ArrayList<Function> solveOneStep() throws Error {
		final boolean solved = variable1.isSolved() & variable2.isSolved();
		ArrayList<Function> result = solved ? solve() : null;;

		if (result == null || result.isEmpty()) {
			result = new ArrayList<>();

			final ArrayList<Function> l1 = new ArrayList<>();
			final ArrayList<Function> l2 = new ArrayList<>();
			if (variable1.isSolved()) {
				l1.add(variable1);
			} else {
				l1.addAll(variable1.solveOneStep());
			}
			if (variable2.isSolved()) {
				l2.add(variable2);
			} else {
				l2.addAll(variable2.solveOneStep());
			}

			final Function[][] results = Utils.joinFunctionsResults(l1, l2);

			for (final Function[] f : results) {
				result.add(NewInstance(root, f[0], f[1]));
			}
		}

		return result;
	}

	protected abstract ArrayList<Function> solve() throws Error;

	@Override
	public void generateGraphics() {
		variable1.setSmall(small);
		variable1.generateGraphics();

		variable2.setSmall(small);
		variable2.generateGraphics();

		width = calcWidth();
		height = calcHeight();
		line = calcLine();
	}

	@Override
	public void draw(int x, int y) {
		final int ln = getLine();
		int dx = 0;
		variable1.draw(dx + x, ln - variable1.getLine() + y);
		dx += 1 + variable1.getWidth();
		if (drawSignum()) {
			Utils.getFont(small).use(DisplayManager.display);
			DisplayManager.renderer.glDrawStringLeft(dx + x, ln - Utils.getFontHeight(small) / 2 + y, getSymbol());
			dx += Utils.getFont(small).getStringWidth(getSymbol());
		}
		variable2.draw(dx + x, ln - variable2.getLine() + y);
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
	public String toString() {
		String val1 = "null";
		String val2 = "null";
		if (variable1 != null) {
			val1 = variable1.toString();
		}
		if (variable2 != null) {
			val2 = variable2.toString();
		}
		return val1 + getSymbol() + val2;
	}

	@Override
	public FunctionTwoValues clone() {
		final Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

	public boolean drawSignum() {
		return true;
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

	protected int calcWidth() {
		return variable1.getWidth() + 1 + (drawSignum() ? Utils.getFont(small).getStringWidth(getSymbol()) : 0) + variable2.getWidth();
	}

	protected int calcHeight() {

		Function tmin = variable1;
		Function tmax = variable1;
		if (tmin == null || variable2.getLine() >= tmin.getLine()) {
			tmin = variable2;
		}
		if (tmax == null || variable2.getHeight() - variable2.getLine() >= tmax.getHeight() - tmax.getLine()) {
			tmax = variable2;
		}
		return tmin.getLine() + tmax.getHeight() - tmax.getLine();
	}

	protected int calcLine() {
		Function tl = variable1;
		if (tl == null || variable2.getLine() >= tl.getLine()) {
			tl = variable2;
		}
		return tl.getLine();
	}

	@Override
	public int hashCode() {
		return variable1.hashCode() + 7 * variable2.hashCode() + 883 * getSymbol().hashCode();
	}

	@Override
	public abstract boolean equals(Object o);
}
