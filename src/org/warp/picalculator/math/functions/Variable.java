package org.warp.picalculator.math.functions;

import java.util.ArrayList;
import java.util.List;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.gui.DisplayManager;
import org.warp.picalculator.math.Calculator;

import com.rits.cloning.Cloner;

public class Variable implements Function {

	protected char var;
	protected int width;
	protected int height;
	protected int line;
	protected int[] varColor;
	protected boolean small;
	protected final Calculator root;
	protected V_TYPE type = V_TYPE.KNOWN;

	public Variable(Calculator root, char val, V_TYPE type) {
		this.root = root;
		var = val;
		this.type = type;
	}

	public Variable(Calculator root, String s, V_TYPE type) throws Error {
		this(root, s.charAt(0), type);
	}

	public char getChar() {
		return var;
	}

	public Variable setChar(char val) {
		return new Variable(root, val, type);
	}

	public V_TYPE getType() {
		return type;
	}

	public Variable setType(V_TYPE typ) {
		return new Variable(root, var, typ);
	}

	@Override
	public void generateGraphics() {
		line = calcLine();
		height = calcHeight();
		width = calcWidth();
		varColor = new int[3];
		switch (type) {
			case KNOWN:
				varColor[0] = 0;
				varColor[1] = 200;
				varColor[2] = 0;
				break;
			case UNKNOWN:
				varColor[0] = 200;
				varColor[1] = 0;
				varColor[2] = 0;
				break;
			case SOLUTION:
				varColor[0] = 0;
				varColor[1] = 0;
				varColor[2] = 200;
				break;
		}
	}

	@Override
	public String getSymbol() {
		return toString();
	}

	@Override
	public String toString() {
		return "" + getChar();
	}

//	public void draw(int x, int y, PIDisplay g, boolean small, boolean drawMinus) {
//		boolean beforedrawminus = this.drawMinus;
//		this.drawMinus = drawMinus;
//		draw(x, y, small);
//		this.drawMinus = beforedrawminus;
//	}

	@Override
	public void draw(int x, int y) {
		Utils.getFont(small).use(DisplayManager.engine);
		DisplayManager.renderer.glColor3i(varColor[0], varColor[1], varColor[2]);
		DisplayManager.renderer.glDrawStringLeft(x + 1, y, toString());
		DisplayManager.renderer.glColor3i(0, 0, 0);
	}

	@Override
	public int getHeight() {
		return height;
	}

	private int calcHeight() {
		final int h1 = Utils.getFontHeight(small);
		return h1;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public int calcWidth() {
		return Utils.getFont(small).getStringWidth(toString()) + 1;
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
		final Cloner cloner = new Cloner();
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
		final List<Function> result = new ArrayList<>();
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
			return ((Variable) o).getChar() == var && ((Variable) o).getType() == type;
		}
		return false;
	}

	@Override
	public Calculator getRoot() {
		return root;
	}
	
	public static enum V_TYPE {
		KNOWN,
		UNKNOWN,
		SOLUTION
	}
}
