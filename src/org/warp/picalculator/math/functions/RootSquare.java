package org.warp.picalculator.math.functions;

import java.math.BigInteger;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.Calculator;
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
			if (Calculator.exactMode == false) {
				return true;
			}
			try {
				Number exponent = new Number(this.parent, BigInteger.ONE);
				exponent = exponent.divide(new Number(null, 2));
				Number resultVal = ((Number)variable).pow(exponent);
				Number originalVariable = resultVal.pow(new Number(null, 2));
				if (originalVariable.equals(variable)) {
					return true;
				}
			} catch (Exception | Error ex) {
				
			}
		}
		return false;
	}
	
	@Override
	public ArrayList<Function> solve() throws Error {
		ArrayList<Function> result = new ArrayList<>();
		if (Calculator.exactMode) {
			Number exponent = new Number(this.parent, BigInteger.ONE);
			exponent = exponent.divide(new Number(null, 2));
			result.add(((Number)variable).pow(exponent));
		} else {
			Number exp = new Number(null, 2);
			Number numb = (Number) variable;
			
			result.add(numb.pow(new Number(null, 1).divide(exp)).setParent(parent));
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
