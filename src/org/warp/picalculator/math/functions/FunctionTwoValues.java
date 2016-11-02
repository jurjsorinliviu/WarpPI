package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;
import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;

import java.util.List;

import org.nevec.rjm.Rational;
import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.graphicengine.Display;

import com.rits.cloning.Cloner;

public abstract class FunctionTwoValues implements Function {
	public FunctionTwoValues(Function parent, Function value1, Function value2) {
		this.parent = parent;
		variable1 = value1;
		variable2 = value2;
	}

	protected Function parent;
	
	protected Function variable1 = (Function) new Number(null, Rational.ZERO);
	protected Function variable2 = (Function) new Number(null, Rational.ZERO);
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
	public boolean isSolved() throws Error {
		return (variable1.isSolved() & variable2.isSolved()) ? !isSolvable() : false;
	}
	
	protected abstract boolean isSolvable() throws Error;
	
	@Override
	public abstract List<Function> solveOneStep() throws Error;
	
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
			dx += glGetStringWidth(getSymbol());
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
//		try {
//			return solve().toString();
			return "TODO: fare una nuova alternativa a solve().toString()";
//		} catch (Error e) {
//			return e.id.toString();
//		}
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
		return variable1.getWidth() + 1 + (drawSignum() ? glGetStringWidth(getSymbol()) : 0) + variable2.getWidth();
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
	public boolean equals(Object o) {
		return o != null && o.hashCode() == this.hashCode();
	}
}
