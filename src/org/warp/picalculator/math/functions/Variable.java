package org.warp.picalculator.math.functions;

import static org.warp.picalculator.device.graphicengine.Display.Render.glDrawStringLeft;
import static org.warp.picalculator.device.graphicengine.Display.Render.glGetStringWidth;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.device.graphicengine.Display;

import com.rits.cloning.Cloner;

public class Variable implements Function {

	private Function parent;
	protected char var;
	protected int width;
	protected int height;
	protected int line;
	protected boolean small;
	
	public Variable(Function parent, char val) {
		this.parent = parent;
		var = val;
	}

	public Variable(Function parent, String s) throws Error {
		this(parent, s.charAt(0));
	}

	public char getChar() {
		return var;
	}

	public void setChar(char val) {
		var = val;
	}

	@Override
	public void generateGraphics() {
		line = calcLine();
		height = calcHeight();
		width = calcWidth();
	}

	@Override
	public String getSymbol() {
		return toString();
	}

	@Override
	public String toString() {
		return ""+getChar();
	}

//	public void draw(int x, int y, PIDisplay g, boolean small, boolean drawMinus) {
//		boolean beforedrawminus = this.drawMinus;
//		this.drawMinus = drawMinus;
//		draw(x, y, small);
//		this.drawMinus = beforedrawminus;
//	}

	@Override
	public void draw(int x, int y) {
		Display.Render.glSetFont(Utils.getFont(small));
		glDrawStringLeft(x+1, y, toString());
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	private int calcHeight() {
		int h1 = Utils.getFontHeight(small);
		return h1;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	public int calcWidth() {
		return glGetStringWidth(Utils.getFont(small), toString())+1;
	}
	
	@Override
	public int getLine() {
		return line;
	}
	
	private int calcLine() {
		return Utils.getFontHeight(small) / 2;
	}
	
	public static class VariableValue {
		public final Variable v;
		public final Number n;
		
		public VariableValue(Variable v, Number n) {
			this.v = v;
			this.n = n;
		}
	}

	@Override
	public Variable clone() {
		Cloner cloner = new Cloner();
		return cloner.deepClone(this);
	}
	
	@Override
	public void setSmall(boolean small) {
		this.small = small;
	}

	@Override
	public boolean isSolved() {
		return true;
	}

	@Override
	public List<Function> solveOneStep() throws Error {
		List<Function> result = new ArrayList<>();
		result.add(this);
		return result;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Variable) {
			return ((Variable) o).getChar() == var;
		}
		return false;
	}

	public Function setParent(Function value) {
		parent = value;
		return this;
	}

	@Override
	public Function getParent() {
		return parent;
	}
}
