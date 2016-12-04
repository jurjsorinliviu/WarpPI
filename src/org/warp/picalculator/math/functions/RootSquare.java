package org.warp.picalculator.math.functions;

import java.util.ArrayList;
import java.util.List;

import org.nevec.rjm.BigIntegerMath;
import org.warp.picalculator.Error;
import org.warp.picalculator.Errors;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathematicalSymbols;

public class RootSquare extends AnteriorFunction {

	public RootSquare(Function parent, Function value) {
		super(parent, value);
	}

	@Override
	public Function NewInstance(Function parent, Function value) {
		return new RootSquare(parent, value);
	}
	
	@Override
	public String getSymbol() {
		return MathematicalSymbols.SQUARE_ROOT;
	}
	
	@Override
	public void generateGraphics() {
		variable.setSmall(small);
		variable.generateGraphics();
		
		height = getVariable().getHeight() + 2;
		width = 1 + 4 + getVariable().getWidth() + 1;
		line = getVariable().getLine() + 2;
	}

	@Override
	protected boolean isSolvable() {
		if (variable instanceof Number) {
			if (BigIntegerMath.isqrt(((Number) variable).term).pow(2).compareTo(((Number) variable).term) == 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		try {
			Number var = (Number) getVariable();
			result.add(new Number(this.getParent(), BigIntegerMath.isqrt(var.term)));
		} catch(NullPointerException ex) {
			throw new Error(Errors.ERROR);
		} catch(NumberFormatException ex) {
			throw new Error(Errors.SYNTAX_ERROR);
		} catch(ArithmeticException ex) {
			throw new Error(Errors.NUMBER_TOO_SMALL);
		}
		return result;
	}

	@Override
	public void draw(int x, int y) {
//		glColor3f(0, 255, 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);
		
		Utils.writeSquareRoot(getVariable(), x, y, small);
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
	public boolean equals(Object o) {
		if (o instanceof RootSquare) {
			return ((RootSquare) o).variable.equals(variable);
		}
		return false;
	}
}
