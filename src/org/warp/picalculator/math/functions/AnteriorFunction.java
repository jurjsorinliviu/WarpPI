package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;
import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;

import java.math.BigInteger;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;

import com.rits.cloning.Cloner;

public abstract class AnteriorFunction implements Function {
	public AnteriorFunction(Function parent, Function value) {
		setParent(parent);
		setVariable(value);
	}

	protected abstract Function NewInstance(Function parent2, Function f);
	
	protected Function parent;
	protected Function variable = new Number(null, BigInteger.ZERO);
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

	public Function getParent() {
		return parent;
	}

	public Function setParent(Function value) {
		parent = value;
		return this;
	}

	@Override
	public abstract String getSymbol();

	@Override
	public final ArrayList<Function> solveOneStep() throws Error {
		final boolean solved = variable.isSolved();
		ArrayList<Function> result = solved?solve():null;
		
		if (result == null || result.isEmpty()) {
			result = new ArrayList<>();
			
			ArrayList<Function> l1 = new ArrayList<Function>();
			if (variable.isSolved()) {
				l1.add(variable);
			} else {
				l1.addAll(variable.solveOneStep());
			}
			
			for (Function f : l1) {
				result.add(NewInstance(this.parent, (Function)f));
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
		
		width = glGetStringWidth(Utils.getFont(small), getSymbol()) + 1 + getVariable().getWidth();
		height = variable.getHeight();
		line = variable.getLine();
	}

	@Override
	public void draw(int x, int y) {
		float h1 = getVariable().getHeight();
		int wsegno = glGetStringWidth(Utils.getFont(small), getSymbol());
		float hsegno = Utils.getFontHeight(small);
		float maxh = getHeight();
		Display.Render.glSetFont(Utils.getFont(small));
		
		glDrawStringLeft(x, (int) Math.floor(y + (maxh - hsegno) / 2), getSymbol());
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
		return getSymbol()+val1;
//		} catch (Error e) {
//			return e.id.toString();
//		}
	}

	@Override
	public AnteriorFunction clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}
	
	@Override
	public int hashCode() {
		return variable.hashCode()+883*getSymbol().hashCode();
	}
	
	@Override
	public abstract boolean equals(Object o);
}
