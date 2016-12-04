package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;
import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;

import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;

import com.rits.cloning.Cloner;

public abstract class FunctionTwoValues implements Function {
	public FunctionTwoValues(Function parent, Function value1, Function value2) {
		this.parent = parent;
		variable1 = value1;
		variable2 = value2;
	}
	
	protected abstract Function NewInstance(Function parent2, Function value1, Function value2);

	protected Function parent;
	
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
		value.setParent(this);
		variable1 = value;
	}

	public Function getParent() {
		return parent;
	}

	public Function setParent(Function value) {
		parent = value;
		return this;
	}

	public Function getVariable2() {
		return variable2;
	}

	public void setVariable2(Function value) {
		value.setParent(this);
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
		ArrayList<Function> result = solved?solve():null;;
		
		if (result == null || result.isEmpty()) {
			result = new ArrayList<>();
			
			ArrayList<Function> l1 = new ArrayList<Function>();
			ArrayList<Function> l2 = new ArrayList<Function>();
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
			
			Function[][] results = Utils.joinFunctionsResults(l1, l2);
			
			for (Function[] f : results) {
				result.add(NewInstance(this.parent, (Function)f[0], (Function)f[1]));
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
		int ln = getLine();
		int dx = 0;
		variable1.draw(dx + x, ln - variable1.getLine() + y);
		dx += 1+variable1.getWidth();
		if (drawSignum()) {
			Display.Render.glSetFont(Utils.getFont(small));
			glDrawStringLeft(dx + x, ln - Utils.getFontHeight(small) / 2 + y, getSymbol());
			dx += glGetStringWidth(Utils.getFont(small), getSymbol());
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
		return val1+getSymbol()+val2;
	}

	@Override
	public FunctionTwoValues clone() {
		Cloner cloner = new Cloner();
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
		return variable1.getWidth() + 1 + (drawSignum() ? glGetStringWidth(Utils.getFont(small), getSymbol()) : 0) + variable2.getWidth();
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
		return variable1.hashCode()+7*variable2.hashCode()+883*getSymbol().hashCode();
	}
	
	@Override
	public abstract boolean equals(Object o);
}
