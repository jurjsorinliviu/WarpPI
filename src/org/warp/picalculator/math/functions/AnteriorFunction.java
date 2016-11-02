package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;
import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;

import java.util.List;

import org.nevec.rjm.NumeroAvanzatoVec;
import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.PIDisplay;
import org.warp.picalculator.device.graphicengine.Display;

import com.rits.cloning.Cloner;

public abstract class AnteriorFunction implements Function {
	public AnteriorFunction(Function parent, Function value) {
		setParent(parent);
		setVariable(value);
	}

	protected Function parent;
	protected Function variable = new Number(null, NumeroAvanzatoVec.ZERO);
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
	public abstract List<Function> solveOneStep() throws Error;
	
	@Override
	public boolean isSolved() throws Error {
		return variable.isSolved() ? !isSolvable() : false;
	}
	
	protected abstract boolean isSolvable() throws Error;
	
	@Override
	public void generateGraphics() {
		variable.setSmall(small);
		variable.generateGraphics();
		
		width = glGetStringWidth(getSymbol()) + 1 + getVariable().getWidth();
		height = variable.getHeight();
		line = variable.getLine();
	}

	@Override
	public void draw(int x, int y) {
		float h1 = getVariable().getHeight();
		int wsegno = glGetStringWidth(getSymbol());
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
		return "TODO: fare una nuova alternativa a solve().toString()";
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
	public boolean equals(Object o) {
		return o != null && o.hashCode() == this.hashCode();
	}
}
