package org.warp.picalculator.math.functions;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine;
import org.warp.picalculator.math.Calculator;

import com.rits.cloning.Cloner;

public abstract class AnteriorFunction implements Function {
	public AnteriorFunction(Function value) {
		root = value.getRoot();
		variable = value;
	}

	public AnteriorFunction(Calculator root, Function value) {
		this.root = root;
		variable = value;
	}

	protected abstract Function NewInstance(Calculator root, Function value);

	protected final Calculator root;
	protected Function variable;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;

	public Function getVariable() {
		return variable;
	}

	public void setVariable(Function value) {
		variable = value;
	}

	@Override
	public Calculator getRoot() {
		return root;
	}

	@Override
	public abstract String getSymbol();

	@Override
	public final ArrayList<Function> solveOneStep() throws Error {
		final boolean solved = variable.isSolved();
		ArrayList<Function> result = solved ? solve() : null;

		if (result == null || result.isEmpty()) {
			result = new ArrayList<>();

			final ArrayList<Function> l1 = new ArrayList<>();
			if (variable.isSolved()) {
				l1.add(variable);
			} else {
				l1.addAll(variable.solveOneStep());
			}

			for (final Function f : l1) {
				result.add(NewInstance(root, f));
			}
		}

		return result;
	}

	protected abstract ArrayList<Function> solve() throws Error;

	@Override
	public boolean isSolved() {
		return variable.isSolved() ? !isSolvable() : false;
	}

	protected abstract boolean isSolvable();

	@Override
	public void generateGraphics() {
		variable.setSmall(small);
		variable.generateGraphics();

		width = Utils.getFont(small).getStringWidth(getSymbol()) + 1 + getVariable().getWidth();
		height = variable.getHeight();
		line = variable.getLine();
	}

	@Override
	public void draw(int x, int y) {
		final float h1 = getVariable().getHeight();
		final int wsegno = Utils.getFont(small).getStringWidth(getSymbol());
		final float hsegno = Utils.getFontHeight(small);
		final float maxh = getHeight();
		Utils.getFont(small).use(DisplayManager.engine);

		DisplayManager.renderer.glDrawStringLeft(x, (int) Math.floor(y + (maxh - hsegno) / 2), getSymbol());
		getVariable().draw(x + wsegno + 1, (int) Math.floor(y + (maxh - h1) / 2));
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
//		try {
//			return solve().toString();
		String val1 = "null";
		if (variable != null) {
			val1 = variable.toString();
		}
		return getSymbol() + val1;
//		} catch (Error e) {
//			return e.id.toString();
//		}
	}

	@Override
	public AnteriorFunction clone() {
		final Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}

	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

	@Override
	public int hashCode() {
		return variable.hashCode() + 883 * getSymbol().hashCode();
	}

	@Override
	public abstract boolean equals(Object o);
}
