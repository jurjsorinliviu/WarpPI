package org.warp.picalculator.math.functions;

import java.math.BigInteger;
import java.util.ArrayList;

import org.warp.picalculator.Error;
import org.warp.picalculator.Utils;
import org.warp.picalculator.math.MathContext;
import org.warp.picalculator.math.Function;
import org.warp.picalculator.math.FunctionSingle;
import org.warp.picalculator.math.MathematicalSymbols;

public class RootSquare extends FunctionSingle {

	public RootSquare(MathContext root, Function value) {
		super(root, value);
	}

	@Override
	public Function NewInstance(MathContext root, Function value) {
		return new RootSquare(root, value);
	}

	@Override
	public String getSymbol() {
		return MathematicalSymbols.SQUARE_ROOT;
	}

	@Override
	public void recomputeDimensions() {
		variable.setSmall(small);
		variable.recomputeDimensions();

		height = getParameter().getHeight() + 2;
		width = 1 + 4 + getParameter().getWidth() + 1;
		line = getParameter().getLine() + 2;
	}

	@Override
	protected boolean isSolvable() {
		if (variable instanceof Number) {
			if (mathContext.exactMode == false) {
				return true;
			}
			try {
				Number exponent = new Number(mathContext, BigInteger.ONE);
				exponent = exponent.divide(new Number(mathContext, 2));
				final Number resultVal = ((Number) variable).pow(exponent);
				final Number originalVariable = resultVal.pow(new Number(mathContext, 2));
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
		final ArrayList<Function> result = new ArrayList<>();
		if (mathContext.exactMode) {
			Number exponent = new Number(mathContext, BigInteger.ONE);
			exponent = exponent.divide(new Number(mathContext, 2));
			result.add(((Number) variable).pow(exponent));
		} else {
			final Number exp = new Number(mathContext, 2);
			final Number numb = (Number) variable;

			result.add(numb.pow(new Number(mathContext, 1).divide(exp)));
		}
		return result;
	}

	@Override
	public int draw(int x, int y, boolean small, int caretPos) {
//		glColor3f(0, 255, 0);
//		glFillRect(x,y,width,height);
//		glColor3f(0, 0, 0);

		Utils.writeSquareRoot(getParameter(), x, y, small);
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
